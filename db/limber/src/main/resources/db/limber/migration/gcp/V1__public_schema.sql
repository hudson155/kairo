grant usage on schema public
  to limber_user_read;

alter default privileges in schema public
  grant select on tables to limber_user_read;

grant select on all tables in schema public to limber_user_read;

alter default privileges in schema public
  grant insert, update, delete on tables to limber_user_write;

grant insert, update, delete on all tables in schema public to limber_user_write;
