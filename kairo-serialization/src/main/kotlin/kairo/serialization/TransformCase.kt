package kairo.serialization

/**
 * Transforms the case (lowercase/uppercase) during deserialization.
 */
@Target(AnnotationTarget.FIELD)
public annotation class TransformCase(val type: Type) {
  public enum class Type {
    None {
      override fun transform(original: String): String =
        original
    },
    Lowercase {
      override fun transform(original: String): String =
        original.lowercase()
    },
    Uppercase {
      override fun transform(original: String): String =
        original.uppercase()
    },
    ;

    public abstract fun transform(original: String): String
  }
}
