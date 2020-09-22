package io.limberapp.web.app.components.formAnswerer.components.formYesNoAnswerQuestion

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceYesNoQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateYesNoQuestionRep
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import react.*
import react.dom.*

internal fun RBuilder.formYesNoAnswerQuestion(
  question: FormTemplateYesNoQuestionRep.Complete,
  onSubmit: (FormInstanceQuestionRep.Creation) -> Unit,
) {
  child(component, Props(question, onSubmit))
}

private data class Props(
  val question: FormTemplateYesNoQuestionRep.Complete,
  val onSubmit: (FormInstanceQuestionRep.Creation) -> Unit,
) : RProps

private class S : Styles("FormYesNoQuestion") {
  val radioOption by css {
    display = Display.inlineBlock
    padding(6.px)
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val (answer, setAnswer) = useState("")
  listOf("Yes" to true, "No" to false).forEach { (option, yes) ->
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
              props.onSubmit(FormInstanceYesNoQuestionRep.Creation(yes = yes))
            }
          }
        }
        +option
      }
    }
  }
}
