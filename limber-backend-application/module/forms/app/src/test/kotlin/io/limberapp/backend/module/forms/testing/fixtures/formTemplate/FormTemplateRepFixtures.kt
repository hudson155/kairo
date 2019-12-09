package io.limberapp.backend.module.forms.testing.fixtures.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object FormTemplateRepFixtures {

    data class Fixture(
        val creation: (orgId: UUID) -> FormTemplateRep.Creation,
        val complete: ResourceTest.(orgId: UUID, idSeed: Int) -> FormTemplateRep.Complete
    )

    operator fun get(i: Int) = fixtures[i]

    private val fixtures = listOf(
        Fixture({ orgId ->
            FormTemplateRep.Creation(
                orgId = orgId,
                title = "Example Form"
            )
        }, { orgId, idSeed ->
            FormTemplateRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                orgId = orgId,
                title = "Example form",
                description = null,
                parts = listOf(FormTemplatePartRepFixtures.default.complete(this, idSeed + 1))
            )
        }),
        Fixture({ orgId ->
            FormTemplateRep.Creation(
                orgId = orgId,
                title = "Vehicle Inspection"
            )
        }, { orgId, idSeed ->
            FormTemplateRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                orgId = orgId,
                title = "Vehicle Inspection",
                description = null,
                parts = listOf(FormTemplatePartRepFixtures.default.complete(this, idSeed + 1))
            )
        })
    )
}
