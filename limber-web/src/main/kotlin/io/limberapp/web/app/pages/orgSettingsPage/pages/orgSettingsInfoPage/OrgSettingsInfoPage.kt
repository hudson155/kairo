package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import react.*

internal fun RBuilder.orgSettingsInfoPage() {
  child(component)
}

internal typealias Props = RProps

internal object OrgSettingsInfoPage {
  const val name = "Organization info"
  const val path = "${OrgSettingsPage.path}/info"
}

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  layoutTitle(OrgSettingsInfoPage.name)
}
