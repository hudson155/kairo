package kairo.transactionManager

public class TransactionBeginFailedException(cause: Exception) : TransactionManagerException(
  message = "An error occurred while beginning a transaction.",
  cause = cause,
)
