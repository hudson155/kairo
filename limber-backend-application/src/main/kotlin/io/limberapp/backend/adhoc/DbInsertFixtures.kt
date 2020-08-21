package io.limberapp.backend.adhoc

import com.piperframework.module.SqlWrapper
import io.limberapp.backend.adhoc.helper.dbConfig

fun main(args: Array<String>) {
  val config = dbConfig(args[0], args.getOrNull(1), args.getOrNull(2))
  with(SqlWrapper(config)) {
    connect()
    insertFixtures()
    disconnect()
  }
}

private const val SQL = """
-- Create Limber org and tenants.
INSERT INTO orgs.org (guid, created_date, name,
                      owner_account_guid)
VALUES ('46c25689-71d6-4544-8c8c-6936d761dddb', NOW() AT TIME ZONE 'UTC', 'Limber',
        '3e2d1681-a666-456e-a168-647d8c3a3150');
INSERT INTO auth.org_role (guid, created_date, org_guid,
                           name, permissions)
VALUES ('b9ce1942-897d-470f-8b68-822d901005a6', NOW() AT TIME ZONE 'UTC', '46c25689-71d6-4544-8c8c-6936d761dddb',
        'Admin', '1100'),
       ('e7b681af-812d-4999-a016-ebb3d6f23104', NOW() AT TIME ZONE 'UTC', '46c25689-71d6-4544-8c8c-6936d761dddb',
        'Member', '0010');
INSERT INTO orgs.feature (guid, created_date, org_guid,
                          rank, name, path, type, is_default_feature)
VALUES ('75a2ed7a-4247-4e63-ab10-a60df3d9aeee', NOW() AT TIME ZONE 'UTC', '46c25689-71d6-4544-8c8c-6936d761dddb',
        0, 'Home', '/home', 'HOME', TRUE),
       ('3dc95c5d-767c-4b29-9c50-a6f93edd0c06', NOW() AT TIME ZONE 'UTC', '46c25689-71d6-4544-8c8c-6936d761dddb',
        1, 'Forms', '/forms', 'FORMS', FALSE);
INSERT INTO auth.tenant (created_date, org_guid, auth0_client_id)
VALUES (NOW() AT TIME ZONE 'UTC', '46c25689-71d6-4544-8c8c-6936d761dddb', 'kwQNShDsyv7vAObSiBG12O46CMY7qr2Q');
INSERT INTO auth.tenant_domain (created_date, org_guid, domain)
VALUES (NOW() AT TIME ZONE 'UTC', '46c25689-71d6-4544-8c8c-6936d761dddb', 'localhost:3000'),
       (NOW() AT TIME ZONE 'UTC', '46c25689-71d6-4544-8c8c-6936d761dddb', 'localhost:8080'),
       (NOW() AT TIME ZONE 'UTC', '46c25689-71d6-4544-8c8c-6936d761dddb', 'limber.limberapp.io');

-- Create user accounts.
INSERT INTO users.user (guid, created_date, identity_provider, superuser,
                        org_guid, name, first_name, last_name, email_address,
                        profile_photo_url)
VALUES ('3e2d1681-a666-456e-a168-647d8c3a3150', NOW() AT TIME ZONE 'UTC', FALSE, TRUE,
        '46c25689-71d6-4544-8c8c-6936d761dddb', 'Jeff Hudson', 'Jeff', 'Hudson', 'jeff.hudson@limberapp.io',
        'https://avatars0.githubusercontent.com/u/1360420?s=460&u=701cdf42f8638e94e4fd374efe5416638a608074&v=4'),
       ('71fe66b2-f115-43b9-a993-cbeb51c1b46a', NOW() AT TIME ZONE 'UTC', FALSE, TRUE,
        '46c25689-71d6-4544-8c8c-6936d761dddb', 'Noah Guld', 'Noah', 'Guld', 'nguld12@gmail.com',
        'https://avatars0.githubusercontent.com/u/8917186?s=460&u=364b0d5270cb9657b4222c0816713831805957c9&v=4');
INSERT INTO auth.org_role_membership (created_date, org_role_guid, account_guid)
VALUES (NOW() AT TIME ZONE 'UTC', 'b9ce1942-897d-470f-8b68-822d901005a6', '3e2d1681-a666-456e-a168-647d8c3a3150'),
       (NOW() AT TIME ZONE 'UTC', 'e7b681af-812d-4999-a016-ebb3d6f23104', '3e2d1681-a666-456e-a168-647d8c3a3150'),
       (NOW() AT TIME ZONE 'UTC', 'e7b681af-812d-4999-a016-ebb3d6f23104', '71fe66b2-f115-43b9-a993-cbeb51c1b46a');

-- Create vehicle inspection form.
INSERT INTO forms.form_template (guid, created_date, feature_guid,
                                 title, description)
VALUES ('85ae9d91-2666-4174-bdfe-1f7cd868869c', NOW() AT TIME ZONE 'UTC', '3dc95c5d-767c-4b29-9c50-a6f93edd0c06',
        'Vehicle Inspection', NULL),
        ('3e8e966d-613f-4bf9-936a-2b1e3c6ab3f0', NOW() AT TIME ZONE 'UTC', '3dc95c5d-767c-4b29-9c50-a6f93edd0c06',
        'Pre-Flight Safety Inspection', NULL);
INSERT INTO forms.form_template_question (guid, created_date, form_template_guid, rank,
                                          label, help_text, required, type, 
                                          multi_line, placeholder, validator, earliest, latest, options)
VALUES ('59c0b82c-57d3-4d29-b7ef-0bbf90084d7d', NOW() AT TIME ZONE 'UTC', '85ae9d91-2666-4174-bdfe-1f7cd868869c', 0,
        'Worker name', NULL, TRUE, 'TEXT',
         FALSE, NULL, NULL, NULL, NULL,NULL),
       ('2a355fc7-33a6-44af-b737-ffa874b2b1a0', NOW() AT TIME ZONE 'UTC', '85ae9d91-2666-4174-bdfe-1f7cd868869c', 1,
        'Date', NULL, TRUE, 'DATE',
         NULL, NULL, NULL, NULL, NULL, NULL),
       ('43f20f0d-03c0-4477-808d-8cfefefb46bc', NOW() AT TIME ZONE 'UTC', '85ae9d91-2666-4174-bdfe-1f7cd868869c', 2,
        'Description', NULL, FALSE, 'TEXT',
         TRUE, NULL, NULL, NULL, NULL, NULL),
       ('1dd86a5e-60bb-4257-9862-caa480ed5286', NOW() AT TIME ZONE 'UTC', '85ae9d91-2666-4174-bdfe-1f7cd868869c', 3,
        'Option', NULL, FALSE, 'RADIO_SELECTOR',
         NULL, NULL, NULL, NULL, NULL, '{"option 1", "option 2"}'),
       ('50c32a31-35c6-458e-b582-f3f8c9a019e4', NOW() AT TIME ZONE 'UTC', '3e8e966d-613f-4bf9-936a-2b1e3c6ab3f0', 0,
        'Pilot Name', NULL, TRUE, 'TEXT',
         FALSE, NULL, NULL, NULL, NULL, NULL),
       ('283167ce-a9fe-4de5-9042-ac1707e8f8dc', NOW() AT TIME ZONE 'UTC', '3e8e966d-613f-4bf9-936a-2b1e3c6ab3f0', 1,
        'Departure Date', NULL, TRUE, 'DATE',
         NULL, NULL, NULL, NULL, NULL, NULL),
       ('761dea1a-e422-4e3c-81c6-497ec21cc1c3', NOW() AT TIME ZONE 'UTC', '3e8e966d-613f-4bf9-936a-2b1e3c6ab3f0', 2,
        'Destination', NULL, TRUE, 'TEXT',
         TRUE, NULL, NULL, NULL, NULL, NULL),
       ('d9fbaa35-ffd6-4a66-8c26-7d40e32ac7aa', NOW() AT TIME ZONE 'UTC', '3e8e966d-613f-4bf9-936a-2b1e3c6ab3f0', 3,
        'Plane', NULL, FALSE, 'RADIO_SELECTOR',
         NULL, NULL, NULL, NULL, NULL, '{"HondaJet", "SR20", "Challenger"}');
INSERT INTO forms.form_instance (guid, created_date, feature_guid,
                                 form_template_guid, number, submitted_date, creator_account_guid)
VALUES ('8e6ff3f9-26a0-42e4-935b-100c0327ab79', NOW() AT TIME ZONE 'UTC', '3dc95c5d-767c-4b29-9c50-a6f93edd0c06',
        '85ae9d91-2666-4174-bdfe-1f7cd868869c', 1, NOW() AT TIME ZONE 'UTC', '3e2d1681-a666-456e-a168-647d8c3a3150'),
       ('168af0c7-1ab1-40f6-9715-c9e8c241442f', NOW() AT TIME ZONE 'UTC', '3dc95c5d-767c-4b29-9c50-a6f93edd0c06',
        '85ae9d91-2666-4174-bdfe-1f7cd868869c', 2, NOW() AT TIME ZONE 'UTC', '71fe66b2-f115-43b9-a993-cbeb51c1b46a');
INSERT INTO forms.form_instance_question (created_date, form_instance_guid, question_guid,
                                          type, text, date)
VALUES (NOW() AT TIME ZONE 'UTC', '8e6ff3f9-26a0-42e4-935b-100c0327ab79', '59c0b82c-57d3-4d29-b7ef-0bbf90084d7d',
        'TEXT', 'Summer Kavan', NULL),
       (NOW() AT TIME ZONE 'UTC', '8e6ff3f9-26a0-42e4-935b-100c0327ab79', '2a355fc7-33a6-44af-b737-ffa874b2b1a0',
        'DATE', NULL, '2020-01-28'),
       (NOW() AT TIME ZONE 'UTC', '8e6ff3f9-26a0-42e4-935b-100c0327ab79', '43f20f0d-03c0-4477-808d-8cfefefb46bc',
        'TEXT', 'I did this vehicle inspection on my birthday!', NULL),
       (NOW() AT TIME ZONE 'UTC', '168af0c7-1ab1-40f6-9715-c9e8c241442f', '59c0b82c-57d3-4d29-b7ef-0bbf90084d7d',
        'TEXT', 'Kanye West', NULL),
       (NOW() AT TIME ZONE 'UTC', '168af0c7-1ab1-40f6-9715-c9e8c241442f', '2a355fc7-33a6-44af-b737-ffa874b2b1a0',
        'DATE', NULL, '2020-02-01'),
       (NOW() AT TIME ZONE 'UTC', '168af0c7-1ab1-40f6-9715-c9e8c241442f', '43f20f0d-03c0-4477-808d-8cfefefb46bc',
        'TEXT', 'Serious vehicle inspection. No jokes here.', NULL);
"""

internal fun SqlWrapper.insertFixtures() {
  with(checkNotNull(dataSource).connection) {
    createStatement().execute(SQL)
  }
}
