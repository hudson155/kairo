create table organization.organization_hostname
(
  id         text
    constraint pkey__organization_hostname primary key,
  version    bigint      not null default 0,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create trigger on_update__organization_hostname
  before update
  on organization.organization_hostname
  for each row
execute procedure updated();

alter table organization.organization_hostname
  add column organization_id text not null;
alter table organization.organization_hostname
  add constraint fk__organization_hostname__organization_id
    foreign key (organization_id)
      references organization.organization (id)
      on delete cascade;
create index ix__organization_hostname__organization_id
  on organization.organization_hostname (organization_id);

alter table organization.organization_hostname
  add column hostname text not null;
alter table organization.organization_hostname
  add constraint cs__organization_hostname__hostname
    check (lower(hostname) = hostname);
alter table organization.organization_hostname
  add constraint uq__organization_hostname__hostname
    unique (hostname);
