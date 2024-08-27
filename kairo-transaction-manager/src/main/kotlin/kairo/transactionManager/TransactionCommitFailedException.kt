package kairo.transactionManager

public class TransactionCommitFailedException(cause: Exception) : TransactionManagerException(
  message = "An error occurred while committing a transaction.",
  cause = cause,
)
