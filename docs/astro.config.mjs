// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

// https://astro.build/config
export default defineConfig({
	site: 'http://localhost:4321',
	locales: {
		root: {
			label: 'English',
			lang: 'en',
		},
	},
	integrations: [
		starlight({
			title: 'Automata',
			social: [{ icon: 'github', label: 'GitHub', href: 'https://github.com/withastro/starlight' }],
			sidebar: [
				{
					label: 'Project',
					autogenerate: { directory: 'project' },
				},
				{
					label: 'Guides',
					autogenerate: { directory: 'guides' },
				},
				{
					label: 'Reference',
					autogenerate: { directory: 'reference' },
				},
				{
					label: 'API Reference (JavaDoc)',
					items: [
						{ label: 'Automata Module', link: '/api/automata/', attrs: { target: '_blank' } },
					],
				},
			],
		}),
	],
});
