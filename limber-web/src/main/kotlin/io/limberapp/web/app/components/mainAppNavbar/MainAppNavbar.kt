package io.limberapp.web.app.components.mainAppNavbar

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.navbar.components.headerLink.headerLink
import io.limberapp.web.app.components.navbar.components.headerLinkGroup.headerLinkGroup
import io.limberapp.web.app.components.navbar.components.headerPhoto.headerPhoto
import io.limberapp.web.app.components.navbar.components.headerText.headerText
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.app.default
import io.limberapp.web.util.buildElements
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

internal data class Props(val features: Set<FeatureRep.Complete>, val name: String?, val photoUrl: String?) : RProps

private val mainAppNavbar = functionalComponent<Props> { props ->
    navbar(
        left = buildElements {
            headerLinkGroup {
                props.features.default?.let { headerLink(to = it.path) { +"Limber" } } ?: headerText { +"Limber" }
            }
        },
        right = buildElements {
            headerLinkGroup {
                props.name?.let { headerText { +it } }
                props.photoUrl?.let { headerPhoto(it) }
                headerLink(to = "/signout") { +"Sign Out" }
            }
        }
    ) {
        headerLinkGroup {
            props.features.forEach { headerLink(to = it.path) { +it.name } }
        }
    }
}

internal fun RBuilder.mainAppNavbar(features: Set<FeatureRep.Complete>, name: String?, photoUrl: String?) {
    child(mainAppNavbar, Props(features, name, photoUrl))
}
