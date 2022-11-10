insert into organization.organization (guid, name)
values (gen_random_uuid(), 'Acme Co.');

insert into organization.organization_auth (organization_guid, guid, auth0_organization_id)
values ((select guid from organization.organization), gen_random_uuid(), 'org_yDiVK18hoeddya8J');

insert into organization.organization_hostname (organization_guid, guid, hostname)
values ((select guid from organization.organization), gen_random_uuid(), 'localhost:3000');

insert into organization.feature (organization_guid, guid, is_default, type, name, root_path)
values ((select guid from organization.organization), gen_random_uuid(), true, 'Placeholder', 'Home', '/placeholder'),
       ((select guid from organization.organization), gen_random_uuid(), false, 'Form', 'My forms', '/forms');
