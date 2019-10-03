# Limber

Limber is a highly dynamic application framework built on Ktor and Preact.

* [`core`](/core):
    This is the main module for the Limber framework. All implementation projects will require this.
* [`errors`](/errors):
    Contains classes for JSON errors intended to be returned to the client.
* [`models`](/models):
    Contains base classes for application models to inherit from. This module is used by the core
    module, among others, so you don't need to included it explicitly in your project.
