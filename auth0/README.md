# Auth0

## Setup

```postgresql
INSERT INTO users.account (created_date, guid, name, identity_provider, superuser)
VALUES (NOW(), 'fcef16c1-d994-4dd5-b2ea-b972d817a29d', 'Auth0', TRUE, FALSE);

INSERT INTO auth.access_token (created_date, guid, account_guid, token)
VALUES (NOW(), 'd92d4629-1286-450a-9e89-cccebed3b373', 'fcef16c1-d994-4dd5-b2ea-b972d817a29d', 'AZtaMsmWRo66gCYMUO4i8w==');
```
