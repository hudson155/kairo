create table organization.organization
(
  id         text
    constraint pkey__organization primary key,
  version    bigint      not null default 0,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create trigger on_update__organization
  before update
  on organization.organization
  for each row
execute procedure updated();

alter table organization.organization
  add column name text not null;
