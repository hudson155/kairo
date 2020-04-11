package io.limberapp.web.app

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.footer.footer
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.app.components.page.page
import io.limberapp.web.app.pages.featurePage.featurePage
import io.limberapp.web.app.pages.notFoundPage.notFoundPage
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch
import react.useState
import kotlin.js.Date

private val mainApp = functionalComponent<RProps> {

    val (features, setFeatures) = useState(
        listOf(
            FeatureRep.Complete(
                id = "75a2ed7a-4247-4e63-ab10-a60df3d9aeee",
                created = Date().toISOString(),
                name = "Home",
                path = "/home",
                type = FeatureRep.Type.HOME,
                isDefaultFeature = true
            ),
            FeatureRep.Complete(
                id = "3dc95c5d-767c-4b29-9c50-a6f93edd0c06",
                created = Date().toISOString(),
                name = "Forms",
                path = "/forms",
                type = FeatureRep.Type.FORMS,
                isDefaultFeature = false
            )
        )
    )

    page(header = buildElement { navbar(features, "Firstname Lastname") }, footer = buildElement { footer() }) {
        switch {
            features.default?.let { route(path = "/", exact = true) { redirect(from = "/", to = it.path) } }
            features.map { feature ->
                route(path = feature.path, exact = true) { buildElement { featurePage(feature) } }
            }
            route(path = "/") { buildElement { notFoundPage() } }
        }
    }
}

internal fun RBuilder.mainApp() {
    child(mainApp)
}

internal val List<FeatureRep.Complete>.default get() = singleOrNull { it.isDefaultFeature }
