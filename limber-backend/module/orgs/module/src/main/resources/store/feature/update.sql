UPDATE orgs.feature
SET name               = COALESCE(:name, name),
    path               = COALESCE(:path, path),
    rank               = COALESCE(:rank, rank),
    is_default_feature = COALESCE(:isDefaultFeature, is_default_feature)
WHERE guid = :featureGuid
RETURNING *
