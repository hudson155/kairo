package limber.feature.auth0

import com.auth0.client.auth.AuthAPI
import com.auth0.client.mgmt.ManagementAPI
import com.auth0.json.mgmt.organizations.Organization
import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.google.inject.Inject
import limber.config.auth0.Auth0Config
import limber.feature.auth0.rep.Auth0OrganizationRep
import java.util.concurrent.TimeUnit

internal class RealAuth0ManagementApi @Inject constructor(
  private val config: Auth0Config.ManagementApi.Real,
) : Auth0ManagementApi {
  private val authApi: AuthAPI =
    AuthAPI.newBuilder(config.domain, config.clientId, config.clientSecret.value).build()

  private val managementApi: Supplier<ManagementAPI> =
    Suppliers.memoizeWithExpiration(::createManagementApi, 6, TimeUnit.HOURS)

  private fun createManagementApi(): ManagementAPI {
    val request = authApi.requestToken("https://${config.domain}/api/v2/")
    val result = request.execute().body
    return ManagementAPI.newBuilder(config.domain, result.accessToken).build()
  }

  override fun createOrganization(creator: Auth0OrganizationRep.Creator): Auth0OrganizationRep {
    val organization = Organization().apply {
      name = creator.name
    }

    val request = managementApi.get().organizations().create(organization)
    return Auth0OrganizationRep(request.execute().body)
  }

  override fun updateOrganization(
    organizationId: String,
    update: Auth0OrganizationRep.Update,
  ): Auth0OrganizationRep {
    val organization = Organization().apply {
      update.name?.let { name = it }
    }

    val request = managementApi.get().organizations().update(organizationId, organization)
    return Auth0OrganizationRep(request.execute().body)
  }

  override fun deleteOrganization(organizationId: String) {
    val request = managementApi.get().organizations().delete(organizationId)
    request.execute()
  }
}
