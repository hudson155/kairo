package io.limberapp.backend.module.orgs.endpoint.org.module

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.ktor.request.receive
import io.limberapp.backend.module.orgs.mapper.module.ModuleMapper
import io.limberapp.backend.module.orgs.rep.module.ModuleRep
import io.limberapp.backend.module.orgs.service.module.ModuleService
import io.limberapp.framework.endpoint.RepApiEndpoint
import io.limberapp.framework.endpoint.authorization.Authorization
import io.limberapp.framework.endpoint.command.AbstractCommand
import java.util.UUID

internal class CreateModule @Inject constructor(
    application: Application,
    private val moduleService: ModuleService
) : RepApiEndpoint<CreateModule.Command, ModuleRep.Complete>(application, config) {

    internal data class Command(
        val orgId: UUID,
        val creationRep: ModuleRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, "orgId"),
        creationRep = call.receive()
    )

    override fun authorization(command: Command) = Authorization.Public

    override suspend fun handler(command: Command): ModuleRep.Complete {
        val completeModel = moduleService.create(ModuleMapper.creationModel(command.creationRep))
        return ModuleMapper.completeRep(completeModel)
    }

    companion object {
        private val config = Config(HttpMethod.Post, "/orgs/{orgId}/modules")
    }
}
