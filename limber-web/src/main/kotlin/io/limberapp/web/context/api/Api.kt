package io.limberapp.web.context.api

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.auth.tenant.TenantClient
import io.limberapp.backend.module.forms.rep.formsSerialModule
import io.limberapp.backend.module.orgs.org.OrgClient
import io.limberapp.web.api.formInstance.FormInstanceApi
import io.limberapp.web.api.formTemplate.FormTemplateApi
import io.limberapp.web.api.user.UserApi

private val json = Json(context = formsSerialModule)

internal class Api(fetch: Fetch) {
    val formInstances = FormInstanceApi(fetch, json)
    val formTemplates = FormTemplateApi(fetch, json)
    val orgs = OrgClient(fetch, json)
    val tenants = TenantClient(fetch, json)
    val users = UserApi(fetch, json)
}
