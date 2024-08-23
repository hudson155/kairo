package kairo.restFeature

/**
 * This API is for [BrokenMethodRestEndpointTemplateTest]
 * which tests cases where the [RestEndpoint.Method] annotation
 * is used in unexpected or unsupported ways.
 */
internal object BrokenMethodLibraryBookApi {
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.Accept("application/json")
  internal data object MissingMethod : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.Accept("application/json")
  internal data object EmptyMethod : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("SYNC")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class Sync(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()
}
