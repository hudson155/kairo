package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import com.piperframework.module.annotation.Store

internal class OrgServiceImpl @Inject constructor(
    @Store private val orgStore: OrgService
) : OrgService by orgStore
