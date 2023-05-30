package limber.config

public abstract class TestConfig : Config() {
  override val clock: ClockConfig = ClockConfig(ClockConfig.Type.Fixed)
  override val ids: IdsConfig = IdsConfig(IdsConfig.Generation.Deterministic)
  override val name: String = "test"
  override val server: ServerConfig = ServerConfig(ServerConfig.Lifecycle(0, 0))
}
