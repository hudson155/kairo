package io.limberapp.backend.module.forms.formTemplate.question

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep

class FormTemplateQuestionClient(private val fetch: Fetch, private val json: Json) {

    suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Post): FormTemplateQuestionRep.Complete {
        val string = fetch(endpoint)
        return json.parse(string)
    }

    suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Patch): FormTemplateQuestionRep.Complete {
        val string = fetch(endpoint)
        return json.parse(string)
    }

    suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Delete) {
        fetch(endpoint)
    }
}
