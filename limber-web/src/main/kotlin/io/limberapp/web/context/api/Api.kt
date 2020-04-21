package io.limberapp.web.context.api

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.auth.tenant.TenantClient
import io.limberapp.backend.module.forms.formInstance.FormInstanceClient
import io.limberapp.backend.module.forms.formTemplate.FormTemplateClient
import io.limberapp.backend.module.forms.rep.formsSerialModule
import io.limberapp.backend.module.orgs.org.OrgClient
import io.limberapp.backend.module.users.user.UserClient

private val json = Json(context = formsSerialModule)

internal class Api(fetch: Fetch) {
    val formInstances = FormInstanceClient(fetch, json)
    val formTemplates = FormTemplateClient(fetch, json)
    val orgs = OrgClient(fetch, json)
    val tenants = TenantClient(fetch, json)
    val users = UserClient(fetch, json)
}
