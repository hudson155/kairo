package kairo.sql.util

public fun IntArray.expectAllSingles(
  block: () -> Nothing = { throw IllegalArgumentException("IntArray was empty.") },
) {
  forEach { int ->
    if (int == 0) block()
    require(int == 1) { "IntArray has an unexpected element: $int." }
  }
}

public fun IntArray.expectAllSingleOrNulls() {
  forEach { int ->
    require(int == 0 || int == 1) { "IntArray has an unexpected element: $int." }
  }
}
