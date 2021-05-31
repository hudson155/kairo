INSERT INTO users.user (guid, created_date, permissions, org_guid, email_address,
                        full_name, profile_photo_url)
VALUES (:guid, :createdDate, :permissions, :orgGuid, :emailAddress,
        :fullName, :profilePhotoUrl)
RETURNING *
