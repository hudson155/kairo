INSERT INTO forms.form_template_question (guid, created_date,
                                          form_template_guid,
                                          rank, label, help_text, required,
                                          type, multi_line, placeholder, validator, earliest, latest, options)
VALUES (:guid, :createdDate,
        (SELECT guid FROM forms.form_template WHERE feature_guid = :featureGuid AND guid = :formTemplateGuid),
        :rank, :label, :helpText, :required,
        :type, :multiLine, :placeholder, :validator, :earliest, :latest, :options)
RETURNING *
