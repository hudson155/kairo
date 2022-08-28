package limber.config

public abstract class TestConfig : Config() {
  override val clock: ClockConfig = ClockConfig(ClockConfig.Type.Fixed)
  override val guids: GuidsConfig = GuidsConfig(GuidsConfig.Generation.Deterministic)
  override val name: String = "test"
  override val server: ServerConfig = ServerConfig(0, 0)
}
