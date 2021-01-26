# Limber Modules

Limber modules are the base unit of a Limber application.
An application will have a module for each distinct concept within it.

Modules come in 2 flavors:
- **Pure modules** are modules that directly extend the `Module` class.
    This type of module must be fully self-contained - it cannot define API endpoints.
    The existence of pure modules should not be visible to the end user
    (or even to an API consumer).
    An example of a pure module
    is one that enables interaction with a certain type of database.
- **Feature modules** are modules that extend the `Feature` class.
    The feature class is not provided by this library.
    Rather, it's provided by the [feature library](/limber-backend/common/feature).
    Feature modules define functionality and API endpoints
    for a particular feature of the application.
    Feature modules often rely on one or more pure modules.
    An example of a feature module
    is one that defines payment processing endpoints.

A small application with 3 features might have 1 module for each feature,
plus one module for the database.
