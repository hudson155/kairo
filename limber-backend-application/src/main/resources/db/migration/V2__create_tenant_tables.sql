CREATE TABLE auth.tenant
(
    id              BIGSERIAL PRIMARY KEY,
    created_date    TIMESTAMP      NOT NULL,
    org_guid        UUID UNIQUE    NOT NULL,
    auth0_client_id VARCHAR UNIQUE NOT NULL
);

CREATE TABLE auth.tenant_domain
(
    id           BIGSERIAL PRIMARY KEY,
    created_date TIMESTAMP NOT NULL,
    org_guid     UUID      NOT NULL REFERENCES auth.tenant (org_guid) ON DELETE CASCADE,
    domain       VARCHAR   NOT NULL
);

CREATE UNIQUE INDEX ON auth.tenant_domain (LOWER(domain));
