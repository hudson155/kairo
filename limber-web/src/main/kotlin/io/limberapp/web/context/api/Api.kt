package io.limberapp.web.context.api

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.auth.client.org.role.OrgRoleClient
import io.limberapp.backend.module.auth.client.org.role.OrgRoleMembershipClient
import io.limberapp.backend.module.auth.client.tenant.TenantClient
import io.limberapp.backend.module.forms.client.formInstance.FormInstanceClient
import io.limberapp.backend.module.forms.client.formTemplate.FormTemplateClient
import io.limberapp.backend.module.forms.rep.formsSerialModule
import io.limberapp.backend.module.orgs.client.org.OrgClient
import io.limberapp.backend.module.users.client.user.UserClient

internal val json = Json(context = formsSerialModule)

internal class Api(fetch: Fetch) {
  val formInstances = FormInstanceClient(fetch, json)
  val formTemplates = FormTemplateClient(fetch, json)
  val orgs = OrgClient(fetch, json)
  val orgRoleMemberships = OrgRoleMembershipClient(fetch, json)
  val orgRoles = OrgRoleClient(fetch, json)
  val tenants = TenantClient(fetch, json)
  val users = UserClient(fetch, json)
}
