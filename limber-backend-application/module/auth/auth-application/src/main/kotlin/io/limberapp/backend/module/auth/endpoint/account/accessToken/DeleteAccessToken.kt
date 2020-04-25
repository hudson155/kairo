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
import io.limberapp.backend.module.auth.service.accessToken.AccessTokenService
import java.util.UUID

/**
 * Deletes the given access token from the given account.
 */
internal class DeleteAccessToken @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val accessTokenService: AccessTokenService
) : LimberApiEndpoint<AccessTokenApi.Delete, Unit>(
    application, servingConfig.apiPathPrefix,
    endpointTemplate = AccessTokenApi.Delete::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = AccessTokenApi.Delete(
        accountGuid = call.parameters.getAsType(UUID::class, "accountGuid"),
        accessTokenGuid = call.parameters.getAsType(UUID::class, "accessTokenGuid")
    )

    override suspend fun Handler.handle(command: AccessTokenApi.Delete) {
        Authorization.Role(JwtRole.SUPERUSER).authorize()
        accessTokenService.delete(command.accountGuid, command.accessTokenGuid)
    }
}
