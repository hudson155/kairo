INSERT INTO users.user (created_date, guid, name, identity_provider, superuser, org_guid, email_address,
                        first_name, last_name, profile_photo_url)
VALUES (:createdDate, :guid, :firstName || ' ' || :lastName, :identityProvider, :superuser, :orgGuid, :emailAddress,
        :firstName, :lastName, :profilePhotoUrl)
