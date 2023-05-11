package limber.endpoint.organization

import com.google.inject.Inject
import limber.auth.OrganizationAuth
import limber.auth.OrganizationPermission
import limber.auth.auth
import limber.exception.organization.OrganizationDoesNotExist
import limber.feature.rest.RestEndpointHandler
import limber.mapper.organization.OrganizationMapper
import limber.model.organization.OrganizationModel
import limber.service.organization.OrganizationService
import limber.util.updater.update
import limber.api.organization.OrganizationApi as Api
import limber.rep.organization.OrganizationRep as Rep

public class UpdateOrganization @Inject internal constructor(
  private val organizationMapper: OrganizationMapper,
  private val organizationService: OrganizationService,
) : RestEndpointHandler<Api.Update, Rep>(Api.Update::class) {
  override suspend fun handler(endpoint: Api.Update): Rep {
    auth(
      auth = OrganizationAuth(OrganizationPermission.OrganizationUpdate, endpoint.organizationGuid),
      onFail = { throw OrganizationDoesNotExist() },
    )

    val update = getBody(endpoint)
    val organization = organizationService.update(endpoint.organizationGuid) { existing ->
      OrganizationModel.Update(
        name = update(existing.name, update.name),
      )
    }

    return organizationMapper(organization)
  }
}
