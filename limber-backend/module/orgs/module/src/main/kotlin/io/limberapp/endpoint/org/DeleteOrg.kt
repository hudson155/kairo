package io.limberapp.endpoint.org

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.org.OrgApi
import io.limberapp.auth.auth.AuthSuperuser
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.org.OrgService
import java.util.UUID

internal class DeleteOrg @Inject constructor(
    private val orgService: OrgService,
) : EndpointHandler<OrgApi.Delete, Unit>(
    template = OrgApi.Delete::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): OrgApi.Delete =
      OrgApi.Delete(orgGuid = call.getParam(UUID::class, "orgGuid"))

  override suspend fun Handler.handle(endpoint: OrgApi.Delete) {
    auth(AuthSuperuser)
    orgService.delete(endpoint.orgGuid)
  }
}
