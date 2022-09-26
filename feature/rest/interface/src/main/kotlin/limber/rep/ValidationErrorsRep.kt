package limber.rep

public data class ValidationErrorsRep(
  val errors: List<ValidationError>,
) : ErrorRep() {
  public data class ValidationError(
    val propertyPath: String,
    val message: String,
  )

  override val message: String = "Validation errors."
}
