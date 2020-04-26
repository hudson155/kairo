SELECT *
FROM forms.form_instance_question
WHERE form_instance_guid = :formInstanceGuid
  AND question_guid = :questionGuid
