package io.limberapp.backend.module.orgs.endpoint.module

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.module.orgs.mapper.module.ModuleMapper
import io.limberapp.backend.module.orgs.rep.module.ModuleRep
import io.limberapp.backend.module.orgs.service.module.ModuleService
import io.limberapp.framework.endpoint.RepListApiEndpoint
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

internal class GetModulesByOrgId @Inject constructor(
    application: Application,
    private val moduleService: ModuleService
) : RepListApiEndpoint<GetModulesByOrgId.Command, ModuleRep.Complete>(application, config) {

    internal data class Command(
        val orgId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, "orgId")
    )

    override fun authorization(command: Command) = Authorization.Public

    override suspend fun handler(command: Command): List<ModuleRep.Complete> {
        val completeModels = moduleService.getByOrgId(command.orgId)
        return completeModels.map { ModuleMapper.completeRep(it) }
    }

    companion object {
        private val config = Config(HttpMethod.Get, "/orgs/{orgId}/modules")
    }
}
