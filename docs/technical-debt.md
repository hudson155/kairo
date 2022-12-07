# Technical Debt

This is a non-comprehensive list of technical debt.
It is not sorted.

## Security

- **Passwords stored in plaintext:**
  [Terraform stores sensitive values in plaintext in the state file](https://github.com/hashicorp/terraform/issues/516).
  If Terraform changes this, great.
  Otherwise, we may want to resolve it by moving to [Atlantis](https://www.runatlantis.io/) or something.

## UX

- **Toasts use default styling:**
  Our toasts use `react-toastify`'s default styling (with some overrides).
  This also means they are not in Storybook.

## Infrastructure

- **Static assets:**
  React's default static assets were removed in https://github.com/limberapp-io/limber/pull/1274.
  HTML was customized in https://github.com/limberapp-io/limber/pull/1358/files.
  Add the following.
  - `favicon.ico` and link from `index.html`.
  - `logo192.png`, `logo512.png`, etc. And link from `index.html`.
  - `manifest.json` and link from `index.html`.
  - HTML `theme-color`, `description`, and other `meta` tags.
  - HTML `title`.
  - `apple-touch-icon`, etc.
  - Google if there's more stuff too.

- **Cooldown period exceeds GAE's:**
  The cooldown implementation takes longer than GAE's permitted 3 seconds.
  https://cloud.google.com/appengine/docs/standard/how-instances-are-managed#shutdown.

- **Request IDs:**
  Use Ktor's CallId plugin, and something in our ingress or the frontend.

- **Metrics:**
  Use either Ktor's Metrics or MicrometerMetrics plugin (or both?).

- **Create GitHub Action to delete old Docker images.**

- **Deploy.**
  - Review the entire `infrastructure/` folder,
    adding any missing documentation and making any necessary updates.
  - Make the first deployment.
  - Revert https://github.com/limberapp-io/limber/pull/1376 (kinda).
  - Update any other jhudson.ca or localhost references?

- **Write CD.**
  Re-enable Gradle CD and write Yarn CD.

- **Move from Google App Engine to Kubernetes.**

## Dev experience

- **No CSS linting:**
  Although IntelliJ is opinionated about CSS formatting
  (specifically, property ordering),
  we do not have linting set up.

- **Detekt type resolution:**
  We use a hack to enable Detekt type resolution.
  See `DetektFeature`.
  This can be removed with Detekt 2.0.

- **Server restarted per test:**
  For backend testing, the server restarts for each and every test.
  It would be preferable (faster) to only start the server once per feature.
  Also turn migrations off in the config at this point.

- **No JS testing:**
  Write JS tests, add them to CI, and remove the `--passWithNoTests` flag.

## Documentation

- **Missing functional READMEs:**
  Backend and web should have functional READMEs
  explaining how to set up the repo, run tests, lint, storybook, etc.

- **Missing Feature and Server creation guides.**
