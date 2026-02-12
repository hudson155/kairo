# Admin Dashboard

A server-rendered admin UI for Kairo applications.
Provides a full-featured dashboard for inspecting endpoints, config, JVM metrics,
database tables, health checks, features, logging, dependencies, integrations, and errors.

Optional tabs for Slack, Auth (Stytch), and Email (MailerSend) appear automatically
when those features are detected at runtime.

## Installation

Install `kairo-admin`.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-admin")
}
```

## Usage

Add the Feature to your Server.

```kotlin
val features = listOf(
  AdminDashboardFeature(
    config = AdminDashboardConfig(
      serverName = "My Service",
    ),
  ),
)
```

The dashboard is available at `/_admin` by default.

### Configuration

All config options have sensible defaults.

```kotlin
AdminDashboardConfig(
  pathPrefix = "/_admin",       // URL prefix for the dashboard.
  title = "Kairo Admin",        // Dashboard title.
  serverName = null,            // Server name displayed in the sidebar.
  docsUrl = null,               // Link to external documentation.
  apiDocsUrl = null,            // Link to API / framework docs.
  githubRepoUrl = null,         // Link to GitHub repo (enables "View on GitHub" links).
  kdocsUrl = null,              // KDocs URL (auto-detected if kairo-kdocs is present).
)
```

### Auto-discovery

Most dashboard data is auto-discovered from registered features at startup:

- **Endpoints** from Ktor Application attributes.
- **Config sources** from the `CONFIG` environment variable and classpath.
- **Health checks** from `HealthCheckFeature`.
- **Dependencies** from `DependencyInjectionFeature` (Koin).
- **Database** from `ConnectionFactory` in Koin.
- **Slack channels** from `SlackFeature` (optional, tab hidden if not present).
- **Auth modules** from `StytchFeature` (optional, tab hidden if not present).
- **Email templates** from `MailersendFeature` (optional, tab hidden if not present).

All optional module dependencies are `compileOnly`,
so they don't add transitive dependencies to your application.

### Manual config sources

If auto-detection isn't sufficient, you can provide config sources explicitly.

```kotlin
AdminDashboardFeature(
  configSources = listOf(
    AdminConfigSource("production", productionConfigText),
  ),
)
```

### KDocs integration

If [kairo-kdocs](../kairo-kdocs/README.md) is included in your application,
the admin dashboard automatically detects it and adds a KDocs link to the sidebar.

### Error tracking

Capture server errors by reporting them to the `errorCollector`.

```kotlin
val adminFeature = AdminDashboardFeature()

// In your error handler:
adminFeature.errorCollector.record(exception)
```

Errors are stored in memory and can be viewed and cleared from the Errors tab.
