insert into organization.organization (guid, name)
values (:guid, :name)
returning *
