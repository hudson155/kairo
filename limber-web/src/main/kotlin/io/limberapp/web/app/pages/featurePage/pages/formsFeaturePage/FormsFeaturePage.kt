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
        // TODO (263) : Once we have access to the hooks, we can fix the routing here see:
        //  https://reacttraining.com/react-router/web/example/nesting
        sideNav {
            sideNavLink("/forms", "Home")
            sideNavLink("/forms/templates", "Templates")
            sideNavLink("/forms/instances", "Instances")
        }
        styledDiv {
            switch {
                route(path = "/forms", exact = true) { styledH1 { +"Welcome to Forms" } }
                route(path = "/forms/instances", exact = true) { buildElement { formInstancesListPage() } }
                route(path = "/forms/templates", exact = true) { buildElement { formTemplatesListPage() } }
            }
        }
    }
}

internal fun RBuilder.formsFeaturePage() {
    child(formsFeaturePage)
}
