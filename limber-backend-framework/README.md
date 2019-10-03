# Limber

Limber is a highly dynamic application framework built on Ktor and Preact.

* [`core`](/core):
    This is the main module for the Limber framework. All implementation projects will require this.
* [`data-conversion`](/data-conversion):
    Responsible for converting data types. This should be used in implementation projects as part of
    Ktor's DataConversion and ContentNegotiation installations.
* [`errors`](/errors):
    Contains classes for JSON errors intended to be returned to the client.
* [`models`](/models):
    Contains base classes for application models to inherit from. This module is used by the core
    module, among others, so you don't need to included it explicitly in your project.
* [`object-mapper`](/object-mapper):
    Jackson object mapper implementation configured to use the data conversion module. This should
    be used in implementation projects as part of Ktor's ContentNegotiation installation. It's also
    used by database modules.
* [`validation`](/validation):
    Provides a Validator class for ad-hoc validation, as well as the Validation class which is used
    to validate models.
