# Limber Backend Framework

The Limber backend framework is a highly dynamic backend application framework built on Ktor.

When creating a new Limber project (an implementation project), your backend application should
include, the root module as a dependency. Read through the other modules below to understand what
they are and see if you need them too.

## Modules

* [`core`](/core):
    This is the nucleus of the Limber framework.
    It's used by the root module, so implementation projects need not require it.
* [`data-conversion`](/data-conversion):
    * Responsible for converting data types.
        This is used for serialization and deserialization,
        both by registering with Jackson
        (for serializing and deserializing arbitrary objects as well as request/response bodies)
        and by registering with Ktor DataConversion
        (for serializing and deserializing request/response parameters).
    * It's used by the root module, so implementation projects need not require it.
* [`errors`](/errors):
    Contains classes for JSON errors intended to be returned to the client.
    It's used by the root module, so implementation projects need not require it.
* [`exception-mapping`](/exception-mapping):
    Maps exceptions to JSON responses from the errors module.
    It's used by the root module, so implementation projects need not require it.
* [`models`](/models):
    * Contains base classes for application models to inherit from.
        Models are used in the application/database layer.
        Models should never be used in the API layer.
    * It's used by the root module, so implementation projects need not require it.
* [`mongo`](/mongo):
    Driver to interact with MongoDB, including automatic model/document mapping.
    Include this in implementation projects if they use MongoDB.
* [`object-mapper`](/object-mapper):
    * Custom Jackson object mapper implementation.
        It's configured with some default modules (e.g. Kotlin),
        default settings (e.g. ability to ignore unknown properties),
        and uses the `data-conversion` module for serialization/deserialization.
        It should be used across Limber.
    * It's used by the root module, so implementation projects need not require it.
* [`reps`](/reps):
    * Contains base classes for application reps to inherit from.
        Reps are used in the API layer.
        Models should never be used in the application/database layer.
    * It's used by the root module, so implementation projects need not require it.
* [`testing`](/reps):
    Contains testing utilities.
    Include this in implementation projects as a test dependency (assuming you write tests).
* [`validation`](/validation):
    * Provides a Validator class for ad-hoc validation,
        as well as the Validation class which is used to validate reps.
    * It's used by the root module, so implementation projects need not require it.

## Package Structure

Because the package structure is divided between modules, it's sometimes difficult to get an idea of
the whole picture.

```
.
├── io.limberapp.framework
├── dataConversion   # Type conversion
│   └─ conversionService # Individual type converters
├── jackson          # Code related to Jackson serialization
│   └─ module        # Custom modules
│   └─ objectMapper  # Custom object mapper
├── model            # Model-related boilerplate
├── rep              # Rep-related boilerplate
├── validation       # Used to validate models
│   └─ util              # Individual validators
└── validator        # Validator for ad-hoc validation
```
