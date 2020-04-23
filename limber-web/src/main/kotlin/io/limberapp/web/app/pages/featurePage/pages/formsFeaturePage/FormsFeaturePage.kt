package io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage

import io.limberapp.web.app.components.sideNav.sideNav
import io.limberapp.web.app.components.sideNav.sideNavLink.sideNavLink
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.formInstancesListPage.formInstancesListPage
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.formTemplatesListPage.formTemplatesListPage
import kotlinx.css.Display
import kotlinx.css.display
import kotlinx.css.flexGrow
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.route
import react.router.dom.switch
import styled.StyleSheet
import styled.css
import styled.styledDiv
import styled.styledH1

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
            sideNavLink("/", "Home")
            sideNavLink("/templates", "Templates")
            sideNavLink("/instances", "Instances")
        }
        styledDiv {
            switch {
                route(path = "/", exact = true) { styledH1 { +"Welcome to Forms" } }
                route(path = "/instances", exact = true) { buildElement { formInstancesListPage() } }
                route(path = "/templates", exact = true) { buildElement { formTemplatesListPage() } }
            }
        }
    }
}

internal fun RBuilder.formsFeaturePage() {
    child(formsFeaturePage)
}
