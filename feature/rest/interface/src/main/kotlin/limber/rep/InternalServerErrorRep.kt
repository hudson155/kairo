package limber.rep

public object InternalServerErrorRep : ErrorRep() {
  override val message: String = "Internal server error."
}
