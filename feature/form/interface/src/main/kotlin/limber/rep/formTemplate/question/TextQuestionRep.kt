package limber.rep.formTemplate.question

import limber.validation.FormTemplateQuestionLabelValidator

public data class TextQuestionRep(
  override val id: String,
  override val required: Required,
  @FormTemplateQuestionLabelValidator override val label: String,
  val size: Size,
) : FormTemplateQuestionRep() {
  public enum class Size { Small, Large }
}
