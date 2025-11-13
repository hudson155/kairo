# MailerSend

Interface for [MailerSend](https://www.mailersend.com/),
letting you easily send emails!

## Installation

Install `kairo-mailersend-feature`.
You don't need to install the MailerSend SDK separately —
it's included by default.

```kotlin
// build.gradle.kts

dependencies {
  implementation("software.airborne.kairo:kairo-mailersend-feature")
}
```

## Usage

First, add the Feature to your Server.

```kotlin
val features = listOf(
  MailersendFeature(config.mailersend),
)
```

We recommend using [kairo-config](../kairo-config/README.md) to configure the Feature.

```hocon
mailersend {
  apiToken = ${MAILERSEND_API_TOKEN}
  templates {
    "library-book-created" = "o65qngkm2kdlwr12"
  }
}
```

Now send an email!

```kotlin
val mailer: Mailer // Inject this.

val email = Email().apply {
  setFrom("Kairo Sample", "kairo-sample@airborne.software")
  addRecipient("Jeff Hudson", "jeff@example.com")
  setTemplateId(mailer.templates.getValue("library-book-created"))
  setSubject("New library book: ${libraryBook.title}")
  addPersonalization("library_book_title", libraryBook.title)
  addPersonalization("library_book_authors", libraryBook.authors.joinToString("\n"))
  addPersonalization("library_book_isbn", libraryBook.isbn)
}
mailer.use { emails().send(email) }
```
