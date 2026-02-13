package kairo.adminSample.author

import kairo.id.Id

@JvmInline
public value class AuthorId(override val value: String) : Id {
  public companion object : Id.Companion<AuthorId>() {
    override fun create(payload: String): AuthorId =
      AuthorId("auth_$payload")
  }
}
