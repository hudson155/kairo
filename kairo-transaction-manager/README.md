# `kairo-transaction-manager`

A transaction is a wrapper for some code that should be either committed or rolled back
depending on whether it completes successfully or throws an exception.

The Kairo `TransactionManager` offers explicit automatic handling of transactions that span multiple systems.
It supports arbitrary transaction types;
some examples are `SqlTransaction` and `MessageTransaction`.

The `TransactionManager` has the following properties:

- **Transactionality:** If the operation completes successfully, the transaction will be committed.
  If the operation fails (throws an exception), the transaction will be rolled back.
  There is no way to roll the transaction back without throwing an exception.
- **Error handling:** Errors during the transaction "begin", "commit", and "rollback" phases
  should be avoided by the underlying implementations.
  Of course, it's not possible to guarantee a perfect success rate.
    - If an error occurs during the "begin" phase,
      outer transactions (if any) will be rolled back.
    - If an error occurs during the "commit" phase,
      outer transactions will be rolled back instead of committed.
      Inner transactions were already committed, and can no longer be rolled back.
      Data corruption is likely.
    - If an error occurs during the "rollback" phase,
      outer transactions will continue to be rolled back.
      Inner transactions were already rolled back.
      Data corruption is possible.
- **Nested transactions:** Nested transactions of the same type reuse the outer transaction.
  In the case of nested transactions of the same type,
  it's as if the inner transaction didn't even exist, and was a direct operation call instead.

To reiterate (since it is important),
data corruption is possible if the commit or rollback phases fail.

## Usage

### Step 1: Include the dependency

```kotlin
// build.gradle.kts

dependencies {
  testImplementation("kairo:kairo-transaction-manager:$kairoVersion")
}
```

### Step 2: Use the transaction manager

`SqlTransaction` is used as an example transaction type here,
but it could be any transaction type.

```kotlin
// src/main/kotlin/yourPackage/.../YourFile.kt

class YourFile @Inject constructor(
  private val transactionManager: TransactionManager,
  private val yourStore: YourStore,
) {
  fun deleteMember(memberId: KairoId) {
    transactionManager.transaction(SqlTransaction::class) {
      deleteLibraryCardsByMember(memberId)
      yourStore.deleteMember(memberId)
    }
  }

  fun deleteLibraryCardsByMember(memberId: KairoId) {
    yourStore.deleteLibraryCardsByMember(memberId)
  }
}
```
