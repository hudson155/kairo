package io.limberapp.backend.module.orgs.model.org

import io.limberapp.framework.model.CreationSubmodel
import java.time.LocalDateTime
import java.util.UUID

data class MembershipModel(
    override val created: LocalDateTime,
    val userId: UUID
) : CreationSubmodel()
