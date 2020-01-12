package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import com.piperframework.module.annotation.Store

internal class MembershipServiceImpl @Inject constructor(
    @Store private val membershipStore: MembershipService
) : MembershipService by membershipStore
