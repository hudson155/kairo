package limber.rep

public object ForbiddenErrorRep : ErrorRep() {
  override val message: String = "Forbidden."
}
