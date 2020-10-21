# `limber-backend:common:reps`

Contains base classes for representations that get sent over the network.
* `CompleteRep` is used for responses.
* `CreationRep` is used for creation requests,
    and is validatable.
* `UpdateRep` is used for modification requests,
    and is validatable.

Rep validation logic is also included in this module.
