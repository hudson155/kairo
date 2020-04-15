package io.limberapp.web.context.api

import io.limberapp.web.api.formInstance.FormInstanceApi
import io.limberapp.web.api.formTemplate.FormTemplateApi
import io.limberapp.web.api.org.OrgApi
import io.limberapp.web.api.tenant.TenantApi
import io.limberapp.web.api.user.UserApi

internal class Api {
    val formInstances = FormInstanceApi()
    val formTemplates = FormTemplateApi()
    val orgs = OrgApi()
    val tenants = TenantApi()
    val users = UserApi()
}
