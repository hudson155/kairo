package kairo.rest.auth

@Suppress("UnusedReceiverParameter")
private fun Auth.all(vararg results: Auth.Result): Auth.Result {
  results.firstOrNull { it != Auth.Result.Success }?.let { return@all it }
  return Auth.Result.Success
}
