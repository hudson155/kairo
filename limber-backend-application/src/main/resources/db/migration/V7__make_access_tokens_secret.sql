ALTER TABLE auth.access_token
    RENAME COLUMN token TO encrypted_secret;
