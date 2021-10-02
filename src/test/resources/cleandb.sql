delete from users;
alter table users auto_increment = 1;
insert into users (username, password)
    values("user1", "pass1");
insert into users (username, password)
    values("user2", "pass2");
insert into users (username, password)
    values("user3", "pass3");

delete from characters;
ALTER TABLE characters AUTO_INCREMENT = 1;
insert into characters (name, level, race, character_class, user_id)
    values("Rigby", 5, "Tabaxi", "Bard", 1);
insert into characters (name, level, race, character_class, user_id)
    values("Tank", 2, "Kobold", "Monk", 1);
insert into characters (name, level, race, character_class, user_id)
    values("Teeko", 15, "Dragonborn", "Paladin", 3);

