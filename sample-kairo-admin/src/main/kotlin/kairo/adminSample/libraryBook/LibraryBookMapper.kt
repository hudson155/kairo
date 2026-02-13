package kairo.adminSample.libraryBook

public class LibraryBookMapper {
  public fun toRep(model: LibraryBookModel): LibraryBookRep =
    LibraryBookRep(
      id = model.id.value,
      createdAt = model.createdAt,
      updatedAt = model.updatedAt,
      title = model.title,
      isbn = model.isbn,
      genre = model.genre,
    )

  public fun toModelCreator(rep: LibraryBookRep.Creator): LibraryBookModel.Creator =
    LibraryBookModel.Creator(
      title = rep.title,
      isbn = rep.isbn,
      genre = rep.genre,
    )

  public fun toModelUpdate(rep: LibraryBookRep.Update): LibraryBookModel.Update =
    LibraryBookModel.Update(
      title = rep.title,
      isbn = rep.isbn,
      genre = rep.genre,
    )
}
