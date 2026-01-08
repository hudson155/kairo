package kairo.optional

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.ReferenceType
import com.fasterxml.jackson.databind.type.TypeBindings
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.databind.type.TypeModifier
import java.lang.reflect.Type

/**
 * Configures Jackson to treat [Optional] like [java.util.Optional]
 * when it comes to [JsonInclude.Include.NON_ABSENT].
 */
internal class OptionalTypeModifier : TypeModifier() {
  override fun modifyType(
    type: JavaType,
    jdkType: Type,
    bindings: TypeBindings,
    typeFactory: TypeFactory,
  ): JavaType {
    if (type.rawClass != Optional::class.java) return type
    if (type is ReferenceType) return type

    val content = type.containedTypeOrUnknown(0)

    return typeFactory.constructReferenceType(Optional::class.java, content)
  }
}
