package kairo.adminSample.author.exception

import io.ktor.http.HttpStatusCode
import kairo.adminSample.author.AuthorId
import kairo.exception.LogicalFailure

public class AuthorNotFound(
  authorId: AuthorId,
) : LogicalFailure("Author not found (authorId=${authorId.value}).") {
  override val type: String = "AuthorNotFound"
  override val status: HttpStatusCode = HttpStatusCode.NotFound
}
