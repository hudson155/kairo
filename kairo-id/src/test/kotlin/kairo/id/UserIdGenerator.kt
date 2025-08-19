package kairo.id

internal class UserIdGenerator(
  strategy: IdGenerationStrategy,
) : IdGenerator<UserId>(strategy, prefix = "user") {
  override fun wrap(value: String): UserId =
    UserId(value)
}
