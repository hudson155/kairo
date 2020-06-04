package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.util.component
import react.*

/**
 * Page for managing organization metadata.
 */
internal fun RBuilder.orgSettingsInfoPage() {
  child(component)
}

internal object OrgSettingsInfoPage {
  const val name = "Organization info"
  const val path = "${OrgSettingsPage.path}/info"
}

private val component = component<RProps> component@{ _ ->
  layoutTitle(OrgSettingsInfoPage.name)
}
