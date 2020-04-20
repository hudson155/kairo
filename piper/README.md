# Piper

The Piper backend framework is a highly dynamic backend application framework built on Ktor.
Parts of Piper are build to work on Kotlin multiplatform (for use with Kotlin-React or in mobile environments),
but implementation projects don't need to use these features.

**Modules**
A Piper application is made up of a number of Piper modules,
where each module should represent some distinct concept within the application.
For example, most projects likely have a `user` module, and if the application is used by organizations,
it will likely have an `organization` or `tenant` module as well.
The rest of the application's modules will depend on the details of the application,
but some examples include `auth`, `payments`, `notifications`, and `tasks`.

When creating a new Piper project (an "implementation project"),
the backend application should include the `application` artifact as a dependency,
and each module should include the `module` artifact as a dependency.
Read through the other artifacts below to understand what they are and see if you need them too.

## Artifacts

* [`application`](/application):
    * JVM-only.
    * This is the core of the Piper application framework.
      This artifact should be included by Piper implementation projects.
* [`config`](/config):
    * JVM-only.
    * Shared configuration classes for Piper applications.
        Not all configuration classes are in here.
        Notably, some that are only relevant for the application are in the `application` artifact.
* [`data-conversion`](/data-conversion):
    * Multiplatform.
    * Responsible for converting data types such as UUIDs. Handles multiple platforms.
* [`errors`](/errors):
    * Multiplatform.
    * Contains JSON errors intended to be returned to the client.
        Every time something goes wrong (whether it's a 4xx error or a 5xx error),
        an error from this artifact should is returned.
* [`exception-mapping`](/exception-mapping):
    * JVM-only.
    * Maps exceptions to JSON responses from the `errors` artifact.
* [`exceptions`](/exceptions):
    * JVM-only.
    * Exception base classes for typical 4xx-related errors.
* [`ktor-auth`](/ktor-auth):
    * JVM-only.
    * Config-driven authentication setup for Ktor.
* [`module`](/module):
    * JVM-only
    * This is the core of Piper modules.
      This artifact should be included by Piper modules, but not by the Piper implementation project / application.
* [`reps`](/reps):
    * Multiplatform.
    * Contains base classes for application reps to inherit from.
        Reps are used in the API layer on all platforms.
        Reps should never be used in the application layer or database layer on the backend.
* [`rest-interface`](/rest-interface):
    * Multiplatform.
    * Supports JVM and JS REST interfaces.
* [`serialization`](/serialization):
    * Multiplatform.
    * Multiplatform (JVM/JS) serialization code for reps.
        Allows the same classes to be serialized and deserialized in the JVM and in JS.
        Also contains boilerplate for polymorphic serialization.
* [`sql`](/sql):
    * JVM
    * Driver to interact with a SQL database.
        Currently, only Postgres is supported.
        Include this in modules if they use a SQL database.
* [`testing`](/testing):
    * TODO
    * Contains testing utilities.
    * Include this in implementation project modules as a test dependency
        (assuming you write tests).
* [`types`](/types):
    * Multiplatform.
    * Contains types common across platforms, but with different implementations.
* [`util`](/util):
    * Multiplatform.
    * Contains multiplatform and platform-specific utilities.
* [`validation`](/validation):
    * Multiplatform
    * Regex-based validation for ad-hoc validation,
        as well as the Validation class which is used to validate reps.
        Note that rep validation itself is not in this artifact,
        but in the `reps` artifact.

## Package Structure

Because the package structure is divided between modules, it's sometimes difficult to get an idea of
the whole picture.

```
.
├── com.piperframework
├── authorization      # Piper authorization configuration
├── config             # Piper framework configuration
├── contentNegotiation # Ktor HTTP content negotiation
├── dataConversion     # Type conversion
├── endpoint           # Code for REST API endpoints
├── error              # Response body error objects
├── exception          # Exceptions that might be thrown
├── exceptionMapping   # Mapping exceptions to errors
├── ktorAuth           # Ktor/Piper authorization integration
├── module             # Code for Piper modules
├── rep                # Rep-related boilerplate (API layer)
├── restInterface      # JVM and JS REST interfaces
├── serialization      # Serialization and deserialization code
├── sql                # SQL driver
├── testing            # Testing code
├── types              # Custom types
├── store              # Code for Piper stores
├── util               # Util
├── validation         # Value and rep validation
└── validator          # Validator for ad-hoc validation
```
