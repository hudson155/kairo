package kairo.sql.util

public fun Int.expectSingle(
  block: () -> Nothing = { throw IllegalArgumentException("Int was 0.") },
) {
  if (this == 0) block()
  require(this == 1) { "IntArray has an unexpected element: $this." }
}
