package kairo.feature

/**
 * Some common priorities are defined here, but feel free to also create your own.
 * During [LifecycleHandler.start], lower priorities run first.
 * During [LifecycleHandler.stop], higher priorities run first.
 */
public object FeaturePriority {
  public const val dependencyInjection: Int = -100_000_000

  public const val database: Int = -20_000_000

  public const val default: Int = 0

  public const val rest: Int = 80_000_000

  public const val healthCheck: Int = 100_000_000
}
