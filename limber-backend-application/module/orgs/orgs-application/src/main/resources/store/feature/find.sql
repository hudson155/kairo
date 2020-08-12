SELECT *
FROM orgs.feature
WHERE <conditions>
  AND archived_date IS NULL
ORDER BY rank
