package io.limberapp.web.app.components.formInstanceRenderer.components.formRadioQuestion

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionRep
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import react.*
import react.dom.*

internal fun RBuilder.fromRadioQuestion(
  question: FormTemplateRadioSelectorQuestionRep.Complete,
  onSubmit: (FormInstanceQuestionRep.Creation) -> Unit,
) {
  child(component, Props(question, onSubmit))
}

private data class Props(
  val question: FormTemplateRadioSelectorQuestionRep.Complete,
  val onSubmit: (FormInstanceQuestionRep.Creation) -> Unit,
) : RProps

private class S : Styles("FormRadioQuestion") {
  val radioOption by css {
    display = Display.inlineBlock
    padding(6.px)
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val (answer, setAnswer) = useState("")
  props.question.options.forEach { option ->
    span(s.c { it::radioOption }) {
      label {
        input {
          attrs {
            type = InputType.radio
            name = props.question.guid
            // TODO (ENG-26): figure out why react thinks this is an uncontrolled component
            checked = answer == option
            onChangeFunction = {
              setAnswer(option)
              props.onSubmit(FormInstanceRadioSelectorQuestionRep.Creation(selection = option))
            }
          }
        }
        +option
      }
    }
  }
}
