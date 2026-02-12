package kairo.adminSample.author

import kairo.adminSample.author.exception.AuthorNotFound

public class AuthorService(
  private val store: AuthorStore,
) {
  public suspend fun get(id: AuthorId): AuthorModel =
    store.get(id) ?: throw AuthorNotFound(id)

  public suspend fun listAll(): List<AuthorModel> =
    store.listAll()

  public suspend fun create(creator: AuthorModel.Creator): AuthorModel =
    store.create(creator)

  public suspend fun update(id: AuthorId, update: AuthorModel.Update): AuthorModel =
    store.update(id, update) ?: throw AuthorNotFound(id)

  public suspend fun delete(id: AuthorId): AuthorModel {
    val model = get(id)
    store.delete(id)
    return model
  }
}
