package kairo.feature

@LifecycleDsl
public fun lifecycle(block: LifecycleBuilder.() -> Unit): Feature.Lifecycle {
  LifecycleBuilder().apply(block)
  return Feature.Lifecycle()
}
