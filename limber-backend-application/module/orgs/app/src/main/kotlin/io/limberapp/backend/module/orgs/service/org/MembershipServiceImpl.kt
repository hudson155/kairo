package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.store.org.MembershipStore

internal class MembershipServiceImpl @Inject constructor(
    membershipStore: MembershipStore
) : MembershipService by membershipStore
