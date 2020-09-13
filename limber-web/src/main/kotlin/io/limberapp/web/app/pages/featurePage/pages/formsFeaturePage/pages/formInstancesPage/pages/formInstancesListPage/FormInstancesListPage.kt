package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstancesListPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstanceViewPage.FormInstanceViewPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstancesListPage.components.formInstancesTable.formInstancesTable
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.pages.formInstancesPage.pages.formInstancesListPage.components.formTemplateSelectorModal.formTemplateSelectorModal
import io.limberapp.web.state.state.feature.useFeatureState
import io.limberapp.web.state.state.formInstances.useFormInstancesState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import react.*
import react.dom.*
import react.router.dom.*

internal fun RBuilder.formInstancesListPage() {
  child(component)
}

internal typealias Props = RProps

private class S : Styles("FormInstancesListPage") {
  val actionRow by css {
    display = Display.flex
    flexDirection = FlexDirection.rowReverse
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val history = useHistory()

  val (feature, _) = useFeatureState()
  val (formInstances, _) = useFormInstancesState()

  val (isTemplateSelectorModalOpen, setIsTemplateSelectorModalOpen) = useState(false)

  layoutTitle(feature.name)

  div(classes = s.c { it::actionRow }) {
    limberButton(
      style = Style.PRIMARY,
      onClick = { setIsTemplateSelectorModalOpen(true) }
    ) { +"Fill out new form" }
  }
  formInstancesTable(
    formInstances = formInstances.values.toSet(),
    onRowClick = { formInstanceGuid -> history.push(FormInstanceViewPage.path(feature.path, formInstanceGuid)) }
  )

  if (isTemplateSelectorModalOpen) {
    formTemplateSelectorModal(
      onClose = { setIsTemplateSelectorModalOpen(false) },
      onFormInstanceCreate = { formTemplateGuid ->
        history.push("${history.location.pathname}/${formTemplateGuid}/edit")
      }
    )
  }
}
