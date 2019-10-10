package io.limberapp.backend.module.orgs.service.formTemplate

import io.limberapp.backend.module.orgs.model.formTemplate.Org
import java.util.UUID

internal interface OrgService {

    fun create(model: Org): Org

    fun getById(id: UUID): Org?

    fun update(id: UUID, updater: Org.Updater): Org
}
