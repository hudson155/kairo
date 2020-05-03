package io.limberapp.web.app.components.mainAppNavbar

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.navbar.components.headerLinkGroup.headerLinkGroup
import io.limberapp.web.app.components.navbar.components.headerPhoto.headerPhoto
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.app.default
import io.limberapp.web.util.buildElements
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.navLink

internal data class Props(val features: Set<FeatureRep.Complete>, val name: String?, val photoUrl: String?) : RProps

private val mainAppNavbar = functionalComponent<Props> { props ->
    navbar(
        left = buildElements {
            headerLinkGroup {
                props.features.default?.let {
                    navLink<RProps>(to = it.path) {
                        headerItem { +"Limber" }
                    }
                }
            }
        },
        right = buildElements {
            headerLinkGroup {
                props.name?.let { headerItem { +it } }
                props.photoUrl?.let { headerPhoto(it) }
                navLink<RProps>(to = "/signout") {
                    headerItem { +"Sign Out" }
                }
            }
        }
    ) {
        headerLinkGroup {
            props.features.forEach {
                navLink<RProps>(to = it.path) {
                    headerItem { +it.name }
                }
            }
        }
    }
}

internal fun RBuilder.mainAppNavbar(features: Set<FeatureRep.Complete>, name: String?, photoUrl: String?) {
    child(mainAppNavbar, Props(features, name, photoUrl))
}
