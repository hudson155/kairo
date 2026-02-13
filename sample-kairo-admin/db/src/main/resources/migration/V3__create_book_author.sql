CREATE TABLE IF NOT EXISTS book_author (
  library_book_id TEXT NOT NULL REFERENCES library_book(id) ON DELETE CASCADE,
  author_id TEXT NOT NULL REFERENCES author(id) ON DELETE CASCADE,
  PRIMARY KEY (library_book_id, author_id)
);

CREATE INDEX IF NOT EXISTS ix__book_author__author_id ON book_author (author_id);
