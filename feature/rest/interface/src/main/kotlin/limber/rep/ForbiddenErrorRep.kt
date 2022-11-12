package limber.rep

public data class ForbiddenErrorRep(val userMessage: String) : ErrorRep() {
  override val message: String = "Forbidden: $userMessage"
}
