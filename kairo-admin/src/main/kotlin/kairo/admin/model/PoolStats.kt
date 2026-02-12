package kairo.admin.model

public data class PoolStats(
  val acquiredSize: Int,
  val allocatedSize: Int,
  val idleSize: Int,
  val pendingAcquireSize: Int,
  val maxAllocatedSize: Int,
  val maxPendingAcquireSize: Int,
)
