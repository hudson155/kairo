-- Authors
INSERT INTO author (id, name, bio) VALUES
  ('auth_001', 'J.R.R. Tolkien', 'English writer and philologist, best known for The Hobbit and The Lord of the Rings'),
  ('auth_002', 'Carl Sagan', 'American astronomer and science communicator known for popularizing astronomy'),
  ('auth_003', 'Doris Kearns Goodwin', 'American biographer and historian, recipient of the Pulitzer Prize'),
  ('auth_004', 'Jane Austen', 'English novelist known for her sharp social commentary and romantic fiction'),
  ('auth_005', 'Isaac Asimov', 'American writer and professor of biochemistry, prolific author of science fiction'),
  ('auth_006', 'Ursula K. Le Guin', 'American author best known for her works of speculative and science fiction'),
  ('auth_007', 'David McCullough', 'American historian, narrator, and two-time Pulitzer Prize winner'),
  ('auth_008', 'Nicholas Sparks', 'American novelist and screenwriter known for his romance novels');

-- Books
INSERT INTO library_book (id, title, isbn, genre) VALUES
  ('book_001', 'The Hobbit', '978-0-618-00221-3', 'Fantasy'),
  ('book_002', 'The Silmarillion', '978-0-618-39111-2', 'Fantasy'),
  ('book_003', 'Cosmos', '978-0-345-53943-4', 'Science'),
  ('book_004', 'Team of Rivals', '978-0-7432-7075-5', 'History'),
  ('book_005', 'Pride and Prejudice', '978-0-14-143951-8', 'Romance'),
  ('book_006', 'Foundation', '978-0-553-29335-7', 'ScienceFiction'),
  ('book_007', 'A Wizard of Earthsea', '978-0-547-72202-0', 'Fantasy'),
  ('book_008', 'The Left Hand of Darkness', '978-0-441-47812-5', 'ScienceFiction'),
  ('book_009', '1776', '978-0-7432-2658-4', 'History'),
  ('book_010', 'The Notebook', '978-0-446-60573-8', 'Romance');

-- Book-Author associations
INSERT INTO book_author (library_book_id, author_id) VALUES
  ('book_001', 'auth_001'),
  ('book_002', 'auth_001'),
  ('book_003', 'auth_002'),
  ('book_004', 'auth_003'),
  ('book_005', 'auth_004'),
  ('book_006', 'auth_005'),
  ('book_007', 'auth_006'),
  ('book_008', 'auth_006'),
  ('book_009', 'auth_007'),
  ('book_010', 'auth_008');
