INSERT INTO users.user (guid, created_date, identity_provider, superuser, org_guid, email_address,
                        first_name, last_name, profile_photo_url)
VALUES (:guid, :createdDate, :identityProvider, :superuser, :orgGuid, :emailAddress,
        :firstName, :lastName, :profilePhotoUrl)
RETURNING *
