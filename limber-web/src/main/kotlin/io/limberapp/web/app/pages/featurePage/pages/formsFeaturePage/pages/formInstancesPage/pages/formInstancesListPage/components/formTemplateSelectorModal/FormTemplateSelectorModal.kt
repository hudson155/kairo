package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstancesListPage.components.formTemplateSelectorModal

import io.limberapp.common.types.UUID
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.app.components.modal.components.modalTitle.modalTitle
import io.limberapp.web.app.components.modal.modal
import io.limberapp.web.state.state.feature.useFeatureState
import io.limberapp.web.state.state.formInstances.useFormInstancesState
import io.limberapp.web.state.state.formTemplates.useFormTemplatesState
import io.limberapp.web.state.state.user.useUserState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.async
import io.limberapp.web.util.c
import io.limberapp.web.util.gs
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import react.*
import react.dom.*

internal fun RBuilder.formTemplateSelectorModal(
  onClose: () -> Unit,
  onFormInstanceCreate: (UUID) -> Unit,
) {
  child(component, Props(onClose, onFormInstanceCreate))
}

internal data class Props(val onClose: () -> Unit, val onFormInstanceCreate: (UUID) -> Unit) : RProps

private class S : Styles("TemplateSelectorModal") {
  val template by css {
    display = Display.flex
    flexDirection = FlexDirection.column
    marginBottom = 24.px
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  val (feature, _) = useFeatureState()
  val (_, formInstancesMutator) = useFormInstancesState()
  val (formTemplates, _) = useFormTemplatesState()
  val (user, _) = useUserState()

  val (creatingForm, setCreatingForm) = useState(false);
  val (selectedTemplate, setSelectedTemplate) = useState<UUID?>(null)

  val createNewFormInstance = {
    setCreatingForm(true)
    async {
      val newFormInstance = formInstancesMutator.post(
        feature.guid,
        FormInstanceRep.Creation(selectedTemplate!!, user.guid)
      )
      setCreatingForm(false)
      // TODO (ENG-26): Add error toast
      props.onFormInstanceCreate(newFormInstance.value)
    }
  }

  modal(onClose = props.onClose) {
    modalTitle(
      title = "Forms",
      description = "Select a form to fill out"
    )
    div {
      // TODO (ENG-26): Replace selection with a nice table
      formTemplates.values.forEach { template ->
        span(s.c { it::template }) {
          label {
            input {
              attrs {
                type = InputType.radio
                name = template.title
                disabled = creatingForm
                // TODO (ENG-26): figure out why react thinks this is an uncontrolled component
                checked = template.guid == selectedTemplate
                onChangeFunction = { setSelectedTemplate(template.guid) }
              }
            }
            +template.title
          }
        }
      }
    }
    div(classes = gs.c { it::modalFooter }) {
      limberButton(
        style = Style.PRIMARY,
        disabled = selectedTemplate == null,
        loading = creatingForm,
        onClick = createNewFormInstance
      ) { +"Fill out form" }
      limberButton(
        style = Style.SECONDARY,
        loading = creatingForm,
        onClick = props.onClose
      ) { +"Cancel" }
    }
  }
}
