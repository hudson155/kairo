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
    org_guid     UUID      NOT NULL,
    domain       VARCHAR   NOT NULL
);

CREATE INDEX idx__tenant_domain__org_guid
    ON auth.tenant_domain (org_guid);

ALTER TABLE auth.tenant_domain
    ADD CONSTRAINT fk__tenant_domain__org_guid FOREIGN KEY (org_guid)
        REFERENCES auth.tenant (org_guid) ON DELETE CASCADE;

CREATE UNIQUE INDEX uniq__tenant_domain__domain
    ON auth.tenant_domain (LOWER(domain));
