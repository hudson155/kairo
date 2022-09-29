insert into <tableName> (guid, document)
values (:guid, :document)
returning guid, document
