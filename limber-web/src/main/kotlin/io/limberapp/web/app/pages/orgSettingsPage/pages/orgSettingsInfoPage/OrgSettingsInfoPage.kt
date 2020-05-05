package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsInfoPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Page for managing organization metadata.
 */
internal fun RBuilder.orgSettingsInfoPage() {
    child(orgSettingsInfoPage)
}

internal const val ORG_SETTINGS_INFO_PAGE_NAME = "Organization info"

private val orgSettingsInfoPage = functionalComponent<RProps> {
    layoutTitle(ORG_SETTINGS_INFO_PAGE_NAME)
}
