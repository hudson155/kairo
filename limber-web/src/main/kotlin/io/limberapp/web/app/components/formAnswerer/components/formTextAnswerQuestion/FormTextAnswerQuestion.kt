package io.limberapp.web.app.components.formAnswerer.components.formTextAnswerQuestion

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.web.util.targetValue
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.events.Event
import react.*
import react.dom.*

internal fun RBuilder.fromTextAnswerQuestion(
  question: FormTemplateTextQuestionRep.Complete,
  hasValidationError: (Boolean) -> Unit,
  onSubmit: (FormInstanceQuestionRep.Creation) -> Unit,
  defaultValue: FormInstanceTextQuestionRep.Complete? = null,
) {
  child(component, Props(question, onSubmit, hasValidationError, defaultValue))
}

private data class Props(
  val question: FormTemplateTextQuestionRep.Complete,
  val onSubmit: (FormInstanceQuestionRep.Creation) -> Unit,
  val hasValidationError: (Boolean) -> Unit,
  val defaultValue: FormInstanceTextQuestionRep.Complete?,
) : RProps

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val (answer, setAnswer) = useState(props.defaultValue?.text ?: "")

  val onBlur = { _: Event ->
    if (props.question.validator?.matches(answer) != false) {
      props.hasValidationError(false)
      // TODO (ENG-42): Use on change to submit with debouncing
      props.onSubmit(FormInstanceTextQuestionRep.Creation(text = answer))
    } else {
      props.hasValidationError(true)
    }
  }

  input {
    attrs {
      value = answer
      onBlurFunction = onBlur
      onChangeFunction = { e -> setAnswer(e.targetValue) }
    }
  }
}
