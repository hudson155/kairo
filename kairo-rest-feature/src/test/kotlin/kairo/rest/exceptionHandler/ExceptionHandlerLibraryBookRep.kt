package kairo.rest.exceptionHandler

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

internal data class ExceptionHandlerLibraryBookRep(
  val title: String,
  val authors: List<Author>,
  val isSeries: Boolean,
  val type: Type,
) {
  @Suppress("unused")
  internal enum class Type { Audio, Print }

  internal data class Creator(
    val authors: List<Author>,
    val type: Type,
  ) {
    @Suppress("unused")
    @JsonIgnore
    val firstAuthor: Author? = authors.firstOrNull()
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
    JsonSubTypes.Type(Author.Named::class, name = "Named"),
    JsonSubTypes.Type(Author.Anonymous::class, name = "Anonymous"),
  )
  internal sealed class Author {
    internal data class Named(
      val firstName: String,
      val lastName: String,
    ) : Author()

    internal data object Anonymous : Author()
  }
}
