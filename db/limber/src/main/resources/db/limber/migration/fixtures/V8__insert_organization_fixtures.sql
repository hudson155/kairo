-- Acme Co.

insert into organization.organization (id, name)
values ('org_r7J81kjsgWsfjgQ1', 'Acme Co.');

insert into organization.organization_auth (id, organization_id, auth0_organization_id, auth0_organization_name)
values ('auth_vIWEcQk2a8M4eyou', 'org_r7J81kjsgWsfjgQ1', 'org_yDiVK18hoeddya8J', 'acme-co');

insert into organization.organization_hostname (id, organization_id, hostname)
values ('host_4Ny72pP64DTkfJJ6', 'org_r7J81kjsgWsfjgQ1', 'localhost:3000');

insert into organization.feature (id, organization_id, is_default,
                                  type, name, icon_name, root_path)
values ('feat_bFZLLIs7WXmhLGqD', 'org_r7J81kjsgWsfjgQ1', true,
        'Placeholder', 'Home', 'home', '/placeholder'),
       ('feat_MwIGd1VjxifchuKp', 'org_r7J81kjsgWsfjgQ1', false,
        'Form', 'My forms', 'assignment', '/forms');

-- Universal Exports.

insert into organization.organization (id, name)
values ('org_q1mtKttEjzOtdNoR', 'Universal Exports');

insert into organization.organization_auth (id, organization_id, auth0_organization_id, auth0_organization_name)
values ('auth_ECBppaQgMOSmBdhL', 'org_q1mtKttEjzOtdNoR', 'org_Ro0Fat17RkcY4ldL', 'universal-exports');

insert into organization.organization_hostname (id, organization_id, hostname)
values ('host_PnfJj2yfBUwZAXRX', 'org_q1mtKttEjzOtdNoR', 'localhost:3001');

insert into organization.feature (id, organization_id, is_default,
                                  type, name, icon_name, root_path)
values ('feat_OSPYVpqEVwGfK3Fz', 'org_q1mtKttEjzOtdNoR', true,
        'Form', 'Exports', 'assignment', '/exports');
