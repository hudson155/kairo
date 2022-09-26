create function updated()
    returns trigger as
$$
begin
    new.updated_at = now();
    return new;
end;
$$ language 'plpgsql';
