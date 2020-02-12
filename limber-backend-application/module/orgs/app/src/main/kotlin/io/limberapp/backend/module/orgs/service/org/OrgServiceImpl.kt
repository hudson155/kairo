package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.store.org.OrgStore

internal class OrgServiceImpl @Inject constructor(
    orgStore: OrgStore
) : OrgService by orgStore
