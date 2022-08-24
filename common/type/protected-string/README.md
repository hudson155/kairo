# `ProtectedString` type

`ProtectedString` should be used instead of `String` for any sensitive data
(for example, secrets or bank account numbers).
It serializes and deserializes just like a `String`,
and its equality is determined the same way.
However, the `toString` method is overridden to output `REDACTED`,
meaning that _the string never ends up in logs_.

Note: Using `ProtectedString` is insufficient for PCI data.
