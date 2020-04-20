package io.limberapp.backend.module.forms.testing.fixtures.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.testing.ResourceTest
import java.time.LocalDateTime
import java.util.UUID

internal object FormInstanceQuestionRepFixtures {

    data class Fixture(
        val creation: () -> FormInstanceQuestionRep.Creation,
        val complete: ResourceTest.(questionId: UUID) -> FormInstanceQuestionRep.Complete
    )

    val textFixture = Fixture({
        FormInstanceTextQuestionRep.Creation(
            text = "Nothing significant to add."
        )
    }, { questionId ->
        FormInstanceTextQuestionRep.Complete(
            created = LocalDateTime.now(fixedClock),
            questionId = questionId,
            text = "Nothing significant to add."
        )
    })
}
