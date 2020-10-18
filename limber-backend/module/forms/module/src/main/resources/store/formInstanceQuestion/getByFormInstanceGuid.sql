SELECT *
FROM forms.form_instance_question
WHERE (SELECT feature_guid
       FROM forms.form_instance
       WHERE guid = form_instance_guid) = :featureGuid
  AND form_instance_guid = :formInstanceGuid
