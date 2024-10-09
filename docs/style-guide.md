# Style guide

Other than the rules defined here, please follow the
[Google Style Guide](https://developers.google.com/style).

- **Product terminology:**
  Treat Kairo _Features_ and _Servers_ as proper nouns (the first letter should be capitalized).

- **Sentence case:**
  Use American English style for general capitalization.
  Use sentence case in all headings, titles, and navigation.
  This includes for user-facing copy and within code and documentation.
  [This is consistent with the Google Style Guide](https://developers.google.com/style/text-formatting).

- **Full sentences:**
  Use full sentences, ending with proper punctuation.
  This applies to both copy and to code comments and logs.
  It also applies within bulleted and numbered lists.

- **Ordering:**
  - Order members in code by **read-create-update-delete** where possible.
  - Order lists alphabetically when there is no other natural order.
    This applies to both copy and to code (such as parameter/method lists and documentation).

- **Plurality:**
  For naming, singular forms should be preferred over plural forms where possible.
  This includes for folder, package, and feature names.
  REST paths should be plural, due to convention.

- **Abbreviations:**
  In general, abbreviations should be avoided.
  Specifically, _i.e._ and _e.g._ should be avoided.

- **Non-null:**
  Prefer the term "non-null" to "not null".

- **Get vs. list vs. search:**
  The term "get" should be used when fetching a particular entity.
  The term "list" should be used when fetching all entities matching a simple condition.
  The term "search" should be used when fetching all entities matching some filter(s).
