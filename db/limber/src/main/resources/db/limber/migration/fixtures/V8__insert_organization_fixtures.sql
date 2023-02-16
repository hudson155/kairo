-- Acme Co.

insert into organization.organization (guid, name)
values ('9d45d538-8131-44cc-aeba-9bef4e045d21', 'Acme Co.');

insert into organization.organization_auth (guid, organization_guid, auth0_organization_id, auth0_organization_name)
values (gen_random_uuid(), '9d45d538-8131-44cc-aeba-9bef4e045d21', 'org_yDiVK18hoeddya8J', 'acme-co');

insert into organization.organization_hostname (guid, organization_guid, hostname)
values (gen_random_uuid(), '9d45d538-8131-44cc-aeba-9bef4e045d21', 'localhost:3000');

insert into organization.feature (guid, organization_guid, is_default,
                                  type, name, icon_name, root_path)
values (gen_random_uuid(), '9d45d538-8131-44cc-aeba-9bef4e045d21', true,
        'Placeholder', 'Home', 'home', '/placeholder'),
       (gen_random_uuid(), '9d45d538-8131-44cc-aeba-9bef4e045d21', false,
        'Form', 'My forms', 'assignment', '/forms');

-- Universal Exports.

insert into organization.organization (guid, name)
values ('15cf1689-6322-40a3-92cb-058834d1c8df', 'Universal Exports');

insert into organization.organization_auth (guid, organization_guid, auth0_organization_id, auth0_organization_name)
values (gen_random_uuid(), '15cf1689-6322-40a3-92cb-058834d1c8df', 'org_Ro0Fat17RkcY4ldL', 'universal-exports');

insert into organization.organization_hostname (guid, organization_guid, hostname)
values (gen_random_uuid(), '15cf1689-6322-40a3-92cb-058834d1c8df', 'localhost:3001');

insert into organization.feature (guid, organization_guid, is_default,
                                  type, name, icon_name, root_path)
values (gen_random_uuid(), '15cf1689-6322-40a3-92cb-058834d1c8df', true,
        'Form', 'Exports', 'assignment', '/exports');
