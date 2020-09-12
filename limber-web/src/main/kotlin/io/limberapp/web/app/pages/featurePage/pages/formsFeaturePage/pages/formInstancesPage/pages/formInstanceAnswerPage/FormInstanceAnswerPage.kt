package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstanceAnswerPage

import com.piperframework.types.UUID
import com.piperframework.util.Outcome
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.web.api.load
import io.limberapp.web.api.useApi
import io.limberapp.web.app.components.formInstanceRenderer.formRenderer
import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.state.state.feature.useFeatureState
import io.limberapp.web.util.Page
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import react.*
import react.dom.*
import react.router.dom.*

internal fun RBuilder.formInstanceAnswerPage() {
  child(component)
}

internal typealias Props = RProps

private data class FormAnswerProps(
  val formTemplate: FormTemplateRep.Complete,
  val formInstance: FormInstanceRep.Complete,
) : RProps

internal object FormInstanceAnswerPage : Page {
  internal data class PageParams(val formInstanceGuid: UUID) : RProps

  // TODO ENG-43: Potentially convert this to an absolute path function
  val subpath = "/:${PageParams::formInstanceGuid.name}/edit"
}

private class S : Styles("FormInstanceAnswerPage") {
  val footer by css {
    display = Display.flex
    flexDirection = FlexDirection.rowReverse
    paddingTop = 12.px
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val api = useApi()
  val match = checkNotNull(useRouteMatch<FormInstanceAnswerPage.PageParams>())

  val (feature, _) = useFeatureState()

  val formInstanceGuid = match.params.formInstanceGuid

  val form = load {
    api(FormInstanceApi.Get(feature.guid, formInstanceGuid))
      .map { formInstance ->
        api(FormTemplateApi.Get(feature.guid, formInstance.formTemplateGuid))
          .map { formInstanceTemplate -> Outcome.Success(Pair(formInstance, formInstanceTemplate)) }
      }
  } ?: return loadingSpinner()

  form.onFailure { return failedToLoad("form") }
  val (formInstance, formTemplate) = form.value

  child(functionalComponent(RBuilder::formAnswerComponent), FormAnswerProps(formTemplate, formInstance))
}

private fun RBuilder.formAnswerComponent(props: FormAnswerProps) {
  val (answeredQuestions, setAnsweredQuestions) = useState(emptySet<UUID>())
  val (requiredQuestions, setRequiredQuestions) = useState(emptySet<UUID>())

  val submittedDateTime = props.formInstance.submittedDate;

  useEffect(listOf(props.formTemplate.guid)) {
    setRequiredQuestions(
      props.formTemplate.questions
        .filter { it.required }
        .map { it.guid }
        .toSet()
    )
  }

  useEffect(listOf(props.formInstance.guid)) {
    setAnsweredQuestions(
      props.formInstance.questions
        .mapNotNull { it.questionGuid }
        .toSet()
    )
  }

  val onAnswerSuccess = { question: FormInstanceQuestionRep.Complete ->
    setAnsweredQuestions(answeredQuestions + checkNotNull(question.questionGuid))
  }

  val onAnswerFailure = { questionGuid: UUID ->
    setAnsweredQuestions(answeredQuestions - questionGuid)
  }

  layoutTitle(props.formTemplate.title)
  formRenderer(props.formTemplate.questions, props.formInstance, onAnswerSuccess, onAnswerFailure)
  div(classes = s.c { it::footer }) {
    limberButton(
      style = Style.PRIMARY,
      disabled = !answeredQuestions.containsAll(requiredQuestions) || props.formInstance.submittedDate != null,
      onClick = { /* TODO (ENG-26): Mark form instance submitted */ },
    ) { +"Submit form" }
  }
  if (submittedDateTime != null) {
    div(classes = s.c { it::footer }) {
      +"Submitted at: "
      +submittedDateTime.toDateString()
    }
  }
}
