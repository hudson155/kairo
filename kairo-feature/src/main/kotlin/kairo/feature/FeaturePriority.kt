package kairo.feature

public object FeaturePriority {
  public const val dependencyInjection: Int = -100_000_000

  public const val database: Int = -20_000_000

  public const val default: Int = 0

  public const val rest: Int = 80_000_000

  public const val healthCheck: Int = 100_000_000
}
