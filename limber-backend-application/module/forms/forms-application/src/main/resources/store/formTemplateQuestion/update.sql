UPDATE forms.form_template_question
SET label       = COALESCE(:label, label),
    help_text   = COALESCE(:helpText, help_text),
    required    = COALESCE(:required, required),
    multi_line  = COALESCE(:multiLine, multi_line),
    placeholder = COALESCE(:placeholder, placeholder),
    validator   = COALESCE(:validator, validator),
    earliest    = COALESCE(:earliest, earliest),
    latest      = COALESCE(:latest, latest),
    options     = COALESCE(:options, options)
WHERE EXISTS(SELECT 1 FROM forms.form_template WHERE feature_guid = :featureGuid AND guid = form_template_guid)
  AND form_template_guid = :formTemplateGuid
  AND guid = :questionGuid
RETURNING *
