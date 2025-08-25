package kairo.rest

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.util.getOrFail
import kotlin.reflect.KParameter
import kotlinx.serialization.json.Json

internal sealed class RestEndpointArgument(
  private val call: ApplicationCall,
  protected val param: KParameter,
) {
  suspend fun read(): Any? =
    read(call)

  protected abstract suspend fun read(call: ApplicationCall): Any?

  internal class Body(
    call: ApplicationCall,
    param: KParameter,
  ) : RestEndpointArgument(call, param) {
    override suspend fun read(call: ApplicationCall): Any {
      // TODO: Try/catch.
      TODO()
      // return call.receive(typeInfo(param.type))
    }
  }

  internal class Param(
    call: ApplicationCall,
    param: KParameter,
  ) : RestEndpointArgument(call, param) {
    private val name: String = checkNotNull(param.name)

    override suspend fun read(call: ApplicationCall): Any? {
      TODO()
      // Json.res
      // call.parameters.getOrFail<>()
      // val parameters = call.parameters.getAll(name) ?: return null
      // val parameter = parameters.single() // Lists are not supported yet.
      // // TODO: Try/catch.
      // return convert(parameter)
    }

    // private fun convert(parameter: String): Any? {
    //   val type = KtorServerMapper.json.constructType(param.type.javaType)
    //   return KtorServerMapper.json.kairoReadSpecial(parameter, type)
    // }
  }
}
