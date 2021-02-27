UPDATE orgs.feature
SET is_default_feature = FALSE
WHERE org_guid = (SELECT org_guid FROM orgs.feature WHERE guid = :featureGuid)
