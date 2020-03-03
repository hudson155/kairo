package io.limberapp.backend.module.forms.testing.fixtures.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object FormTemplateRepFixtures {

    data class Fixture(
        val creation: (featureId: UUID) -> FormTemplateRep.Creation,
        val complete: ResourceTest.(featureId: UUID, idSeed: Int) -> FormTemplateRep.Complete
    )

    val exampleFormFixture = Fixture({ featureId ->
        FormTemplateRep.Creation(
            featureId = featureId,
            title = "Example form"
        )
    }, { featureId, idSeed ->
        FormTemplateRep.Complete(
            id = deterministicUuidGenerator[idSeed],
            created = LocalDateTime.now(fixedClock),
            featureId = featureId,
            title = "Example form",
            description = null,
            questions = FormTemplateQuestionRepFixtures.defaults.mapIndexed { i, rep ->
                rep.complete(this, idSeed + 1 + i)
            }
        )
    })

    val vehicleInspectionFixture = Fixture({ featureId ->
        FormTemplateRep.Creation(
            featureId = featureId,
            title = "Vehicle Inspection"
        )
    }, { featureId, idSeed ->
        FormTemplateRep.Complete(
            id = deterministicUuidGenerator[idSeed],
            created = LocalDateTime.now(fixedClock),
            featureId = featureId,
            title = "Vehicle Inspection",
            description = null,
            questions = FormTemplateQuestionRepFixtures.defaults.mapIndexed { i, rep ->
                rep.complete(this, idSeed + 1 + i)
            }
        )
    })
}
