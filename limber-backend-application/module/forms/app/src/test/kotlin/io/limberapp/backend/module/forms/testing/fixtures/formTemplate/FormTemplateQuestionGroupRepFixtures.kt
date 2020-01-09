package io.limberapp.backend.module.forms.testing.fixtures.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionGroupRep
import io.limberapp.backend.module.forms.testing.ResourceTest

internal object FormTemplateQuestionGroupRepFixtures {

    data class Fixture(
        val complete: ResourceTest.(idSeed: Int) -> FormTemplateQuestionGroupRep.Complete
    )

    val default = Fixture { idSeed ->
        FormTemplateQuestionGroupRep.Complete(
            id = deterministicUuidGenerator[idSeed],
            questions = FormTemplateQuestionRepFixtures.defaults.mapIndexed { i, rep ->
                rep.complete(this, idSeed + 1 + i)
            }
        )
    }
}
