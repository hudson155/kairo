package io.limberapp.backend.module.orgs.store.org

import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.framework.store.Store

internal interface OrgStore : Store<OrgModel.Complete, OrgModel.Update>
