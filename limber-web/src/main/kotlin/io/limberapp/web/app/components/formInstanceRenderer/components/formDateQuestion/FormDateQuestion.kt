package io.limberapp.web.app.components.formInstanceRenderer.components.formDateQuestion

import com.piperframework.types.LocalDate
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.web.util.targetValue
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import kotlin.js.Date

internal fun RBuilder.formDateQuestion(
  question: FormTemplateDateQuestionRep.Complete,
  onSubmit: (FormInstanceQuestionRep.Creation) -> Unit,
  hasValidationError: (Boolean) -> Unit,
  defaultValue: FormInstanceDateQuestionRep.Complete? = null,
) {
  child(component, Props(question, onSubmit, hasValidationError, defaultValue))
}

private data class Props(
  val question: FormTemplateDateQuestionRep.Complete,
  val onSubmit: (FormInstanceQuestionRep.Creation) -> Unit,
  val hasValidationError: (Boolean) -> Unit,
  val defaultValue: FormInstanceDateQuestionRep.Complete?,
) : RProps

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val (answer, setAnswer) = useState(props.defaultValue?.date ?: LocalDate())

  val onChange = { e: Event ->
    setAnswer(e.targetValue)
    if (validateQuestionInput(props.question, answer)) {
      props.hasValidationError(false)
      props.onSubmit(FormInstanceDateQuestionRep.Creation(date = e.targetValue))
    } else {
      props.hasValidationError(true)
    }
  }

  input {
    attrs {
      type = InputType.date
      value = answer
      onChangeFunction = onChange
    }
  }
}

private fun validateQuestionInput(
  question: FormTemplateDateQuestionRep.Complete,
  input: LocalDate,
): Boolean {
  val inputMS = Date(input).getTime()
  val earliestMS = question.earliest?.let { Date(it).getTime() }
  val latestMS = question.latest?.let { Date(it).getTime() }

  if (earliestMS != null && earliestMS > inputMS) return false
  if (latestMS != null && latestMS < inputMS) return false

  return true
}
