package io.limberapp.web.app

import io.limberapp.backend.module.orgs.rep.org.default
import io.limberapp.web.app.components.footer.footer
import io.limberapp.web.app.components.mainAppNavbar.mainAppNavbar
import io.limberapp.web.app.components.minimalPage.LinkType
import io.limberapp.web.app.components.minimalPage.minimalPage
import io.limberapp.web.app.components.page.page
import io.limberapp.web.app.pages.failedToLoadPage.failedToLoadPage
import io.limberapp.web.app.pages.featurePage.featurePage
import io.limberapp.web.app.pages.loadingPage.loadingPage
import io.limberapp.web.app.pages.notFoundPage.notFoundPage
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.orgSettingsPage.orgSettingsPage
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.auth.useAuth
import io.limberapp.web.context.globalState.action.org.loadOrg
import io.limberapp.web.context.globalState.action.org.state
import io.limberapp.web.context.globalState.action.user.loadUser
import io.limberapp.web.util.componentWithApi
import io.limberapp.web.util.rootPath
import react.*
import react.router.dom.*

/**
 * Part of the application root.
 *   - Loads the org and user.
 *   - Handles routing for the authenticated application.
 */
internal fun RBuilder.appFeatureRouter() {
  child(component)
}

private val component = componentWithApi<RProps> component@{ self, _ ->
  val auth = useAuth()

  self.loadOrg(checkNotNull(auth.jwt).org.guid)
  self.loadUser(checkNotNull(auth.jwt).user.guid)

  // While the org is loading, show the loading page.
  self.gs.org.let { loadableState ->
    when (loadableState) {
      is LoadableState.Unloaded -> return@component minimalPage(linkType = null) { loadingPage("Loading org...") }
      is LoadableState.Error -> return@component minimalPage(linkType = LinkType.SIGN_OUT) { failedToLoadPage("org") }
      is LoadableState.Loaded -> Unit
    }
  }

  // While the user is loading, show the loading page.
  self.gs.user.let { loadableState ->
    when (loadableState) {
      is LoadableState.Unloaded -> return@component minimalPage(linkType = null) { loadingPage("Loading user...") }
      is LoadableState.Error -> return@component minimalPage(linkType = LinkType.SIGN_OUT) { failedToLoadPage("user") }
      is LoadableState.Loaded -> Unit
    }
  }

  val features = self.gs.org.state.features

  page(header = buildElement { mainAppNavbar() }, footer = buildElement { footer() }) {
    switch {
      features.default?.let { route(path = rootPath, exact = true) { redirect(to = it.path) } }
      route(path = OrgSettingsPage.path) { buildElement { orgSettingsPage() } }
      features.forEach { feature ->
        route(path = feature.path) { buildElement { featurePage(feature) } }
      }
      route(path = rootPath) { buildElement { notFoundPage() } }
    }
  }
}
