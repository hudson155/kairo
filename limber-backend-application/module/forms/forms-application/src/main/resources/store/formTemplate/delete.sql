UPDATE forms.form_template
SET archived_date = NOW()
WHERE feature_guid = :featureGuid
  AND guid = :formTemplateGuid
  AND archived_date IS NULL
