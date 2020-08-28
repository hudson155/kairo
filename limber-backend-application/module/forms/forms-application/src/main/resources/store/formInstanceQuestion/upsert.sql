INSERT INTO forms.form_instance_question (created_date,
                                          form_instance_guid,
                                          question_guid, type, text, date, selections)
VALUES (:createdDate,
        (SELECT guid FROM forms.form_instance WHERE feature_guid = :featureGuid AND guid = :formInstanceGuid),
        :questionGuid, :type, :text, :date, :selections)
ON CONFLICT (form_instance_guid, question_guid) DO UPDATE
    SET text       = COALESCE(:text, form_instance_question.text),
        date       = COALESCE(:date, form_instance_question.date),
        selections = COALESCE(:selections, form_instance_question.selections)
WHERE EXISTS(SELECT 1
             FROM forms.form_instance
             WHERE feature_guid = :featureGuid
               AND guid = form_instance_question.form_instance_guid)
RETURNING *
