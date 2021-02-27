package io.limberapp.endpoint.org

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.org.OrgApi
import io.limberapp.auth.auth.AuthSuperuser
import io.limberapp.mapper.org.OrgMapper
import io.limberapp.rep.org.OrgRep
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.org.OrgService

internal class PostOrg @Inject constructor(
    private val orgService: OrgService,
    private val orgMapper: OrgMapper,
) : EndpointHandler<OrgApi.Post, OrgRep.Complete>(
    template = OrgApi.Post::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): OrgApi.Post =
      OrgApi.Post(rep = call.getAndValidateBody())

  override suspend fun Handler.handle(endpoint: OrgApi.Post): OrgRep.Complete {
    val rep = endpoint.rep.required()
    auth(AuthSuperuser)
    val org = orgService.create(orgMapper.model(rep))
    return orgMapper.completeRep(org, emptyList())
  }
}
