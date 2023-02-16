package limber.feature

/**
 * Features are initialized in order according to this enum.
 * We guarantee that all Features of a given priority will be initialized
 * prior to any Feature of the next priority,
 * but provide no ordering guarantees for Features with the same priority.
 */
public enum class FeaturePriority { Monitoring, Framework, Normal }
