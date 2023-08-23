package limber.feature.event

import limber.config.sql.EventConfig
import limber.feature.TestFeature

public class TestEventFeature(
  config: EventConfig,
) : EventFeature(config), TestFeature
