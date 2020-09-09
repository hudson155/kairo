ALTER TABLE users.account
    DROP COLUMN name;

ALTER TABLE users.user
    ALTER COLUMN first_name
        DROP NOT NULL;

ALTER TABLE users.user
    ALTER COLUMN last_name
        DROP NOT NULL;
