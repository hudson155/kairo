package io.limberapp.web.context.globalState.action.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.web.context.LoadableState

internal typealias OrgState = LoadableState<OrgRep.Complete>
