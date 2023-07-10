package limber.api.organizationAuth

import io.ktor.http.HttpMethod
import jakarta.validation.Valid
import limber.feature.rest.QueryParam
import limber.feature.rest.RestEndpoint
import limber.rep.organizationAuth.OrganizationAuthRep

public object OrganizationAuthApi {
  public data class Get(
    val authId: String,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organization-auths/$authId"
  }

  public data class GetByOrganization(
    val organizationId: String,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations/$organizationId/auths"
  }

  public data class GetByHostname(
    val hostname: String,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organization-auths"
    override val qp: List<QueryParam> = listOf(::hostname)
  }

  public data class Create(
    val organizationId: String,
    @Valid override val body: OrganizationAuthRep.Creator?,
  ) : RestEndpoint<OrganizationAuthRep.Creator>() {
    override val method: HttpMethod = HttpMethod.Post
    override val path: String = "/organizations/$organizationId/auths"
  }

  public data class Update(
    val authId: String,
    @Valid override val body: OrganizationAuthRep.Update?,
  ) : RestEndpoint<OrganizationAuthRep.Update>() {
    override val method: HttpMethod = HttpMethod.Patch
    override val path: String = "/organization-auths/$authId"
  }

  public data class Delete(
    val authId: String,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Delete
    override val path: String = "/organization-auths/$authId"
  }
}
