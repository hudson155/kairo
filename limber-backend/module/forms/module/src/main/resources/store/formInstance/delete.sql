DELETE
FROM forms.form_instance
WHERE feature_guid = :featureGuid
  AND guid = :formInstanceGuid
