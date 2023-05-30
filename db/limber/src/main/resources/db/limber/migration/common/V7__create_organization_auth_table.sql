create table organization.organization_auth
(
  id         text
    constraint pkey__organization_auth primary key,
  version    bigint      not null default 0,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create trigger on_update__organization_auth
  before update
  on organization.organization_auth
  for each row
execute procedure updated();

alter table organization.organization_auth
  add column organization_id text not null;
alter table organization.organization_auth
  add constraint fk__organization_auth__organization_id
    foreign key (organization_id)
      references organization.organization (id)
      on delete cascade;
alter table organization.organization_auth
  add constraint uq__organization_auth__organization_id
    unique (organization_id);

alter table organization.organization_auth
  add column auth0_organization_id text;
alter table organization.organization_auth
  add constraint uq__organization_auth__auth0_organization_id
    unique (auth0_organization_id);

alter table organization.organization_auth
  add column auth0_organization_name text not null;
alter table organization.organization_auth
  add constraint cs__organization_auth__auth0_organization_name
    check (lower(auth0_organization_name) = auth0_organization_name);
alter table organization.organization_auth
  add constraint uq__organization_auth__auth0_organization_name
    unique (auth0_organization_name);
