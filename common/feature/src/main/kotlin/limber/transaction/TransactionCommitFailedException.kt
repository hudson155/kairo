package limber.transaction

public class TransactionCommitFailedException(cause: Exception) : TransactionManagerException(
  message = "An error occurred while committing a transaction.",
  cause = cause,
)
