-- Create Auth0 account and access token.
INSERT INTO users.account (created_date, guid, name, identity_provider, superuser)
VALUES (NOW(), 'fcef16c1-d994-4dd5-b2ea-b972d817a29d', 'Auth0', TRUE, FALSE);
INSERT INTO auth.access_token (created_date, guid, account_guid,
                               encrypted_secret)
VALUES (NOW(), 'd92d4629-1286-450a-9e89-cccebed3b373', 'fcef16c1-d994-4dd5-b2ea-b972d817a29d',
        '$2a$12$3O4rmdax7Ai8Qe30YXCFi.jhAE7IqY1AcgMQWil/hke0RWUUUgWsy');

-- Create Limber org and tenants.
INSERT INTO orgs.org (created_date, guid, name, owner_account_guid)
VALUES (NOW(), '5761db85-7701-4ce4-9162-ab0fb4149b0d', 'Limber', '3e2d1681-a666-456e-a168-647d8c3a3150');
INSERT INTO orgs.feature (created_date, guid, org_guid,
                          name, path, type, is_default_feature)
VALUES (NOW(), '75a2ed7a-4247-4e63-ab10-a60df3d9aeee', '5761db85-7701-4ce4-9162-ab0fb4149b0d',
        'Home', '/home', 'HOME', TRUE),
       (NOW(), '3dc95c5d-767c-4b29-9c50-a6f93edd0c06', '5761db85-7701-4ce4-9162-ab0fb4149b0d',
        'Forms', '/forms', 'FORMS', FALSE);
INSERT INTO auth.tenant (created_date, org_guid, auth0_client_id)
VALUES (NOW(), '5761db85-7701-4ce4-9162-ab0fb4149b0d', 'eXqVXnBUsRkvDv2nTv9hURTA2IHzNWDa');
INSERT INTO auth.tenant_domain (created_date, org_guid, domain)
VALUES (NOW(), '5761db85-7701-4ce4-9162-ab0fb4149b0d', 'localhost:3000'),
       (NOW(), '5761db85-7701-4ce4-9162-ab0fb4149b0d', 'localhost:8080'),
       (NOW(), '5761db85-7701-4ce4-9162-ab0fb4149b0d', 'limberapp.io');

-- Create user accounts.
INSERT INTO users.user (created_date, guid, identity_provider, superuser, org_guid,
                        name, first_name, last_name, email_address,
                        profile_photo_url)
VALUES (NOW(), '3e2d1681-a666-456e-a168-647d8c3a3150', FALSE, TRUE, '5761db85-7701-4ce4-9162-ab0fb4149b0d',
        'Jeff Hudson', 'Jeff', 'Hudson', 'jeff.hudson@limberapp.io',
        'https://lh6.googleusercontent.com/-FW2t5iZWAcg/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rczJ7skwxHKEbvhG5MiQXYnamb0eg/photo.jpg'),
       (NOW(), '71fe66b2-f115-43b9-a993-cbeb51c1b46a', FALSE, TRUE, '5761db85-7701-4ce4-9162-ab0fb4149b0d',
        'Noah Guld', 'Noah', 'Guld', 'nguld12@gmail.com',
        'https://avatars0.githubusercontent.com/u/8917186?s=460&u=364b0d5270cb9657b4222c0816713831805957c9&v=4');

-- Create vehicle inspection form.
INSERT INTO forms.form_template (created_date, guid, feature_guid,
                                 title, description)
VALUES (NOW(), '85ae9d91-2666-4174-bdfe-1f7cd868869c', '3dc95c5d-767c-4b29-9c50-a6f93edd0c06',
        'Vehicle Inspection', NULL);
INSERT INTO forms.form_template_question (created_date, guid, form_template_guid, rank,
                                          label, help_text, type, multi_line, placeholder, validator, earliest, latest)
VALUES (NOW(), '59c0b82c-57d3-4d29-b7ef-0bbf90084d7d', '85ae9d91-2666-4174-bdfe-1f7cd868869c', 0,
        'Worker name', NULL, 'TEXT', FALSE, NULL, NULL, NULL, NULL),
       (NOW(), '2a355fc7-33a6-44af-b737-ffa874b2b1a0', '85ae9d91-2666-4174-bdfe-1f7cd868869c', 1,
        'Date', NULL, 'DATE', NULL, NULL, NULL, NULL, NULL),
       (NOW(), '43f20f0d-03c0-4477-808d-8cfefefb46bc', '85ae9d91-2666-4174-bdfe-1f7cd868869c', 2,
        'Description', NULL, 'TEXT', TRUE, NULL, NULL, NULL, NULL);
INSERT INTO forms.form_instance (created_date, guid, feature_guid,
                                 form_template_guid)
VALUES (NOW(), '8e6ff3f9-26a0-42e4-935b-100c0327ab79', '3dc95c5d-767c-4b29-9c50-a6f93edd0c06',
        '85ae9d91-2666-4174-bdfe-1f7cd868869c'),
       (NOW(), '168af0c7-1ab1-40f6-9715-c9e8c241442f', '3dc95c5d-767c-4b29-9c50-a6f93edd0c06',
        '85ae9d91-2666-4174-bdfe-1f7cd868869c');
INSERT INTO forms.form_instance_question (created_date, form_instance_guid, form_template_question_guid,
                                          type, text, date)
VALUES (NOW(), '8e6ff3f9-26a0-42e4-935b-100c0327ab79', '59c0b82c-57d3-4d29-b7ef-0bbf90084d7d',
        'TEXT', 'Summer Kavan', NULL),
       (NOW(), '8e6ff3f9-26a0-42e4-935b-100c0327ab79', '2a355fc7-33a6-44af-b737-ffa874b2b1a0',
        'DATE', NULL, '2020-01-28'),
       (NOW(), '8e6ff3f9-26a0-42e4-935b-100c0327ab79', '43f20f0d-03c0-4477-808d-8cfefefb46bc',
        'TEXT', 'I did this vehicle inspection on my birthday!', NULL),
       (NOW(), '168af0c7-1ab1-40f6-9715-c9e8c241442f', '59c0b82c-57d3-4d29-b7ef-0bbf90084d7d',
        'TEXT', 'Kanye West', NULL),
       (NOW(), '168af0c7-1ab1-40f6-9715-c9e8c241442f', '2a355fc7-33a6-44af-b737-ffa874b2b1a0',
        'DATE', NULL, '2020-02-01'),
       (NOW(), '168af0c7-1ab1-40f6-9715-c9e8c241442f', '43f20f0d-03c0-4477-808d-8cfefefb46bc',
        'TEXT', 'Serious vehicle inspection. No jokes here.', NULL);
