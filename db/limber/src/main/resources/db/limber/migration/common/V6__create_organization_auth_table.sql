create table organization.organization_auth
(
  guid       uuid
    constraint pkey__organization_auth primary key,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create trigger on_update__organization_auth
  before update
  on organization.organization_auth
  for each row
execute procedure updated();

alter table organization.organization_auth
  add column organization_guid uuid not null;
alter table organization.organization_auth
  add constraint fk__organization_auth__organization_guid
    foreign key (organization_guid)
      references organization.organization (guid)
      on delete cascade;
alter table organization.organization_auth
  add constraint uq__organization_auth__organization_guid
    unique (organization_guid);

alter table organization.organization_auth
  add column auth0_organization_id text not null;
alter table organization.organization_auth
  add constraint uq__organization_auth__auth0_organization_id
    unique (auth0_organization_id);
