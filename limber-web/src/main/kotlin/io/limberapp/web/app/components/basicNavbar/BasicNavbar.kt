package io.limberapp.web.app.components.basicNavbar

import io.limberapp.web.app.components.navbar.components.headerGroup.headerGroup
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.state.state.tenant.useTenantState
import io.limberapp.web.util.buildElements
import react.*

/**
 * Top-of-page nav for use in an unauthenticated or partially loaded state. There's no way to have a main section to
 * this nav implementation.
 *
 * [children] should be [headerItem]s for the right section of the nav.
 */
internal fun RBuilder.basicNavbar(children: RHandler<Props> = {}) {
  child(component, handler = children)
}

internal typealias Props = RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  // The type system is janked because this might be null if we're outside a provider,
  // but it doesn't appear that way to the type system.
  // Not gonna fix it because this code is condemned.
  val tenant = useTenantState()?.first

  navbar(
    left = buildElements { headerGroup { headerItem { tenant?.name?.let { +it } } } },
    right = buildElements { headerGroup { props.children() } }
  ) {}
}
