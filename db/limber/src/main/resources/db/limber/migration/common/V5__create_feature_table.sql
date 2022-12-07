create table organization.feature
(
  guid       uuid
    constraint pkey__feature primary key,
  version    bigint      not null default 0,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create trigger on_update__feature
  before update
  on organization.feature
  for each row
execute procedure updated();

alter table organization.feature
  add column organization_guid uuid not null;
alter table organization.feature
  add constraint fk__feature__organization_guid
    foreign key (organization_guid)
      references organization.organization (guid)
      on delete cascade;
create index ix__feature__organization_guid
  on organization.feature (organization_guid);

alter table organization.feature
  add column is_default boolean not null;
create unique index uq__feature__is_default
  on organization.feature (organization_guid)
  where is_default is true;

alter table organization.feature
  add column type text not null;

alter table organization.feature
  add column name text not null;

alter table organization.feature
  add column icon_name text;

alter table organization.feature
  add column root_path text not null;
alter table organization.feature
  add constraint cs__feature__root_path
    check (lower(root_path) = root_path);
alter table organization.feature
  add constraint uq__feature__root_path
    unique (organization_guid, root_path);
