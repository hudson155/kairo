package io.limberapp.web.app.pages.featurePage.pages.formPage

import io.limberapp.web.app.components.sideNav.SideNavLink
import io.limberapp.web.app.components.sideNav.sideNav
import io.limberapp.web.app.pages.featurePage.pages.formPage.formInstancesListPage.formInstancesListPage
import io.limberapp.web.app.pages.featurePage.pages.formPage.formTemplatesListPage.formTemplatesListPage
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

private val formPage = functionalComponent<RProps> {
    styledDiv {
        css { +Styles.root }
        // TODO (263) : Once we have access to the hooks, we can fix the routing here see:
        //  https://reacttraining.com/react-router/web/example/nesting
        sideNav(
            links = listOf(
                SideNavLink("/forms", "Home"),
                SideNavLink("/forms/templates", "Templates"),
                SideNavLink("/forms/instances", "Instances")

            )
        )
        styledDiv {
            switch {
                route(path = "/forms", exact = true) { styledH1 { +"Welcome to Forms" } }
                route(path = "/forms/instances") { buildElement { formInstancesListPage() } }
                route(path = "/forms/templates") { buildElement { formTemplatesListPage() } }
            }
        }
    }
}

internal fun RBuilder.formPage() {
    child(formPage)
}
