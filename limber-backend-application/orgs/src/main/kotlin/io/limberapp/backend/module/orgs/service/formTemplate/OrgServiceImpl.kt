package io.limberapp.backend.module.orgs.service.formTemplate

import com.google.inject.Inject
import io.ktor.features.NotFoundException
import io.limberapp.backend.module.orgs.model.formTemplate.Org
import io.limberapp.backend.module.orgs.store.formTemplate.OrgStore
import io.limberapp.framework.store.create
import io.limberapp.framework.store.getById
import io.limberapp.framework.store.update
import java.util.UUID

internal class OrgServiceImpl @Inject constructor(
    private val orgStore: OrgStore
) : OrgService {

    override fun create(model: Org) = orgStore.create(model)

    override fun getById(id: UUID) = orgStore.getById(id)

    override fun update(id: UUID, updater: Org.Updater): Org {
        return orgStore.update(id, updater)
    }
}
