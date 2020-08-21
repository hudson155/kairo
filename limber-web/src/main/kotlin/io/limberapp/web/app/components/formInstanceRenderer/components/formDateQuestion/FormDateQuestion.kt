package io.limberapp.web.app.components.formInstanceRenderer.components.formDateQuestion

import com.piperframework.types.LocalDate
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.web.util.targetValue
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import react.*
import react.dom.*

internal fun RBuilder.formDateQuestion(
  question: FormTemplateDateQuestionRep.Complete,
  onSubmit: (FormInstanceQuestionRep.Creation) -> Unit,
  defaultValue: FormInstanceDateQuestionRep.Complete? = null,
) {
  child(component, Props(question, onSubmit, defaultValue))
}

private data class Props(
  val question: FormTemplateDateQuestionRep.Complete,
  val onSubmit: (FormInstanceQuestionRep.Creation) -> Unit,
  val defaultValue: FormInstanceDateQuestionRep.Complete?,
) : RProps

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val (answer, setAnswer) = useState(props.defaultValue?.date ?: LocalDate())
  input {
    attrs {
      type = InputType.date
      value = answer
      onChangeFunction = { e ->
        setAnswer(e.targetValue)
        props.onSubmit(FormInstanceDateQuestionRep.Creation(date = e.targetValue))
      }
    }
  }
}
