INSERT INTO orgs.feature (guid, created_date, org_guid, rank, name, path, type, is_default_feature)
VALUES (:guid, :createdDate, :orgGuid, :rank, :name, LOWER(:path), :type, :isDefaultFeature)
RETURNING *
