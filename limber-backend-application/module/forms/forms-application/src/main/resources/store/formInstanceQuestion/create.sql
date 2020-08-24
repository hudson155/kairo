INSERT INTO forms.form_instance_question (created_date,
                                          form_instance_guid,
                                          question_guid, type, text, date, selections)
VALUES (:createdDate,
        (SELECT guid FROM forms.form_instance WHERE feature_guid = :featureGuid AND guid = :formInstanceGuid),
        :questionGuid, :type, :text, :date, :selections)
RETURNING *
