package io.limberapp.backend.module.orgs.mapper

import io.limberapp.backend.module.orgs.model.org.OrgModel
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import java.time.LocalDateTime
import java.util.UUID

internal object OrgMapper {

    fun creationModel(rep: OrgRep.Creation) = OrgModel.Creation(
        name = rep.name
    )

    fun completeModel(model: OrgModel.Creation) =
        OrgModel.Complete(
            id = UUID.randomUUID(),
            created = LocalDateTime.now(),
            version = 0,
            name = model.name
        )

    fun updateModel(rep: OrgRep.Update) = OrgModel.Update(
        name = rep.name
    )

    fun completeRep(model: OrgModel.Complete) = OrgRep.Complete(
        id = model.id,
        created = model.created,
        version = model.version,
        name = model.name
    )
}
