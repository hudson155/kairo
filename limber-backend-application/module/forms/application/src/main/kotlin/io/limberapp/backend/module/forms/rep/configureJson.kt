package io.limberapp.backend.module.forms.rep

import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRepMixIn
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRepMixIn

internal fun configureJsonForFormsModule(json: Json) {

    json.objectMapper.addMixIn(
        FormInstanceQuestionRep.Creation::class.java,
        FormInstanceQuestionRepMixIn.Creation::class.java
    )

    json.objectMapper.addMixIn(
        FormInstanceQuestionRep.Complete::class.java,
        FormInstanceQuestionRepMixIn.Complete::class.java
    )

    json.objectMapper.addMixIn(
        FormTemplateQuestionRep.Creation::class.java,
        FormTemplateQuestionRepMixIn.Creation::class.java
    )

    json.objectMapper.addMixIn(
        FormTemplateQuestionRep.Complete::class.java,
        FormTemplateQuestionRepMixIn.Complete::class.java
    )

    json.objectMapper.addMixIn(
        FormTemplateQuestionRep.Update::class.java,
        FormTemplateQuestionRepMixIn.Update::class.java
    )
}
