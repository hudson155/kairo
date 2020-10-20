# `limber-backend:common:finder`

This module defines the finder interface.
Implementing the `Finder` interface allows for dynamic querying of entities
(think: search/sort/filter).

Suppose there exists an entity called `JiraTicket`.
If `JiraTicketService` implements `Finder<JiraTicketModel, JiraTicketFinder>`,
the following is possible.
```kotlin
fun main(jiraTicketService: JiraTicketService) {
  val jiraTicket: JiraTicketModel = jiraTicketService.findOnlyOrThrow { ticketKey("ABC-123") }

  val jiraTicket: JiraTicketModel? = jiraTicketService.findOnlyOrNull { ticketKey("ABC-456") }

  val jiraTickets: List<JiraTicketModel> = jiraTicketService.findAsList {
    projectKey("ABC")
    sortBy(JiraTicketFinder.SortBy.TICKET_KEY, SortableFinder.SortDirection.ASCENDING)
  }

  val jiraTickets: Set<JiraTicketModel> = jiraTicketService.findAsSet {
    projectKey("ABC")
    creatorAccountGuid(jeffHudson)
    assigneeAccountGuid(noahGuld)
  }

  val jiraTicketExists: Boolean = jiraTicketService.has { ticketKey("ABC-789") }
}
```

### Example

Suppose you have an entity called `JiraTicket`,
and that you would like to allow users to query that entity using multiple fields,
and sort the results dynamically.

First, create an interface called `JiraTicketFinder`
in `io.limberapp.backend.module.model.jiraTicket`.
```kotlin
// If sorting is not required, simply implement Finder instead of SortableFinder.
interface JiraTicketFinder : SortableFinder<JiraTicketFinder.SortBy> {
  fun ticketKey(ticketKey: String)
  fun projectGuid(projectGuid: UUID)
  fun creatorAccountGuid(creatorAccountGuid: UUID)
  fun assigneeAccountGuid(assigneeAccountGuid: UUID)
  fun descriptionSearchString(descriptionSearchString: String)

  enum class SortBy { TICKET_KEY, CREATED_DATE, UPDATED_DATE }
}
```

Next, create a query builder called `JiraTicketQueryBuilder`
in `io.limberapp.backend.module.store.jiraTicket`.
```kotlin
internal class JiraTicketQueryBuilder : QueryBuilder(), JiraTicketFinder {
  override fun ticketKey(ticketKey: String) {
    conditions += "key = :ticketKey"
    bindings["ticketKey"] = ticketKey
  }

  override fun projectGuid(projectGuid: UUID) {
    conditions += "project_guid = :projectGuid"
    bindings["projectGuid"] = projectGuid
  }

  override fun creatorAccountGuid(creatorAccountGuid: UUID) {
    conditions += "creator_account_guid = :creatorAccountGuid"
    bindings["creatorAccountGuid"] = creatorAccountGuid
  }

  override fun assigneeAccountGuid(assigneeAccountGuid: UUID) {
    conditions += "assignee_account_guid = :assigneeAccountGuid"
    bindings["assigneeAccountGuid"] = assigneeAccountGuid
  }

  // This naive search implementation is intended to demonstrate the query builder functionality,
  // not to suggest best practices for searching.
  override fun descriptionSearchString(descriptionSearchString: String) {
    conditions += "LOWER(description) LIKE '%' + LOWER(:descriptionSearchString) + '%'"
    bindings["descriptionSearchString"] = descriptionSearchString
  }

  override fun sortBy(sortBy: JiraTicketFinder.SortBy, sortDirection: SortableFinder.SortDirection) {
    val columnName = when (sortBy) {
      JiraTicketFinder.SortBy.TICKET_KEY -> "key"
      JiraTicketFinder.SortBy.CREATED_DATE -> "created_date"
      JiraTicketFinder.SortBy.UPDATED_DATE -> "updated_date"
    }
    sorts += Pair(columnName, sortDirection == SortableFinder.SortDirection.ASCENDING)
  }
}
```

To enable finder functionality at the store layer,
have `JiraTicketStore` implement `Finder<JiraTicketModel, JiraTicketFinder>`.
```kotlin
override fun <R> find(result: (Iterable<JiraTicketModel>) -> R, query: JiraTicketFinder.() -> Unit): R =
    withHandle { handle ->
      handle.createQuery(sqlResource("/store/jiraTicket/find.sql"))
          .withFinder(JiraTicketQueryBuilder().apply(query))
          .mapTo(JiraTicketModel::class.java)
          .let(result)
    }
```

And to enable finder functionality at the service layer,
have `JiraTicketService` implement `Finder<JiraTicketModel, JiraTicketFinder>` as well.
`JiraTicketServiceImpl` can delegate its implementation to the store layer using
```kotlin
internal class FormInstanceServiceImpl @Inject constructor(
    // ...
    private val formInstanceStore: FormInstanceStore,
) : FormInstanceService, Finder<FormInstanceModel, FormInstanceFinder> by formInstanceStore
```
