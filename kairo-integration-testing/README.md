# Integration Testing

**Black-box integration testing is the recommended way to test your Kairo application.**
Black box testing is easy, comprehensive, and fast,
and it evolves well with your application over time.

### Black-box testing

Black-box testing verifies that multiple components within your application work correctly
by verifying only inputs/outputs,
_not by testing implementation details_.
This high-yield strategy **validates contracts**
while remaining **resilient to internal refactoring**.

### Kairo integration testing

`kairo-integration-testing` (and related dependencies)
are a form of black-box testing for Kairo applications
that targets the **service layer**.

You should use this library if:

- You're using Kairo Features and a Kairo Server.
- You're using Koin (Kairo's recommended [dependency injection](../kairo-dependency-injection) approach).

```kotlin
@ExtendWith(UserFeatureTest::class)
class GetUserTest {
  @Test
  fun `User exists`(userService: UserService): Unit =
    runTest {
      val jeff = setup {
        userService.create(UserModel.Creator.jeff)
      }
      test {
        userService.get(jeff.id)?.sanitized()
          .shouldBe(UserModel.jeff)
      }
    }
}
```

## Installation

Install `kairo-integration-testing`.\
You should also install the integration testing libraries for any other modules you rely on.

```kotlin
// build.gradle.kts

dependencies {
  testImplementation("software.airborne.kairo:kairo-integration-testing")
}
```

### Integration testing modules

- [Postgres](./postgres)

## Usage

Given our Server is broken into Domain Features,
we test each of those Features separately.

### Create a JUnit extension for your Feature

For your Feature under test,
create a JUnit extension.

```kotlin
class UserFeatureTest : FeatureTest() {
  override fun createServer(context: ExtensionContext, koinApplication: KoinApplication): Server =
    Server(
      name = "User Feature Test Server",
      features = listOf(
        DependencyInjectionFeature(koinApplication),
        UserFeature(koinApplication.koin),
      ),
    )
}
```

Include whatever Features you rely on from the service layer down.

_Note: Even if you're using REST, you **don't** need to install the REST feature.
Integration testing happens at the service layer._

### Write a GET test

```kotlin
val UserId.Companion.zero: UserId
  get() = UserId("user_00000000")

val UserModel.Creator.Companion.jeff: UserModel.Creator
  get() =
    UserModel.Creator(
      emailAddress = "jeff@example.com",
    )

val UserModel.Companion.jeff: UserModel
  get() =
    UserModel(
      id = LibraryBookId.zero,
      createdAt = Instant.epoch,
      emailAddress = "jeff@example.com",
    )

@ExtendWith(UserFeatureTest::class)
class GetUserTest {
  @Test
  fun `User doesn't exist`(userService: UserService): Unit =
    runTest {
      setup {
        userService.create(UserModel.Creator.jeff)
      }
      test {
        userService.get(UserId.random())
          .shouldBeNull()
      }
    }

  @Test
  fun `User exists`(userService: UserService): Unit =
    runTest {
      val jeff = setup {
        userService.create(UserModel.Creator.jeff)
      }
      test {
        userService.get(jeff.id)?.sanitized()
          .shouldBe(UserModel.jeff)
      }
    }
}
```

A few things to notice here:

1. `GetUserTest` doesn't extend any superclass,
   but it does extend `UserFeatureTest` through an annotation.
2. We have a happy-path test (`User exists`) and a logical error test (`User doesn't exist`).
3. Both tests use Kotlin's `runTest` function.
4. Both tests use the `setup` and `test` helpers to modularize test semantics.

### Write a POST test

```kotlin
val UserId.Companion.zero: UserId
  get() = UserId("user_00000000")

fun UserModel.sanitized(): UserModel =
  copy(
    id = UserId.zero,
    createdAt = Instant.epoch,
  )

val UserModel.Creator.Companion.jeff: UserModel.Creator
  get() =
    UserModel.Creator(
      emailAddress = "jeff@example.com",
    )

val UserModel.Companion.jeff: UserModel
  get() =
    UserModel(
      id = LibraryBookId.zero,
      createdAt = Instant.epoch,
      emailAddress = "jeff@example.com",
    )

val UserModel.Creator.Companion.noah: UserModel.Creator
  get() =
    UserModel.Creator(
      emailAddress = "noah@example.com",
    )

val UserModel.Companion.noah: UserModel
  get() =
    UserModel(
      id = LibraryBookId.zero,
      createdAt = Instant.epoch,
      emailAddress = "noah@example.com",
    )

@ExtendWith(LibraryFeatureTest::class)
class CreateUserTest {
  @Test
  fun `Happy path`(userService: UserService): Unit =
    runTest {
      val jeff = test {
        val created = userService.create(UserModel.Creator.jeff)
        created.sanitized()
          .shouldBe(UserModel.jeff)
        return@test created
      }
      postcondition {
        userService.get(jeff.id)?.sanitized()
          .shouldBe(UserModel.jeff)
      }
    }

  @Test
  fun `Duplicate email address`(userService: UserService): Unit =
    runTest {
      setup {
        userService.create(
          UserModel.Creator.noah.copy(
            emailAddress = UserModel.jeff.emailAddress,
          ),
        )
      }
      test {
        shouldThrow(DuplicateEmailAddress(UserModel.Creator.jeff.emailAddress)) {
          userService.create(UserModel.Creator.jeff)
        }
      }
    }
}
```

A few things to notice here:

1. `CreateUserTest` has the same overall format as `GetUserTest`.
2. We have a happy-path test (`Happy path`) and a logical error test (`Duplicate email address`).
3. The `UserModel.sanitized()` extension function
   ensures that we don't assert on differences in the `id` and `createdAt` fields.
   You may have your own preferred way of doing this.
