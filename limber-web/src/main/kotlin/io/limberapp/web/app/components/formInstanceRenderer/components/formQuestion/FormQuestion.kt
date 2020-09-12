package io.limberapp.web.app.components.formInstanceRenderer.components.formQuestion

import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.api.formInstance.question.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.web.api.useApi
import io.limberapp.web.app.components.formInstanceRenderer.components.formDateQuestion.formDateQuestion
import io.limberapp.web.app.components.formInstanceRenderer.components.formRadioQuestion.fromRadioQuestion
import io.limberapp.web.app.components.formInstanceRenderer.components.formTextQuestion.fromTextQuestion
import io.limberapp.web.state.state.feature.useFeatureState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.async
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

internal fun RBuilder.formQuestion(
  question: FormTemplateQuestionRep.Complete,
  formInstance: FormInstanceRep.Complete,
  onAnswerQuestion: (FormInstanceQuestionRep.Complete) -> Unit,
  onClearQuestion: (UUID) -> Unit,
  defaultValue: FormInstanceQuestionRep.Complete? = null,
) {
  child(component, Props(question, formInstance, onAnswerQuestion, onClearQuestion, defaultValue))
}

private data class Props(
  val question: FormTemplateQuestionRep.Complete,
  val formInstance: FormInstanceRep.Complete,
  val onAnswerSuccess: (FormInstanceQuestionRep.Complete) -> Unit,
  val onAnswerFailure: (UUID) -> Unit,
  val defaultValue: FormInstanceQuestionRep.Complete?,
) : RProps

private class S : Styles("FromQuestion") {
  val root by css {
    padding(20.px)
    lastChild {
      borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
    }.not()
    // Transparent border is used to stop div from janking over when error css is applied.
    borderLeft(5.px, BorderStyle.solid, Color.transparent)
  }
  val error by css {
    borderColor = Theme.Color.Indicator.error
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val api = useApi()

  val (feature, _) = useFeatureState();

  val (hasValidationError, setHasValidationError) = useState(false);

  val submitAnswer = { rep: FormInstanceQuestionRep.Creation ->
    async {
      api(
        endpoint = FormInstanceQuestionApi.Put(
          featureGuid = feature.guid,
          formInstanceGuid = props.formInstance.guid,
          questionGuid = props.question.guid,
          rep = rep
        )
      ).fold(
        successAction = { props.onAnswerSuccess(it) },
        failureAction = { props.onAnswerFailure(props.question.guid) },
      )
    }
  }

  val hasValidationErrorCallback = { hasError: Boolean ->
    if (hasError) {
      setHasValidationError(true)
      props.onAnswerFailure(props.question.guid)
    } else {
      setHasValidationError(false)
    }
  }

  div(classes = cls(s.c { it::root }, s.c(hasValidationError) { it::error })) {
    label {
      if (props.question.required) +"* "
      +props.question.label
    }
    br {}
    when (props.question) {
      is FormTemplateDateQuestionRep.Complete -> formDateQuestion(
        question = props.question,
        onSubmit = submitAnswer,
        hasValidationError = hasValidationErrorCallback,
        defaultValue = props.defaultValue as FormInstanceDateQuestionRep.Complete?,
      )
      is FormTemplateRadioSelectorQuestionRep.Complete -> fromRadioQuestion(
        question = props.question,
        onSubmit = submitAnswer,
        defaultValue = props.defaultValue as FormInstanceRadioSelectorQuestionRep.Complete?,
      )
      is FormTemplateTextQuestionRep.Complete -> fromTextQuestion(
        question = props.question,
        onSubmit = submitAnswer,
        hasValidationError = hasValidationErrorCallback,
        defaultValue = props.defaultValue as FormInstanceTextQuestionRep.Complete?,
      )
    }
  }
}
