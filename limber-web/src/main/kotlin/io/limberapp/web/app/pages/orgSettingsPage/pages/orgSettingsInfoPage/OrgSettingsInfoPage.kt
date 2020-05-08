package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.pages.orgSettingsPage.orgSettingsPage
import io.limberapp.web.util.Page
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Page for managing organization metadata.
 */
internal fun RBuilder.orgSettingsInfoPage() {
    child(component)
}

internal val orgSettingsInfoPage = Page(
    name = "Organization info",
    path = "${orgSettingsPage.path}/info"
)

private val component = functionalComponent<RProps> {
    layoutTitle(orgSettingsInfoPage.name)
}
