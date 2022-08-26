package limber.feature

/**
 * Features are initialized in descending order according to this enum.
 * We guarantee that all features of a given priority will be initialized
 * prior to any feature of the next priority,
 * but provide no ordering guarantees for features with the same priority.
 */
public enum class FeaturePriority { Monitoring, Framework, Normal }
