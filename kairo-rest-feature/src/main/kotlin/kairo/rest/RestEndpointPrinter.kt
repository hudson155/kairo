package kairo.rest

/**
 * There are varying use cases for converting [RestEndpointTemplate] instances to strings.
 * Implementations of this class achieve those varying use cases.
 */
internal abstract class RestEndpointPrinter {
  abstract fun write(template: RestEndpointTemplate): String
}
