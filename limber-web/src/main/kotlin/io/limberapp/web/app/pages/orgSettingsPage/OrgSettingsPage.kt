package io.limberapp.web.app.pages.orgSettingsPage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.context.globalState.useGlobalState
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Page for managing organization-level settings.
 */
internal fun RBuilder.orgSettingsPage() {
    child(orgSettingsPage)
}

private val orgSettingsPage = functionalComponent<RProps> {
    val global = useGlobalState()

    val orgName = checkNotNull(global.state.org.state).name

    standardLayout(leftPane = null) {
        layoutTitle("$orgName Settings")
    }
}
