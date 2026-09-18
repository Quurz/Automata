/**
 * UMLDoclet Pan & Zoom Interactive Viewer for Javadoc
 * Provides mouse-wheel zoom, drag-to-pan, toolbar controls and fullscreen modal view for UML diagrams.
 */
(function() {
    'use strict';

    function initUmlPanZoom() {
        const objects = document.querySelectorAll('object[type="image/svg+xml"], object[data$=".svg"]');
        if (!objects || objects.length === 0) return;

        createModalContainer();

        objects.forEach(function(obj) {
            if (obj.dataset.umlPanZoomInitialized) return;
            obj.dataset.umlPanZoomInitialized = 'true';

            setupDiagram(obj);
        });
    }

    let globalModal = null;
    let modalSvgClone = null;
    let modalPanZoomState = null;

    function createModalContainer() {
        if (document.getElementById('uml-panzoom-modal')) return;

        const modal = document.createElement('div');
        modal.id = 'uml-panzoom-modal';
        modal.className = 'uml-modal';
        modal.innerHTML = `
            <div class="uml-modal-backdrop"></div>
            <div class="uml-modal-dialog">
                <div class="uml-modal-header">
                    <span class="uml-modal-title">UML Diagram</span>
                    <div class="uml-modal-toolbar">
                        <button class="uml-btn uml-btn-zoom-in" title="Zoom in (+)">+</button>
                        <button class="uml-btn uml-btn-zoom-out" title="Zoom out (−)">−</button>
                        <button class="uml-btn uml-btn-reset" title="Reset view">↺</button>
                        <button class="uml-btn uml-btn-close" title="Close (Esc)">✕</button>
                    </div>
                </div>
                <div class="uml-modal-body">
                    <div class="uml-modal-viewport" id="uml-modal-viewport"></div>
                </div>
                <div class="uml-modal-footer">
                    <span>Mausrad: Zoom • Ziehen: Verschieben • Doppelklick: Reset • ESC: Schließen</span>
                </div>
            </div>
        `;
        document.body.appendChild(modal);
        globalModal = modal;

        modal.querySelector('.uml-modal-backdrop').addEventListener('click', closeModal);
        modal.querySelector('.uml-btn-close').addEventListener('click', closeModal);

        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape' && globalModal.classList.contains('active')) {
                closeModal();
            }
        });

        const zoomInBtn = modal.querySelector('.uml-modal-toolbar .uml-btn-zoom-in');
        const zoomOutBtn = modal.querySelector('.uml-modal-toolbar .uml-btn-zoom-out');
        const resetBtn = modal.querySelector('.uml-modal-toolbar .uml-btn-reset');

        zoomInBtn.addEventListener('click', function() {
            if (modalPanZoomState) modalPanZoomState.zoom(1.25);
        });
        zoomOutBtn.addEventListener('click', function() {
            if (modalPanZoomState) modalPanZoomState.zoom(0.8);
        });
        resetBtn.addEventListener('click', function() {
            if (modalPanZoomState) modalPanZoomState.reset();
        });
    }

    function openModal(svgDoc, title) {
        if (!globalModal) createModalContainer();
        const viewport = document.getElementById('uml-modal-viewport');
        viewport.innerHTML = '';

        if (title) {
            const titleElem = globalModal.querySelector('.uml-modal-title');
            if (titleElem) titleElem.textContent = 'UML Diagram: ' + title;
        }

        const svgElement = svgDoc ? svgDoc.querySelector('svg') : null;
        if (!svgElement) return;

        const importedSvg = document.importNode(svgElement, true);
        importedSvg.removeAttribute('width');
        importedSvg.removeAttribute('height');
        importedSvg.style.width = '100%';
        importedSvg.style.height = '100%';
        importedSvg.style.display = 'block';
        importedSvg.style.overflow = 'hidden';

        viewport.appendChild(importedSvg);
        globalModal.classList.add('active');
        document.body.style.overflow = 'hidden';

        modalPanZoomState = attachPanZoom(viewport, importedSvg);
    }

    function closeModal() {
        if (!globalModal) return;
        globalModal.classList.remove('active');
        document.body.style.overflow = '';
        if (modalPanZoomState && modalPanZoomState.destroy) {
            modalPanZoomState.destroy();
            modalPanZoomState = null;
        }
    }

    function setupDiagram(obj) {
        const wrapper = document.createElement('div');
        wrapper.className = 'uml-diagram-card';

        // Preserve float or inline alignment from original object style
        const origStyle = obj.getAttribute('style') || '';
        if (origStyle.includes('float:right') || origStyle.includes('float: right')) {
            wrapper.classList.add('uml-float-right');
        } else if (origStyle.includes('margin-left:auto') || origStyle.includes('display:block')) {
            wrapper.classList.add('uml-block-center');
        }

        const toolbar = document.createElement('div');
        toolbar.className = 'uml-diagram-controls';
        toolbar.innerHTML = `
            <button class="uml-btn uml-btn-zoom-in" title="Vergrößern">+</button>
            <button class="uml-btn uml-btn-zoom-out" title="Verkleinern">−</button>
            <button class="uml-btn uml-btn-reset" title="Ansicht zurücksetzen">↺</button>
            <button class="uml-btn uml-btn-fullscreen" title="Vollbild-Pane öffnen">⛶</button>
        `;

        const viewport = document.createElement('div');
        viewport.className = 'uml-diagram-viewport';

        obj.parentNode.insertBefore(wrapper, obj);
        wrapper.appendChild(toolbar);
        wrapper.appendChild(viewport);
        viewport.appendChild(obj);

        function onSvgReady() {
            try {
                const svgDoc = obj.contentDocument;
                if (!svgDoc) return;
                const svg = svgDoc.querySelector('svg');
                if (!svg) return;

                const panZoom = attachPanZoom(viewport, svg, svgDoc);

                const dataAttr = obj.getAttribute('data') || '';
                const fileName = dataAttr.split('/').pop().replace('.svg', '');

                toolbar.querySelector('.uml-btn-zoom-in').addEventListener('click', function(e) {
                    e.stopPropagation();
                    panZoom.zoom(1.25);
                });
                toolbar.querySelector('.uml-btn-zoom-out').addEventListener('click', function(e) {
                    e.stopPropagation();
                    panZoom.zoom(0.8);
                });
                toolbar.querySelector('.uml-btn-reset').addEventListener('click', function(e) {
                    e.stopPropagation();
                    panZoom.reset();
                });
                toolbar.querySelector('.uml-btn-fullscreen').addEventListener('click', function(e) {
                    e.stopPropagation();
                    openModal(svgDoc, fileName);
                });

                wrapper.addEventListener('dblclick', function(e) {
                    if (e.target.closest('.uml-diagram-controls')) return;
                    openModal(svgDoc, fileName);
                });

            } catch (err) {
                console.warn('UML PanZoom initialization warning:', err);
            }
        }

        obj.addEventListener('load', onSvgReady);
        if (obj.contentDocument && obj.contentDocument.querySelector('svg')) {
            onSvgReady();
        }
    }

    function attachPanZoom(container, svg, innerDoc) {
        let viewBox = null;
        let initialViewBox = null;

        function initViewBox() {
            const vb = svg.getAttribute('viewBox');
            if (vb) {
                const parts = vb.trim().split(/[\s,]+/).map(Number);
                if (parts.length === 4 && parts.every(n => !isNaN(n))) {
                    viewBox = { x: parts[0], y: parts[1], w: parts[2], h: parts[3] };
                    initialViewBox = { ...viewBox };
                    return;
                }
            }
            // If no viewBox, calculate from getBBox or attributes
            const bbox = svg.getBBox ? svg.getBBox() : { x: 0, y: 0, width: svg.clientWidth || 500, height: svg.clientHeight || 400 };
            viewBox = { x: bbox.x, y: bbox.y, w: bbox.width || 500, h: bbox.height || 400 };
            initialViewBox = { ...viewBox };
            svg.setAttribute('viewBox', `${viewBox.x} ${viewBox.y} ${viewBox.w} ${viewBox.h}`);
        }

        initViewBox();

        function applyViewBox() {
            svg.setAttribute('viewBox', `${viewBox.x} ${viewBox.y} ${viewBox.w} ${viewBox.h}`);
        }

        function zoom(factor, clientX, clientY) {
            if (!viewBox) return;
            const rect = container.getBoundingClientRect();
            let px = 0.5;
            let py = 0.5;

            if (clientX !== undefined && clientY !== undefined && rect.width > 0 && rect.height > 0) {
                px = Math.max(0, Math.min(1, (clientX - rect.left) / rect.width));
                py = Math.max(0, Math.min(1, (clientY - rect.top) / rect.height));
            }

            const newW = viewBox.w / factor;
            const newH = viewBox.h / factor;

            // Constrain zoom limits
            if (newW < initialViewBox.w * 0.05 || newW > initialViewBox.w * 25) return;

            viewBox.x += px * (viewBox.w - newW);
            viewBox.y += py * (viewBox.h - newH);
            viewBox.w = newW;
            viewBox.h = newH;

            applyViewBox();
        }

        function pan(dx, dy) {
            if (!viewBox) return;
            const rect = container.getBoundingClientRect();
            if (rect.width <= 0 || rect.height <= 0) return;

            const scaleX = viewBox.w / rect.width;
            const scaleY = viewBox.h / rect.height;

            viewBox.x -= dx * scaleX;
            viewBox.y -= dy * scaleY;

            applyViewBox();
        }

        function reset() {
            if (!initialViewBox) return;
            viewBox = { ...initialViewBox };
            applyViewBox();
        }

        let isDragging = false;
        let lastX = 0;
        let lastY = 0;

        function onMouseDown(e) {
            if (e.button !== 0) return; // only primary button
            isDragging = true;
            lastX = e.clientX;
            lastY = e.clientY;
            container.classList.add('is-dragging');
            if (innerDoc && innerDoc.body) innerDoc.body.style.cursor = 'grabbing';
            e.preventDefault();
        }

        function onMouseMove(e) {
            if (!isDragging) return;
            const dx = e.clientX - lastX;
            const dy = e.clientY - lastY;
            lastX = e.clientX;
            lastY = e.clientY;
            pan(dx, dy);
            e.preventDefault();
        }

        function onMouseUp(e) {
            if (!isDragging) return;
            isDragging = false;
            container.classList.remove('is-dragging');
            if (innerDoc && innerDoc.body) innerDoc.body.style.cursor = '';
        }

        function onWheel(e) {
            e.preventDefault();
            const delta = e.deltaY;
            const factor = delta > 0 ? 0.9 : 1.11;
            zoom(factor, e.clientX, e.clientY);
        }

        // Event listeners on container
        container.addEventListener('mousedown', onMouseDown);
        window.addEventListener('mousemove', onMouseMove);
        window.addEventListener('mouseup', onMouseUp);
        container.addEventListener('wheel', onWheel, { passive: false });

        // Also listen inside innerDoc if available
        if (innerDoc) {
            innerDoc.addEventListener('mousedown', function(e) {
                // translate innerDoc coords to window coords
                const rect = container.getBoundingClientRect();
                isDragging = true;
                lastX = rect.left + e.clientX;
                lastY = rect.top + e.clientY;
                container.classList.add('is-dragging');
                if (innerDoc.body) innerDoc.body.style.cursor = 'grabbing';
                e.preventDefault();
            });
            innerDoc.addEventListener('mousemove', function(e) {
                if (!isDragging) return;
                const rect = container.getBoundingClientRect();
                const currentX = rect.left + e.clientX;
                const currentY = rect.top + e.clientY;
                const dx = currentX - lastX;
                const dy = currentY - lastY;
                lastX = currentX;
                lastY = currentY;
                pan(dx, dy);
                e.preventDefault();
            });
            innerDoc.addEventListener('mouseup', onMouseUp);
            innerDoc.addEventListener('wheel', function(e) {
                e.preventDefault();
                const rect = container.getBoundingClientRect();
                const factor = e.deltaY > 0 ? 0.9 : 1.11;
                zoom(factor, rect.left + e.clientX, rect.top + e.clientY);
            }, { passive: false });
        }

        return {
            zoom: zoom,
            pan: pan,
            reset: reset,
            destroy: function() {
                container.removeEventListener('mousedown', onMouseDown);
                window.removeEventListener('mousemove', onMouseMove);
                window.removeEventListener('mouseup', onMouseUp);
                container.removeEventListener('wheel', onWheel);
            }
        };
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initUmlPanZoom);
    } else {
        initUmlPanZoom();
    }
})();
