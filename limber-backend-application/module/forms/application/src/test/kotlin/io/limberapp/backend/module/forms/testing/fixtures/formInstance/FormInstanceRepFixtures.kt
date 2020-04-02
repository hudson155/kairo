package io.limberapp.backend.module.forms.testing.fixtures.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object FormInstanceRepFixtures {

    data class Fixture(
        val creation: (featureId: UUID, formTemplateId: UUID) -> FormInstanceRep.Creation,
        val complete: ResourceTest.(featureId: UUID, formTemplateId: UUID, idSeed: Int) -> FormInstanceRep.Complete
    )

    val fixture = Fixture({ featureId, formTemplateId ->
        FormInstanceRep.Creation(
            featureId = featureId,
            formTemplateId = formTemplateId
        )
    }, { featureId, formTemplateId, idSeed ->
        FormInstanceRep.Complete(
            id = deterministicUuidGenerator[idSeed],
            created = LocalDateTime.now(fixedClock),
            featureId = featureId,
            formTemplateId = formTemplateId,
            questions = emptyList()
        )
    })
}
