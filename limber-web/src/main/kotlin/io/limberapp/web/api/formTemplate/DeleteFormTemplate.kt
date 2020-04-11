package io.limberapp.web.api.formTemplate

import io.limberapp.web.api.Api

internal suspend fun deleteFormTemplate(formTemplateId: String) {
    Api.delete("/form-templates/$formTemplateId")
}
