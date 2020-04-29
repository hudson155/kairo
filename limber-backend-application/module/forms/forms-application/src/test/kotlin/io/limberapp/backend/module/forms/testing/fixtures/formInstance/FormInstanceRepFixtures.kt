package io.limberapp.backend.module.forms.testing.fixtures.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object FormInstanceRepFixtures {
    data class Fixture(
        val creation: (featureGuid: UUID, formTemplateGuid: UUID) -> FormInstanceRep.Creation,
        val complete: ResourceTest.(featureGuid: UUID, formTemplateGuid: UUID, idSeed: Int) -> FormInstanceRep.Complete
    )

    val fixture = Fixture({ featureGuid, formTemplateGuid ->
        FormInstanceRep.Creation(
            featureGuid = featureGuid,
            formTemplateGuid = formTemplateGuid
        )
    }, { featureGuid, formTemplateGuid, idSeed ->
        FormInstanceRep.Complete(
            guid = deterministicUuidGenerator[idSeed],
            createdDate = LocalDateTime.now(fixedClock),
            featureGuid = featureGuid,
            formTemplateGuid = formTemplateGuid,
            questions = emptyList()
        )
    })
}

internal fun FormInstanceRep.Complete.summary() = FormInstanceRep.Summary(
    guid = guid,
    createdDate = createdDate,
    featureGuid = featureGuid,
    formTemplateGuid = formTemplateGuid
)
