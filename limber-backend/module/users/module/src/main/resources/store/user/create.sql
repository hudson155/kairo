INSERT INTO users.user (guid, created_date, permissions, org_guid, email_address,
                        first_name, last_name, profile_photo_url)
VALUES (:guid, :createdDate, :permissions, :orgGuid, :emailAddress,
        :firstName, :lastName, :profilePhotoUrl)
RETURNING *
