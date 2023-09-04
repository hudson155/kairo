package limber.endpoint.organization

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.feature.rest.RestEndpointHandler
import limber.mapper.organization.OrganizationMapper
import limber.service.organization.OrganizationInterface
import limber.api.organization.OrganizationApi as Api
import limber.rep.organization.OrganizationRep as Rep

public class DeleteOrganization @Inject internal constructor(
  private val organizationMapper: OrganizationMapper,
  private val organizationService: OrganizationInterface,
) : RestEndpointHandler<Api.Delete, Rep>(Api.Delete::class) {
  override suspend fun handler(endpoint: Api.Delete): Rep {
    auth { OrganizationAuth(OrganizationPermission.Organization_Delete, endpoint.organizationId) }

    val organization = organizationService.delete(endpoint.organizationId)

    return organizationMapper(organization)
  }
}
