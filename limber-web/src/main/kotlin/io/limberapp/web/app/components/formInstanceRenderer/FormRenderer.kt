package io.limberapp.web.app.components.formInstanceRenderer

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.web.app.components.formInstanceRenderer.components.formQuestion.formQuestion
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.js.onSubmitFunction
import react.*
import react.dom.*

internal fun RBuilder.formRenderer(
  formTemplateQuestions: List<FormTemplateQuestionRep.Complete>,
  formInstance: FormInstanceRep.Complete,
) {
  child(component, Props(formTemplateQuestions, formInstance))
}

internal data class Props(
  val formTemplateQuestions: List<FormTemplateQuestionRep.Complete>,
  val formInstance: FormInstanceRep.Complete,
) : RProps

private class S : Styles("FromRenderer") {
  val root by css {
    backgroundColor = Theme.Color.Background.white
    border(1.px, BorderStyle.solid, Theme.Color.Border.light)
    borderRadius = Theme.Sizing.borderRadius
    padding(20.px)
  }
  val footer by css {
    display = Display.flex
    flexDirection = FlexDirection.rowReverse
    paddingTop = 12.px
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val instanceQuestions = props.formInstance.questions.associateBy { it.questionGuid }

  form(classes = s.c { it::root }) {
    attrs {
      onSubmitFunction = { e -> e.preventDefault() } // disable enter key triggering submit
    }
    props.formTemplateQuestions.forEach {
      formQuestion(it, props.formInstance, instanceQuestions[it.guid])
    }
    div(classes = s.c { it::footer }) {
      limberButton(
        style = Style.PRIMARY,
        onClick = { /* TODO (ENG-26): Mark form instance submitted */ }
      ) { +"Submit form" }
    }
  }
}
