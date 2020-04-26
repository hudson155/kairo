INSERT INTO orgs.feature (guid, created_date, org_guid, name, path, type, is_default_feature)
VALUES (:guid, :createdDate, :orgGuid, :name, LOWER(:path), :type, :isDefaultFeature)
