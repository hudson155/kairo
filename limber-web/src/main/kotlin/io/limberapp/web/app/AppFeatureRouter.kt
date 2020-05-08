package io.limberapp.web.app

import io.limberapp.backend.module.orgs.rep.org.default
import io.limberapp.web.app.components.basicNavbar.basicNavbar
import io.limberapp.web.app.components.footer.footer
import io.limberapp.web.app.components.mainAppNavbar.mainAppNavbar
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.page.page
import io.limberapp.web.app.pages.featurePage.featurePage
import io.limberapp.web.app.pages.loadingPage.loadingPage
import io.limberapp.web.app.pages.notFoundPage.notFoundPage
import io.limberapp.web.app.pages.orgSettingsPage.orgSettingsPage
import io.limberapp.web.app.pages.signOutPage.signOutPage
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.auth.useAuth
import io.limberapp.web.context.globalState.action.org.ensureOrgLoaded
import io.limberapp.web.context.globalState.action.user.ensureUserLoaded
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.Strings
import io.limberapp.web.util.rootPath
import io.limberapp.web.util.withContext
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.navLink
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch

/**
 * Part of the application root.
 *   - Loads the org and user.
 *   - Handles routing for the authenticated application.
 */
internal fun RBuilder.appFeatureRouter() {
    child(component)
}

private val component = functionalComponent<RProps> {
    val api = useApi()
    val auth = useAuth()
    val global = useGlobalState()

    withContext(global, api) { ensureOrgLoaded(checkNotNull(auth.jwt).org.guid) }

    withContext(global, api) { ensureUserLoaded(checkNotNull(auth.jwt).user.guid) }

    // While the org is loading, show the loading page.
    if (!global.state.org.isLoaded) {
        page(
            header = buildElement {
                basicNavbar {
                    navLink<RProps>(to = signOutPage.path, exact = true) { headerItem { +Strings.signOut } }
                }
            },
            footer = buildElement { footer() }
        ) {
            loadingPage(Strings.loadingOrg)
        }
        return@functionalComponent
    }

    // While the user is loading, show the loading page.
    if (!global.state.user.isLoaded) {
        page(
            header = buildElement {
                basicNavbar {
                    navLink<RProps>(to = signOutPage.path, exact = true) { headerItem { +Strings.signOut } }
                }
            },
            footer = buildElement { footer() }
        ) {
            loadingPage(Strings.loadingUser)
        }
        return@functionalComponent
    }

    val features = checkNotNull(global.state.org.state).features

    page(header = buildElement { mainAppNavbar() }, footer = buildElement { footer() }) {
        switch {
            features.default?.let { route(path = rootPath, exact = true) { redirect(to = it.path) } }
            route(path = orgSettingsPage.path) { buildElement { orgSettingsPage() } }
            features.map { feature ->
                route(path = feature.path) { buildElement { featurePage(feature) } }
            }
            route(path = rootPath) { buildElement { notFoundPage() } }
        }
    }
}
