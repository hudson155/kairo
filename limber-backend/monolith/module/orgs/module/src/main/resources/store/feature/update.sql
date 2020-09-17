UPDATE orgs.feature
SET rank               = COALESCE(:rank, rank),
    name               = COALESCE(:name, name),
    path               = COALESCE(LOWER(:path), path),
    is_default_feature = COALESCE(:isDefaultFeature, is_default_feature)
WHERE org_guid = :orgGuid
  AND guid = :featureGuid
RETURNING *
