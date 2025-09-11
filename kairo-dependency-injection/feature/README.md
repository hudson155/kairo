# Dependency Injection

Don't waste time and energy manually wiring your classes together.
Let Koin create and wire everything instead!

[Koin](https://insert-koin.io/) is a **lightweight, Kotlin-native dependency injection** library
integrated into the Kairo ecosystem.

### Why Koin?

- **Kotlin-first with compile-time generation:**
  - No proxies, simple Kotlin DSL.
  - Dynamic modules (if you use them) are generated at compile time.
    No reflection!

- **Kairo-integrated:**
  - Uses Kairo lifecycle hooks.
  - Your custom Features can easily provide bindings.

- **Easy testing:**
  Koin + JUnit parameter resolvers let you add parameters to your integration test functions.

## Installation

Install `kairo-dependency-injection-feature`.\
You don't need to install Koin separately — it's included by default.

If you want to use Koin annotations (recommended),
also add the relevant KSP lines from the snippet below.

```kotlin
// build.gradle.kts

plugins {
  id("com.google.devtools.ksp")
}

dependencies {
  ksp("io.insert-koin:koin-ksp-compiler")
  implementation("software.airborne.kairo:kairo-dependency-injection-feature")
}
```

## Usage

First, add the Feature to your Server.

```kotlin
val koinApplication = koinApplication()

val features = listOf(
  DependencyInjectionFeature(koinApplication),
)
```

Refer to [Koin's documentation](https://insert-koin.io/docs/reference/introduction)
for advanced usage.

### Using annotations (recommended)

Bindings are created by annotating classes with `@Single` or `@Factory`.

```kotlin
@Single
class UserStore(
  private val database: R2dbcDatabase, // Injected automatically.
) {
  suspend fun get(id: UserId): UserModel? =
    suspendTransaction(db = database) {
      UserTable
        .selectAll()
        .where { UserTable.id eq id }
        .map(UserModel::fromRow)
        .singleNullOrThrow()
    }
}
```

```kotlin
@Single
class UserService(
  private val userStore: UserStore, // Injected automatically.
) {
  suspend fun get(id: UserId): UserModel? =
    userStore.get(id)
}
```

Constructor parameters are automatically injected.

Koin's KSP-based annotation processor will pick up on these bindings,
you just need to register them with your Feature.

```kotlin
@org.koin.core.annotation.Module
@org.koin.core.annotation.ComponentScan
class UserFeature : Feature(), KoinModule {
  override val name: String = "User"

  // Genereated by KSP; name includes Java package.
  override val koinModule: Module = user_UserFeature
}
```

### Wiring it manually instead

Instead of using annotations and KSP codegen,
you can wire it up manually if you prefer.

```kotlin
class UserFeature(
  private val koin: Koin,
) : Feature(), KoinModule {
  override val name: String = "User"

  override val koinModule: Module =
    module {
      single<UserStore> { UserStore(...) }
      single<UserService> { UserService(...) }
    }
}
```
