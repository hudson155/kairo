package io.limberapp.web.app.components.formRenderer.components.formRendererQuestion

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*
import kotlin.js.Date

internal fun RBuilder.formRendererQuestion(
  formTemplateQuestion: FormTemplateQuestionRep.Complete,
  formInstanceQuestion: FormInstanceQuestionRep.Complete?,
) {
  child(component, Props(formTemplateQuestion, formInstanceQuestion))
}

internal data class Props(
  val formTemplateQuestion: FormTemplateQuestionRep.Complete,
  val formInstanceQuestion: FormInstanceQuestionRep.Complete?,
) : RProps

private class S : Styles("FormRendererQuestion") {
  val root by css {
    padding(20.px)
    lastChild {
      borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
    }.not()
  }
  val label by css {
    color = Theme.Color.Text.faded
    marginBottom = 8.px
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  div(classes = s.c { it::root }) {
    small {
      label(classes = s.c { it::label }) {
        if (props.formTemplateQuestion.required) +"* "
        +props.formTemplateQuestion.label
      }
    }
    br {}
    if (props.formInstanceQuestion == null) {
      div { +"(Not Answered)" }
    } else {
      when (props.formTemplateQuestion) {
        is FormTemplateDateQuestionRep.Complete -> {
          val formInstanceQuestion = props.formInstanceQuestion as FormInstanceDateQuestionRep.Complete
          div { +Date(formInstanceQuestion.date).toDateString() }
        }
        is FormTemplateRadioSelectorQuestionRep.Complete -> {
          val formInstanceQuestion = props.formInstanceQuestion as FormInstanceRadioSelectorQuestionRep.Complete
          div { +formInstanceQuestion.selection }
        }
        is FormTemplateTextQuestionRep.Complete -> {
          val formInstanceQuestion = props.formInstanceQuestion as FormInstanceTextQuestionRep.Complete
          div { +formInstanceQuestion.text }
        }
      }
    }
  }
}
