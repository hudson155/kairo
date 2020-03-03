CREATE TABLE auth.tenant
(
    id              BIGSERIAL PRIMARY KEY,
    created_date    TIMESTAMP      NOT NULL,
    domain          VARCHAR UNIQUE NOT NULL,
    org_guid        UUID           NOT NULL,
    auth0_client_id VARCHAR        NOT NULL
);
