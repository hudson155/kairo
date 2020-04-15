package io.limberapp.web.api

import io.limberapp.web.api.formInstance.FormInstanceApi
import io.limberapp.web.api.formTemplate.FormTemplateApi
import io.limberapp.web.api.org.OrgApi
import io.limberapp.web.api.tenant.TenantApi
import io.limberapp.web.api.user.UserApi

internal object Api {
    val FormInstances = FormInstanceApi
    val FormTemplates = FormTemplateApi
    val Orgs = OrgApi
    val Tenants = TenantApi
    val Users = UserApi
}
