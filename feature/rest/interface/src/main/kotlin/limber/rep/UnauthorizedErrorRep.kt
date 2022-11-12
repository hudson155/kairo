package limber.rep

public data class UnauthorizedErrorRep(val userMessage: String) : ErrorRep() {
  override val message: String = "Unauthorized: $userMessage"
}
