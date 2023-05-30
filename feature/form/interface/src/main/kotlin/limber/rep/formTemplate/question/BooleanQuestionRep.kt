package limber.rep.formTemplate.question

import limber.validation.FormTemplateQuestionLabelValidator

public data class BooleanQuestionRep(
  override val id: String,
  override val required: Required,
  @FormTemplateQuestionLabelValidator override val label: String,
  val default: Boolean?,
) : FormTemplateQuestionRep()
