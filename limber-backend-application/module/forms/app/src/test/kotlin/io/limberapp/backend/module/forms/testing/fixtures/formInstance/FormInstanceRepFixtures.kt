package io.limberapp.backend.module.forms.testing.fixtures.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object FormInstanceRepFixtures {

    data class Fixture(
        val creation: (orgId: UUID, formTemplateId: UUID) -> FormInstanceRep.Creation,
        val complete: ResourceTest.(orgId: UUID, formTemplateId: UUID, idSeed: Int) -> FormInstanceRep.Complete
    )

    val fixture = Fixture({ orgId, formTemplateId ->
        FormInstanceRep.Creation(
            orgId = orgId,
            formTemplateId = formTemplateId
        )
    }, { orgId, formTemplateId, idSeed ->
        FormInstanceRep.Complete(
            id = deterministicUuidGenerator[idSeed],
            created = LocalDateTime.now(fixedClock),
            orgId = orgId,
            formTemplateId = formTemplateId,
            questions = emptyList()
        )
    })
}
