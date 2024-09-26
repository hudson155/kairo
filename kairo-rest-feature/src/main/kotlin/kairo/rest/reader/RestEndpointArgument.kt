package kairo.rest.reader

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import kairo.rest.ktorServerMapper
import kairo.rest.util.typeInfo
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.javaType

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
    override suspend fun read(call: ApplicationCall): Any =
      call.receive(typeInfo(param.type))
  }

  internal class Param(
    call: ApplicationCall,
    param: KParameter,
  ) : RestEndpointArgument(call, param) {
    private val name: String = checkNotNull(param.name)

    override suspend fun read(call: ApplicationCall): Any? {
      val parameters = call.parameters.getAll(name) ?: return null
      val parameter = parameters.single() // Lists are not supported yet.
      @Suppress("ForbiddenMethodCall")
      return ktorServerMapper.convertValue(parameter, ktorServerMapper.constructType(param.type.javaType))
    }
  }
}
