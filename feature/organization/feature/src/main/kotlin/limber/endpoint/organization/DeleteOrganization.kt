package limber.endpoint.organization

import com.google.inject.Inject
import limber.auth.PlatformPermission
import limber.auth.PlatformPermissionAuth
import limber.auth.auth
import limber.exception.organization.OrganizationDoesNotExist
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
    auth(
      auth = PlatformPermissionAuth(PlatformPermission.Organization_Delete),
      onFail = { throw OrganizationDoesNotExist() },
    )

    val organization = organizationService.delete(endpoint.organizationId)

    return organizationMapper(organization)
  }
}
