package limber.config

public abstract class Config {
  public abstract val clock: ClockConfig
  public abstract val guids: GuidsConfig
  public abstract val name: String
  public abstract val server: ServerConfig
}
