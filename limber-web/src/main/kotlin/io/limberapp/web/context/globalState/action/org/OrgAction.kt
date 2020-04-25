package io.limberapp.web.context.globalState.action.org

import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.web.context.globalState.action.Action

internal sealed class OrgAction : Action() {
    internal object BeginLoading : OrgAction()

    internal data class Set(val org: OrgRep.Complete) : OrgAction()
}
