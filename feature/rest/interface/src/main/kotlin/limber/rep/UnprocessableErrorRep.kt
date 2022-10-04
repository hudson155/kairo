package limber.rep

public data class UnprocessableErrorRep(
  override val message: String,
) : ErrorRep()
