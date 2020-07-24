package io.limberapp.web.app.root.globalStateProvider

import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.users.api.user.UserApi
import io.limberapp.web.api.load
import io.limberapp.web.api.useApi
import io.limberapp.web.app.components.minimalPage.LinkType
import io.limberapp.web.app.components.minimalPage.minimalPage
import io.limberapp.web.app.pages.failedToLoadPage.failedToLoadPage
import io.limberapp.web.app.pages.loadingPage.loadingPage
import io.limberapp.web.auth.useAuth
import io.limberapp.web.state.state.orgState.orgStateProvider
import io.limberapp.web.state.state.user.userStateProvider
import io.limberapp.web.state.state.users.usersStateProvider
import react.*

/**
 * Loads and provides state that needs to be available at all times in the application.
 */
internal fun RBuilder.globalStateProvider(children: RHandler<Props>) {
  child(component, handler = children)
}

internal typealias Props = RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val api = useApi()
  val auth = useAuth()

  // Eagerly load basic entities that are required to render the app.
  val orgGuid = checkNotNull(auth.jwt).org.guid
  val userGuid = checkNotNull(auth.jwt).user.guid
  val org = load { api(OrgApi.Get(orgGuid)) }
  val user = load { api(UserApi.Get(userGuid)) }
  val users = load { api(UserApi.GetByOrgGuid(orgGuid)) }

  // While the org is loading, show the loading page.
  (org ?: return minimalPage(linkType = null) { loadingPage("Loading org...") })
    .onFailure { return minimalPage(linkType = LinkType.SIGN_OUT) { failedToLoadPage("org") } }

  // While the user is loading, show the loading page.
  (user ?: return minimalPage(linkType = null) { loadingPage("Loading user...") })
    .onFailure { return minimalPage(linkType = LinkType.SIGN_OUT) { failedToLoadPage("user") } }

  // While the users are loading, show the loading page.
  (users ?: return minimalPage(linkType = null) { loadingPage("Loading users...") })
    .onFailure { return minimalPage(linkType = LinkType.SIGN_OUT) { failedToLoadPage("users") } }

  orgStateProvider(org.value) {
    userStateProvider(user.value) {
      usersStateProvider(users.value.associateBy { it.guid }) {
        props.children()
      }
    }
  }
}
