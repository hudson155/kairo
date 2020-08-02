SELECT *
FROM forms.form_template
WHERE feature_guid = :featureGuid
  AND archived_date IS NULL
