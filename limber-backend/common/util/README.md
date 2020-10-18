# `limber-backend:common:util`

As the name suggests, this contains utilities.
**This Gradle module should have no dependencies.**

All utilities should be in `io.limberapp.common.util.somePackage`,
where `somePackage` is a logical grouping.
Some utilities may be Limber-specific.

Commonly used utilities that are stdlib-esqe
may be moved to the `kotlin` package (or its subpackages) instead.
However, it's crucial that any utilities in the `kotlin` package are
highly general, preferably small, and definitely not Limber-specific.
