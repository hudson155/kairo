package kairo.adminSample.libraryBook

import kairo.adminSample.libraryBook.exception.LibraryBookNotFound

public class LibraryBookService(
  private val store: LibraryBookStore,
) {
  public suspend fun get(id: LibraryBookId): LibraryBookModel =
    store.get(id) ?: throw LibraryBookNotFound(id)

  public suspend fun listAll(): List<LibraryBookModel> =
    store.listAll()

  public suspend fun create(creator: LibraryBookModel.Creator): LibraryBookModel =
    store.create(creator)

  public suspend fun update(id: LibraryBookId, update: LibraryBookModel.Update): LibraryBookModel =
    store.update(id, update) ?: throw LibraryBookNotFound(id)

  public suspend fun delete(id: LibraryBookId): LibraryBookModel {
    val model = get(id)
    store.delete(id)
    return model
  }
}
