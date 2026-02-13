package kairo.admin.collector

import kairo.admin.model.AdminDependencyInfo
import org.koin.core.Koin
import org.koin.core.annotation.KoinInternalApi

public class DependencyCollector(
  private val koinProvider: () -> Koin?,
) {
  @OptIn(KoinInternalApi::class)
  public fun collect(): List<AdminDependencyInfo> {
    val koin = koinProvider() ?: return emptyList()
    return koin.instanceRegistry.instances.values.map { factory ->
      val definition = factory.beanDefinition
      val qualifiedName = definition.primaryType.qualifiedName
        ?: definition.primaryType.simpleName
        ?: "Unknown"
      val simpleName = definition.primaryType.simpleName ?: "Unknown"
      val packageName = if (qualifiedName.contains('.')) {
        qualifiedName.substringBeforeLast('.')
      } else {
        ""
      }
      AdminDependencyInfo(
        className = qualifiedName,
        simpleName = simpleName,
        packageName = packageName,
        kind = definition.kind.name,
        qualifier = definition.qualifier?.value,
        scope = definition.scopeQualifier.value.takeIf { it != "_root_" },
        secondaryTypes = definition.secondaryTypes.mapNotNull { it.qualifiedName },
        hasCallbacks = definition.callbacks.onClose != null,
      )
    }.sortedBy { it.className }
  }
}
