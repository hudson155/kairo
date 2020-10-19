SELECT *
FROM forms.form_template_question
WHERE (SELECT feature_guid FROM forms.form_template WHERE guid = form_template_guid) = :featureGuid
  AND form_template_guid = :formTemplateGuid
ORDER BY rank
