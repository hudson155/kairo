package kairo.rest

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receiveStream
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.javaType

internal sealed class RestEndpointArgument(
  private val call: ApplicationCall,
  protected val param: KParameter,
) {
  suspend fun read(): Any? = read(call)

  protected abstract suspend fun read(call: ApplicationCall): Any?

  internal class Body(
    call: ApplicationCall,
    param: KParameter,
  ) : RestEndpointArgument(call, param) {
    override suspend fun read(call: ApplicationCall): Any =
      ktorMapper.readValue(call.receiveStream(), ktorMapper.constructType(param.type.javaType))
  }

  internal class Param(
    call: ApplicationCall,
    param: KParameter,
  ) : RestEndpointArgument(call, param) {
    private val name: String = checkNotNull(param.name)

    override suspend fun read(call: ApplicationCall): Any? {
      val parameters = call.parameters.getAll(name) ?: return null
      val parameter = parameters.single() // Lists are not supported yet.
      return ktorMapper.convertValue(parameter, ktorMapper.constructType(param.type.javaType))
    }
  }
}
