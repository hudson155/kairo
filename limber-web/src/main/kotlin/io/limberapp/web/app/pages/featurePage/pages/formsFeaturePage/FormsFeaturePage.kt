package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage

import io.limberapp.web.app.components.sideNav.sideNav
import io.limberapp.web.app.components.sideNav.sideNavItem.sideNavItem
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.formInstancesListPage.formInstancesListPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.formTemplatesListPage.formTemplatesListPage
import kotlinx.css.Display
import kotlinx.css.display
import kotlinx.css.flexGrow
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.dom.div
import react.dom.h1
import react.functionalComponent
import react.router.dom.route
import react.router.dom.switch
import styled.StyleSheet
import styled.css
import styled.styledDiv

/**
 * Parent page for forms module pages.
 */
internal fun RBuilder.formsFeaturePage() {
    child(formsFeaturePage)
}

private object Styles : StyleSheet("FormPageStyles", isStatic = true) {
    val root by css {
        display = Display.flex
        flexGrow = 1.0
    }
}

private val formsFeaturePage = functionalComponent<RProps> {
    styledDiv {
        css { +Styles.root }
        sideNav {
            sideNavItem("/", "Home")
            sideNavItem("/templates", "Templates")
            sideNavItem("/instances", "Instances")
        }
        div {
            switch {
                route(path = "/", exact = true) { h1 { +"Welcome to Forms" } }
                route(path = "/instances", exact = true) { buildElement { formInstancesListPage() } }
                route(path = "/templates", exact = true) { buildElement { formTemplatesListPage() } }
            }
        }
    }
}
