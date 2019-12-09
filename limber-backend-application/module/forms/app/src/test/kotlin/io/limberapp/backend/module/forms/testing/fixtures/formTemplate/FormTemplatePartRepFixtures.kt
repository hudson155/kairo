package io.limberapp.backend.module.forms.testing.fixtures.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplatePartRep
import io.limberapp.backend.module.forms.testing.ResourceTest

internal object FormTemplatePartRepFixtures {

    data class Fixture(
        val creation: () -> FormTemplatePartRep.Creation,
        val complete: ResourceTest.(idSeed: Int) -> FormTemplatePartRep.Complete
    )

    operator fun get(i: Int) = fixtures[i]

    private val fixtures = listOf(
        Fixture({
            FormTemplatePartRep.Creation(
                title = "Part 0",
                description = "This is the 0th part. It's not default in any form template, but added manually."
            )
        }, { idSeed ->
            FormTemplatePartRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                title = "Part 0",
                description = "This is the 0th part. It's not default in any form template, but added manually.",
                questionGroups = listOf(FormTemplateQuestionGroupRepFixtures.default.complete(this, idSeed + 1))
            )
        })
    )

    val default = Fixture({
        FormTemplatePartRep.Creation(
            title = null,
            description = null
        )
    }, { idSeed ->
        FormTemplatePartRep.Complete(
            id = deterministicUuidGenerator[idSeed],
            title = null,
            description = null,
            questionGroups = listOf(FormTemplateQuestionGroupRepFixtures.default.complete(this, idSeed + 1))
        )
    })
}
