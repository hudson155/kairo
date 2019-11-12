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
    Responsible for converting data types.
    It's used by the root module, so implementation projects need not require it.
* [`errors`](/errors):
    Contains classes for JSON errors intended to be returned to the client.
    It's used by the root module, so implementation projects need not require it.
* [`exception-mapping`](/exception-mapping):
    Maps exceptions to JSON responses from the errors module.
    It's used by the root module, so implementation projects need not require it.
* [`models`](/models):
    Contains base classes for application models to inherit from.
    It's used by the root module, so implementation projects need not require it.
* [`mongo`](/mongo):
    Driver to interact with MongoDB, including automatic model/document mapping.
    Include this in implementation projects if they use MongoDB.
* [`object-mapper`](/object-mapper):
    Jackson object mapper implementation configured to use the data conversion module.
    It's used by the root module, so implementation projects need not require it.
* [`reps`](/reps):
    Contains base classes for application reps to inherit from.
    It's used by the root module, so implementation projects need not require it.
* [`testing`](/reps):
    Contains testing utilities.
    Include this in implementation projects as a test dependency (assuming you write tests).
* [`validation`](/validation):
    Provides a Validator class for ad-hoc validation, as well as the Validation class which is used
    to validate models.
    It's used by the root module, so implementation projects need not require it.

## Package Structure

Because the package structure is divided between modules, it's sometimes difficult to get an idea of
the whole picture.

```
.
├── io.limberapp.framework
├── model      # Model-related boilerplate
├── rep        # Rep-related boilerplate
├── validation # Used to validate models
└── validator  # Validator for ad-hoc validation
```
