package kairo.transactionManager

/**
 * Exception thrown when a method requiring a transaction is called without one.
 */
public class MissingTransactionException : TransactionManagerException(
  message = "This operation requires an active transaction context",
  cause = Exception("No transaction context was found"),
)
