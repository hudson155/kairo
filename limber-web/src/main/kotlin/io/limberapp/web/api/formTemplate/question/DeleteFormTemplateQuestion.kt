package io.limberapp.web.api.formTemplate.question

import io.limberapp.web.api.Api

internal suspend fun deleteFormTemplateQuestion(formTemplateId: String, questionId: String) {
    Api.delete("/form-templates/$formTemplateId/questions/$questionId")
}
