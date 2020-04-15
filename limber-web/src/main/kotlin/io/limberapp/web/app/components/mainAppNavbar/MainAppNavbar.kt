package io.limberapp.web.app.components.mainAppNavbar

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.navbar.components.headerLink.headerLink
import io.limberapp.web.app.components.navbar.components.headerLinkGroup.headerLinkGroup
import io.limberapp.web.app.components.navbar.components.headerText.headerText
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.app.default
import io.limberapp.web.context.auth.useAuth
import io.limberapp.web.util.buildElements
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

internal data class Props(val features: List<FeatureRep.Complete>, val name: String?) : RProps

private val mainAppNavbar = functionalComponent<Props> { props ->
    val auth = useAuth()
    navbar(
        left = buildElements {
            headerLinkGroup {
                props.features.default?.let { headerLink(to = it.path) { +"Limber" } } ?: headerText { +"Limber" }
            }
            headerLinkGroup {
                props.features.forEach { headerLink(to = it.path) { +it.name } }
            }
        },
        right = buildElements {
            headerLinkGroup {
                props.name?.let { headerText { +it } }
                if (auth.isAuthenticated) {
                    headerLink(to = "/signout") { +"Sign Out" }
                } else {
                    headerLink(to = "/signin") { +"Sign In" }
                }
            }
        }
    )
}

internal fun RBuilder.mainAppNavbar(features: List<FeatureRep.Complete>, name: String?) {
    child(mainAppNavbar, Props(features, name))
}
