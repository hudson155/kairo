UPDATE orgs.feature
SET is_default_feature = FALSE
WHERE org_guid = :orgGuid
