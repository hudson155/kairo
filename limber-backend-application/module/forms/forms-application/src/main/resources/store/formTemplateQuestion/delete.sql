DELETE
FROM forms.form_template_question
WHERE EXISTS(SELECT 1
             FROM forms.form_template
             WHERE feature_guid = :featureGuid
               AND guid = form_template_question.form_template_guid)
  AND form_template_guid = :formTemplateGuid
  AND guid = :questionGuid
