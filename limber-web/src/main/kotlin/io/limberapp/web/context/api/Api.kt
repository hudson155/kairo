package io.limberapp.web.context.api

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.rep.formsSerialModule
import io.limberapp.web.api.formInstance.FormInstanceApi
import io.limberapp.web.api.formTemplate.FormTemplateApi
import io.limberapp.web.api.org.OrgApi
import io.limberapp.web.api.tenant.TenantApi
import io.limberapp.web.api.user.UserApi

private val json = Json(context = formsSerialModule)

internal class Api(fetch: Fetch) {
    val formInstances = FormInstanceApi(fetch, json)
    val formTemplates = FormTemplateApi(fetch, json)
    val orgs = OrgApi(fetch, json)
    val tenants = TenantApi(fetch, json)
    val users = UserApi(fetch, json)
}
