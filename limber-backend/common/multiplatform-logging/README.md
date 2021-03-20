# Multiplatform Logging

This library provides a simple multiplatform logging facade,
which is backed by platform-specific implementations:
- JVM: SLF4J (yes, another facade).
- JS: JavaScript console.

This allows multiplatform libraries to use logging
without worrying about the implementation.
