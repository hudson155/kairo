package io.limberapp.backend.module.auth.endpoint.account.accessToken

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.JwtRole
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.auth.api.accessToken.AccessTokenApi
import io.limberapp.backend.module.auth.mapper.accessToken.AccessTokenMapper
import io.limberapp.backend.module.auth.rep.accessToken.AccessTokenRep
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenService
import java.util.UUID

/**
 * Returns all access tokens for the given account. Note that this endpoint does not actually return the token itself,
 * just its ID. The token itself is only returned once, immediately after it is created. The account must record the
 * token appropriately, because it cannot be returned again. If a new token is needed, the account can always delete the
 * existing token and create another.
 */
internal class GetAccessTokensByAccountId @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val accessTokenService: AccessTokenService,
    private val accessTokenMapper: AccessTokenMapper
) : LimberApiEndpoint<AccessTokenApi.GetByAccountId, List<AccessTokenRep.Complete>>(
    application, servingConfig.apiPathPrefix,
    endpointTemplate = AccessTokenApi.GetByAccountId::class.template()
) {

    override suspend fun determineCommand(call: ApplicationCall) = AccessTokenApi.GetByAccountId(
        accountId = call.parameters.getAsType(UUID::class, "accountId")
    )

    override suspend fun Handler.handle(command: AccessTokenApi.GetByAccountId): List<AccessTokenRep.Complete> {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        val models = accessTokenService.getByAccountId(command.accountId)
        return models.map { accessTokenMapper.completeRep(it) }
    }
}
