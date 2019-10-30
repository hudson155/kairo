package io.limberapp.backend.module.orgs.mapper.module

import io.limberapp.backend.module.orgs.model.module.ModuleModel
import io.limberapp.backend.module.orgs.rep.module.ModuleRep
import java.time.LocalDateTime
import java.util.UUID

internal object ModuleMapper {

    fun creationModel(rep: ModuleRep.Creation) = ModuleModel.Creation(
        name = rep.name,
        type = when (rep.type) {
            ModuleRep.Type.FORM -> ModuleModel.Type.FORM
        }
    )

    fun completeModel(model: ModuleModel.Creation) = ModuleModel.Complete(
        id = UUID.randomUUID(),
        created = LocalDateTime.now(),
        version = 0,
        name = model.name,
        type = model.type
    )

    fun updateModel(rep: ModuleRep.Update) = ModuleModel.Update(
        name = rep.name
    )

    fun completeRep(model: ModuleModel.Complete) = ModuleRep.Complete(
        id = model.id,
        created = model.created,
        version = model.version,
        name = model.name,
        type = when (model.type) {
            ModuleModel.Type.FORM -> ModuleRep.Type.FORM
        }
    )
}
