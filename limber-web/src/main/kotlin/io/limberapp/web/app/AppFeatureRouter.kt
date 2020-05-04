package io.limberapp.web.app

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.rep.org.default
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.web.app.components.basicNavbar.basicNavbar
import io.limberapp.web.app.components.footer.footer
import io.limberapp.web.app.components.mainAppNavbar.mainAppNavbar
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.page.page
import io.limberapp.web.app.pages.featurePage.featurePage
import io.limberapp.web.app.pages.loadingPage.loadingPage
import io.limberapp.web.app.pages.notFoundPage.notFoundPage
import io.limberapp.web.app.pages.orgSettingsPage.orgSettingsPage
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.auth.useAuth
import io.limberapp.web.context.globalState.action.org.OrgAction
import io.limberapp.web.context.globalState.action.user.UserAction
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.async
import react.RBuilder
import react.RProps
import react.buildElement
import react.child
import react.functionalComponent
import react.router.dom.navLink
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch
import react.useEffect

/**
 * Part of the application root.
 *   - Loads the org and user.
 *   - Handles routing for the authenticated application.
 */
internal fun RBuilder.appFeatureRouter() {
    child(appFeatureRouter)
}

private val appFeatureRouter = functionalComponent<RProps> {
    val api = useApi()
    val auth = useAuth()
    val global = useGlobalState()

    // Load org asynchronously.
    val orgGuid = OrgApi.Get(checkNotNull(auth.jwt).org.guid)
    useEffect(listOf(orgGuid)) {
        if (global.state.org.hasBegunLoading) return@useEffect
        global.dispatch(OrgAction.BeginLoading)
        async {
            val org = api.orgs(orgGuid)
            global.dispatch(OrgAction.Set(org))
        }
    }

    // Load user asynchronously.
    val userGuid = UserApi.Get(checkNotNull(auth.jwt).user.guid)
    useEffect(listOf(userGuid)) {
        if (global.state.user.hasBegunLoading) return@useEffect
        global.dispatch(UserAction.BeginLoading)
        async {
            val user = api.users(userGuid)
            global.dispatch(UserAction.Set(user))
        }
    }

    // While the org is loading, show the loading page.
    if (!global.state.org.isLoaded) {
        page(
            header = buildElement {
                basicNavbar {
                    navLink<RProps>(to = "/signout", exact = true) { headerItem { +"Sign Out" } }
                }
            },
            footer = buildElement { footer() }
        ) {
            loadingPage("Loading org...")
        }
        return@functionalComponent
    }

    // While the user is loading, show the loading page.
    if (!global.state.user.isLoaded) {
        page(
            header = buildElement {
                basicNavbar {
                    navLink<RProps>(to = "/signout", exact = true) { headerItem { +"Sign Out" } }
                }
            },
            footer = buildElement { footer() }
        ) {
            loadingPage("Loading user...")
        }
        return@functionalComponent
    }

    val features = checkNotNull(global.state.org.state).features

    page(header = buildElement { mainAppNavbar() }, footer = buildElement { footer() }) {
        switch {
            features.default?.let { route(path = "/", exact = true) { redirect(from = "/", to = it.path) } }
            route(path = "/settings/org") { buildElement { orgSettingsPage() } }
            features.map { feature ->
                route(path = feature.path) { buildElement { featurePage(feature) } }
            }
            route(path = "/") { buildElement { notFoundPage() } }
        }
    }
}
