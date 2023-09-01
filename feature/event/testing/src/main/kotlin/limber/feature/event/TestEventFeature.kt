package limber.feature.event

import limber.config.event.EventConfig
import limber.feature.TestFeature

public class TestEventFeature(
  config: EventConfig,
) : EventFeature(config), TestFeature {
  init {
    check(config.publish == null)
    check(config.subscribe == null)
  }
}
