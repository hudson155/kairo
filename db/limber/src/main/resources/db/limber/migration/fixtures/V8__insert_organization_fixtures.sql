insert into organization.organization (guid, name)
values ('9d45d538-8131-44cc-aeba-9bef4e045d21', 'Acme Co.');

insert into organization.organization_auth (organization_guid, guid, auth0_organization_id)
values ('9d45d538-8131-44cc-aeba-9bef4e045d21', gen_random_uuid(), 'org_yDiVK18hoeddya8J');

insert into organization.organization_hostname (organization_guid, guid, hostname)
values ('9d45d538-8131-44cc-aeba-9bef4e045d21', gen_random_uuid(), 'localhost:3000');

insert into organization.feature (organization_guid, guid, is_default,
                                  type, name, icon_name, root_path)
values ((select guid from organization.organization), '9b76e07a-7e1e-4617-a184-35f3c64f6ce4', true,
        'Placeholder', 'Home', 'home', '/placeholder'),
       ((select guid from organization.organization), 'f16a3df1-cd27-4c0f-bd8e-0d7d4ab23046', false,
        'Form', 'My forms', 'assignment', '/forms');
