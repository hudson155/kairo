package io.limberapp.web.app.components.formInstanceRenderer.components.formTextQuestion

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.web.util.targetValue
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import react.*
import react.dom.*

internal fun RBuilder.fromTextQuestion(
  formTemplateQuestion: FormTemplateTextQuestionRep.Complete,
  onSubmit: (FormInstanceQuestionRep.Creation) -> Unit,
) {
  child(component, Props(formTemplateQuestion, onSubmit))
}

private data class Props(
  val formTemplateQuestion: FormTemplateTextQuestionRep.Complete,
  val onSubmit: (FormInstanceQuestionRep.Creation) -> Unit,
) : RProps

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val (answer, setAnswer) = useState("")
  input {
    attrs {
      value = answer
      // TODO (ENG-42): Use on change to submit with debouncing
      onBlurFunction = { props.onSubmit(FormInstanceTextQuestionRep.Creation(text = answer)) }
      onChangeFunction = { e -> setAnswer(e.targetValue) }
    }
  }
}
