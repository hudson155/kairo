SELECT *
FROM auth.tenant
WHERE <conditions>
  AND archived_date IS NULL
