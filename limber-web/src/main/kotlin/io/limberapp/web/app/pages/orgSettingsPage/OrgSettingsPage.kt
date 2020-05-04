package io.limberapp.web.app.pages.orgSettingsPage

import io.limberapp.web.app.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.layouts.standardLayoutWithLeftPane.standardLayoutWithLeftPane
import io.limberapp.web.context.globalState.useGlobalState
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

internal fun RBuilder.orgSettingsPage() {
    child(orgSettingsPage)
}

private val orgSettingsPage = functionalComponent<RProps> {
    val global = useGlobalState()

    val orgName = checkNotNull(global.state.org.state).name

    standardLayoutWithLeftPane(
        title = "$orgName Settings",
        leftPane = null
    ) {
        layoutTitle("$orgName Settings")
    }
}
