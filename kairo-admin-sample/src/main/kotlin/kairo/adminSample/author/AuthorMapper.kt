package kairo.adminSample.author

public class AuthorMapper {
  public fun toRep(model: AuthorModel): AuthorRep =
    AuthorRep(
      id = model.id.value,
      createdAt = model.createdAt,
      updatedAt = model.updatedAt,
      name = model.name,
      bio = model.bio,
    )

  public fun toModelCreator(rep: AuthorRep.Creator): AuthorModel.Creator =
    AuthorModel.Creator(
      name = rep.name,
      bio = rep.bio,
    )

  public fun toModelUpdate(rep: AuthorRep.Update): AuthorModel.Update =
    AuthorModel.Update(
      name = rep.name,
      bio = rep.bio,
    )
}
