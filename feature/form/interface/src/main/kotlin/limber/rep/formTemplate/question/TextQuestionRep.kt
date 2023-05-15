package limber.rep.formTemplate.question

import limber.validation.FormTemplateQuestionLabelValidator
import java.util.UUID

public data class TextQuestionRep(
  override val guid: UUID,
  override val required: Required,
  @FormTemplateQuestionLabelValidator override val label: String,
  val size: Size,
) : FormTemplateQuestionRep() {
  public enum class Size { Small, Large }
}
