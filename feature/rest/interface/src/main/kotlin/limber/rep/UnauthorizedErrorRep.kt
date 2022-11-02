package limber.rep

public object UnauthorizedErrorRep : ErrorRep() {
  override val message: String = "Unauthorized."
}
