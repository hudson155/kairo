package io.limberapp.web.context.globalState.action.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.web.context.LoadableState

internal typealias OrgState = LoadableState<OrgRep.Complete>

/**
 * [OrgState] is loaded eagerly, so it's ok to access [state] all the time without doing a null check.
 */
internal val LoadableState<OrgRep.Complete>.state get() = checkNotNull(stateOrNull)
