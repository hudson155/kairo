CREATE TABLE auth.tenant
(
    created_date    TIMESTAMP NOT NULL,
    org_guid        UUID      NOT NULL,
    auth0_client_id VARCHAR   NOT NULL
);

CREATE UNIQUE INDEX uniq__tenant__org_guid
    ON auth.tenant (org_guid);

CREATE UNIQUE INDEX uniq__tenant__auth0_client_id
    ON auth.tenant (auth0_client_id);

CREATE TABLE auth.tenant_domain
(
    created_date TIMESTAMP NOT NULL,
    org_guid     UUID      NOT NULL REFERENCES auth.tenant (org_guid) ON DELETE CASCADE,
    domain       VARCHAR   NOT NULL
);

CREATE UNIQUE INDEX ON auth.tenant_domain (LOWER(domain));
