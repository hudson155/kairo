import { defineConfig } from "astro/config";
import starlight from "@astrojs/starlight";

export default defineConfig({
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
          ],
        },
        {
          label: "Standalone libraries",
          items: [
            { slug: "modules/kairo-config" },
            { slug: "modules/kairo-coroutines" },
            { slug: "modules/kairo-darb" },
            { slug: "modules/kairo-datetime" },
            { slug: "modules/kairo-exception" },
            { slug: "modules/kairo-gcp-secret-supplier" },
            { slug: "modules/kairo-hocon" },
            { slug: "modules/kairo-id" },
            { slug: "modules/kairo-image" },
            { slug: "modules/kairo-logging" },
            { slug: "modules/kairo-money" },
            { slug: "modules/kairo-optional" },
            { slug: "modules/kairo-protected-string" },
            { slug: "modules/kairo-reflect" },
            { slug: "modules/kairo-serialization" },
            { slug: "modules/kairo-testing" },
            { slug: "modules/kairo-util" },
            { slug: "modules/kairo-validation" },
          ],
        },
        {
          label: "Application libraries",
          items: [
            { slug: "modules/kairo-application" },
            { slug: "modules/kairo-client" },
            { slug: "modules/kairo-dependency-injection" },
            { slug: "modules/kairo-feature" },
            { slug: "modules/kairo-health-check" },
            { slug: "modules/kairo-integration-testing" },
            { slug: "modules/kairo-ktor" },
            { slug: "modules/kairo-mailersend" },
            { slug: "modules/kairo-rest" },
            { slug: "modules/kairo-server" },
            { slug: "modules/kairo-slack" },
            { slug: "modules/kairo-sql" },
            { slug: "modules/kairo-stytch" },
          ],
        },
      ],
    }),
  ],
});
