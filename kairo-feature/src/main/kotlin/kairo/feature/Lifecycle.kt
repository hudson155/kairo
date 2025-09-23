package kairo.feature

public fun lifecycle(block: LifecycleBuilder.() -> Unit): List<LifecycleHandler> =
  LifecycleBuilder().apply(block).handlers
