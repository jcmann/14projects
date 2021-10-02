delete from characters;
ALTER TABLE characters AUTO_INCREMENT = 1;
insert into characters (name, level, race, character_class)
    values("Rigby", 5, "Tabaxi", "Bard");
insert into characters (name, level, race, character_class)
    values("Tank", 2, "Kobold", "Monk");
insert into characters (name, level, race, character_class)
    values("Teeko", 15, "Dragonborn", "Paladin");

