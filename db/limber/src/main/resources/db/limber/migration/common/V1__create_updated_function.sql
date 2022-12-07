create function updated()
  returns trigger as
$$
begin
  new.version = old.version + 1;
  new.updated_at = now();
  return new;
end;
$$ language 'plpgsql';
