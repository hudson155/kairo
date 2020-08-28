UPDATE forms.form_template
SET title       = COALESCE(:title, title),
    description = COALESCE(:description, description)
WHERE feature_guid = :featureGuid
  AND guid = :formTemplateGuid
RETURNING *
