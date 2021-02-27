INSERT INTO orgs.feature (guid, created_date, org_guid, name, path, type, rank, is_default_feature)
VALUES (:guid, :createdDate, :orgGuid, :name, :path, :type, :rank, :isDefaultFeature)
RETURNING *
