# Util library

_This library is included automatically in all Gradle modules._

Utilities should not be Limber-specific.
They should also be fairly lightweight, and not require any dependencies,
since this library is shared so heavily.

**Note about the `kotlin` package:**
Some library functions are placed in the `kotlin` package.
These don't require imports which is nice,
but this should be used sparingly,
and such functions should have functionality, interfaces, and performance
equivalent with what you'd expect from a standard library.
