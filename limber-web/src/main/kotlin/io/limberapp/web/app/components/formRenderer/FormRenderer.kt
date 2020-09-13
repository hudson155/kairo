package io.limberapp.web.app.components.formRenderer

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.web.app.components.formRenderer.components.formRendererQuestion.formRendererQuestion
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
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

private class S : Styles("FormRenderer") {
  val root by css {
    backgroundColor = Theme.Color.Background.white
    border(1.px, BorderStyle.solid, Theme.Color.Border.light)
    borderRadius = Theme.Sizing.borderRadius
    padding(20.px)
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val instanceQuestions = props.formInstance.questions.associateBy { it.questionGuid }

  div(classes = s.c { it::root }) {
    props.formTemplateQuestions.forEach { formTemplateQuestion ->
      formRendererQuestion(
        formTemplateQuestion = formTemplateQuestion,
        formInstanceQuestion = instanceQuestions[formTemplateQuestion.guid],
      )
    }
  }
}
