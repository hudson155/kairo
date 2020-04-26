SELECT *
FROM forms.form_template_question
WHERE form_template_guid = :formTemplateGuid
  AND guid = :guid
