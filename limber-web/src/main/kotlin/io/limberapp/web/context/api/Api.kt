package io.limberapp.web.context.api

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.auth.tenant.TenantClient
import io.limberapp.backend.module.forms.formInstance.FormInstanceClient
import io.limberapp.backend.module.forms.formTemplate.FormTemplateClient
import io.limberapp.backend.module.forms.rep.formsSerialModule
import io.limberapp.web.api.org.OrgApi
import io.limberapp.web.api.user.UserApi

private val json = Json(context = formsSerialModule)

internal class Api(fetch: Fetch) {
    val formInstances = FormInstanceClient(fetch, json)
    val formTemplates = FormTemplateClient(fetch, json)
    val orgs = OrgApi(fetch, json)
    val tenants = TenantClient(fetch, json)
    val users = UserApi(fetch, json)
}
