package kairo.feature

/**
 * Features are initialized in order according to this enum.
 * Kairo guarantees that all Features of a given priority will be initialized
 * prior to any Feature of the next priority,
 * but provides no ordering guarantees for Features sharing the same priority.
 */
public enum class FeaturePriority { Monitoring, Framework, Normal }
