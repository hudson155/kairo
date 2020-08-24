DELETE
FROM forms.form_instance_question
WHERE EXISTS(SELECT 1 FROM forms.form_instance WHERE feature_guid = :featureGuid AND guid = form_instance_guid)
  AND form_instance_guid = :formInstanceGuid
  AND question_guid = :questionGuid
