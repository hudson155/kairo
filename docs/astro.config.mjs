import { defineConfig } from "astro/config";
import starlight from "@astrojs/starlight";
import moduleSidebar from "./src/generated-sidebar.json";

export default defineConfig({
  site: "https://kairo.airborne.software",
  redirects: {
    "/api": "/api/index.html",
    "/api/": "/api/index.html",
  },
  integrations: [
    starlight({
      title: "Kairo",
      description:
        "Your one-stop toolkit for production-ready Kotlin backends.",
      social: [
        {
          icon: "github",
          label: "GitHub",
          href: "https://github.com/hudson155/kairo",
        },
      ],
      sidebar: [
        {
          label: "Getting started",
          items: [
            { label: "Introduction", slug: "index" },
            { label: "Installation", slug: "getting-started" },
            { label: "Style guide", slug: "style-guide" },
            { label: "Changelog", slug: "changelog" },
          ],
        },
        ...moduleSidebar,
        {
          label: "API Reference",
          items: [
            {
              label: "API Docs (Dokka)",
              link: "/api/",
            },
          ],
        },
      ],
    }),
  ],
});
