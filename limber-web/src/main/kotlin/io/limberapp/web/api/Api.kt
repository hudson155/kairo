package io.limberapp.web.api

import com.piperframework.restInterface.Fetch
import io.limberapp.backend.module.auth.client.org.role.OrgRoleClient
import io.limberapp.backend.module.auth.client.org.role.OrgRoleClientImpl
import io.limberapp.backend.module.auth.client.org.role.OrgRoleMembershipClient
import io.limberapp.backend.module.auth.client.org.role.OrgRoleMembershipClientImpl
import io.limberapp.backend.module.auth.client.tenant.TenantClient
import io.limberapp.backend.module.auth.client.tenant.TenantClientImpl
import io.limberapp.backend.module.forms.client.formInstance.FormInstanceClient
import io.limberapp.backend.module.forms.client.formInstance.FormInstanceClientImpl
import io.limberapp.backend.module.forms.client.formInstance.question.FormInstanceQuestionClient
import io.limberapp.backend.module.forms.client.formInstance.question.FormInstanceQuestionClientImpl
import io.limberapp.backend.module.forms.client.formTemplate.FormTemplateClient
import io.limberapp.backend.module.forms.client.formTemplate.FormTemplateClientImpl
import io.limberapp.backend.module.orgs.client.org.OrgClient
import io.limberapp.backend.module.orgs.client.org.OrgClientImpl
import io.limberapp.backend.module.users.client.user.UserClient
import io.limberapp.backend.module.users.client.user.UserClientImpl

internal class Api(fetch: Fetch) :
  FormInstanceClient by FormInstanceClientImpl(fetch, json),
  FormInstanceQuestionClient by FormInstanceQuestionClientImpl(fetch, json),
  FormTemplateClient by FormTemplateClientImpl(fetch, json),
  OrgClient by OrgClientImpl(fetch, json),
  OrgRoleClient by OrgRoleClientImpl(fetch, json),
  OrgRoleMembershipClient by OrgRoleMembershipClientImpl(fetch, json),
  TenantClient by TenantClientImpl(fetch, json),
  UserClient by UserClientImpl(fetch, json)
