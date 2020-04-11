package io.limberapp.web.api.formInstance.question

import io.limberapp.web.api.Api

internal suspend fun deleteFormInstanceQuestion(formInstanceId: String, questionId: String) {
    Api.delete("/form-instances/$formInstanceId/questions/$questionId")
}
