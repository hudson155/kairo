# Backend Utilities

As the name suggests, this library contains utilities.
**This library should have no dependencies.**
All backend code has access to this library by default.

All utilities should be in `io.limberapp.common.util.somePackage`,
where `somePackage` is a logical grouping.
Some utilities may be Limber-specific.

Commonly used utilities that are stdlib-esqe
may be moved to the `kotlin` package (or its subpackages) instead,
allowing them to be used without an import statement.
However, it's crucial that any utilities in the `kotlin` package are
highly general, preferably small, and definitely not Limber-specific.
