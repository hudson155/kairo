package limber.rep.formTemplate

import limber.validation.FormTemplateNameValidator

public data class FormTemplateRep(
  val id: String,
  val featureId: String,
  val current: FormTemplateVersionRep,
  val latest: FormTemplateVersionRep?,
) {
  public data class Creator(
    @FormTemplateNameValidator val name: String,
  )
}
