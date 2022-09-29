update <tableName>
set document = :document
where guid = :guid
returning guid, document
