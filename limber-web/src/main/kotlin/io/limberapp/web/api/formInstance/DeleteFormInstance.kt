package io.limberapp.web.api.formInstance

import io.limberapp.web.api.Api

internal suspend fun deleteFormInstance(formInstanceId: String) {
    Api.delete("/form-instances/$formInstanceId")
}
