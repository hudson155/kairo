package limber.rep.formTemplate.question

import limber.validation.FormTemplateQuestionLabelValidator
import java.util.UUID

public data class BooleanQuestionRep(
  override val guid: UUID,
  override val required: Required,
  @FormTemplateQuestionLabelValidator override val label: String,
  val default: Boolean?,
) : FormTemplateQuestionRep()
