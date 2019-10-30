package io.limberapp.backend.module.orgs.endpoint.module

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.module.orgs.mapper.module.ModuleMapper
import io.limberapp.backend.module.orgs.rep.module.ModuleRep
import io.limberapp.backend.module.orgs.service.module.ModuleService
import io.limberapp.framework.endpoint.NullableRepApiEndpoint
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

internal class GetModuleById @Inject constructor(
    application: Application,
    private val moduleService: ModuleService
) : NullableRepApiEndpoint<GetModuleById.Command, ModuleRep.Complete>(application, config) {

    internal data class Command(
        val orgId: UUID,
        val moduleId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, "orgId"),
        moduleId = call.parameters.getAsType(UUID::class, "moduleId")
    )

    override fun authorization(command: Command) = Authorization.Public

    override suspend fun handler(command: Command): ModuleRep.Complete? {
        val completeModel = moduleService.getById(command.moduleId)
        return completeModel?.let { ModuleMapper.completeRep(it) }
    }

    companion object {
        private val config = Config(HttpMethod.Get, "/orgs/{orgId}/modules/{moduleId}")
    }
}
