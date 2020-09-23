SELECT MAX(rank)
FROM forms.form_template_question
WHERE form_template_guid = :formTemplateGuid
