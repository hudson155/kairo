package limber.transaction

public class TransactionContextCreationFailedException(cause: Exception) : TransactionManagerException(
  message = "An error occurred while creating the context for a transaction.",
  cause = cause,
)
