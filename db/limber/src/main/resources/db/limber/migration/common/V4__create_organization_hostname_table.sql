create table organization.organization_hostname
(
    guid       uuid
        constraint pkey__organization_hostname primary key,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create trigger on_update__organization_hostname
    before update
    on organization.organization_hostname
    for each row
execute procedure updated();

alter table organization.organization_hostname
    add column organization_guid uuid not null;
alter table organization.organization_hostname
    add constraint fk__organization_hostname__organization_guid
        foreign key (organization_guid)
            references organization.organization (guid)
            on delete cascade;
create index ix__organization_hostname__organization_guid
    on organization.organization_hostname (organization_guid);

alter table organization.organization_hostname
    add column hostname text not null;
alter table organization.organization_hostname
    add constraint cs__organization_hostname__hostname
        check (lower(hostname) = hostname);
alter table organization.organization_hostname
    add constraint uq__organization_hostname__hostname
        unique (hostname);
