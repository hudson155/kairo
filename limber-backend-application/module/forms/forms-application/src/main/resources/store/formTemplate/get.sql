SELECT *
FROM forms.form_template
WHERE feature_guid = :featureGuid
  AND guid = :formTemplateGuid
  AND archived_date IS NULL
