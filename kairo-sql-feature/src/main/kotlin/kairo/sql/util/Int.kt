package kairo.sql.util

public fun Int.expectSingle(
  block: () -> Nothing = { throw IllegalArgumentException("IntArray was empty.") },
) {
  if (this == 0) block()
  require(this == 1) { "IntArray has an unexpected element: $this." }
}
