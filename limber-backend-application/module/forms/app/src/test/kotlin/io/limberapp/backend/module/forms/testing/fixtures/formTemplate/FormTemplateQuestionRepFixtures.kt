package io.limberapp.backend.module.forms.testing.fixtures.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime

internal object FormTemplateQuestionRepFixtures {

    data class Fixture(
        val creation: () -> FormTemplateQuestionRep.Creation,
        val complete: ResourceTest.(idSeed: Int) -> FormTemplateQuestionRep.Complete
    )

    operator fun get(i: Int) = fixtures[i]

    private val fixtures = listOf(
        Fixture({
            FormTemplateTextQuestionRep.Creation(
                label = "Additional Information",
                helpText = null,
                multiLine = true,
                placeholder = null,
                validator = null
            )
        }, { idSeed ->
            FormTemplateTextQuestionRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                label = "Additional Information",
                helpText = null,
                maxLength = 10_000,
                multiLine = true,
                placeholder = null,
                validator = null
            )
        })
    )

    val defaults = listOf(
        Fixture({
            FormTemplateTextQuestionRep.Creation(
                label = "Worker name",
                helpText = null,
                multiLine = false,
                placeholder = null,
                validator = null
            )
        }, { idSeed ->
            FormTemplateTextQuestionRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                label = "Worker name",
                helpText = null,
                maxLength = 200,
                multiLine = false,
                placeholder = null,
                validator = null
            )
        }),
        Fixture({
            FormTemplateDateQuestionRep.Creation(
                label = "Date",
                helpText = null,
                earliest = null,
                latest = null
            )
        }, { idSeed ->
            FormTemplateDateQuestionRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                label = "Date",
                helpText = null,
                earliest = null,
                latest = null
            )
        }),
        Fixture({
            FormTemplateTextQuestionRep.Creation(
                label = "Description",
                helpText = null,
                multiLine = true,
                placeholder = null,
                validator = null
            )
        }, { idSeed ->
            FormTemplateTextQuestionRep.Complete(
                id = deterministicUuidGenerator[idSeed],
                created = LocalDateTime.now(fixedClock),
                label = "Description",
                helpText = null,
                maxLength = 10_000,
                multiLine = true,
                placeholder = null,
                validator = null
            )
        })
    )
}
