-- Localized texts
DROP TABLE IF EXISTS _translation;
CREATE TABLE _translation (
    _id INTEGER NOT NULL,
    _language CHAR(3) NOT NULL, -- ISO 639-2/5 language code, upper case
    _text TEXT,
    PRIMARY KEY (_id, _language)
);

-- tiny, small, medium, large, huge, gargantuan
DROP TABLE IF EXISTS _size;
CREATE TABLE _size (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _description_id INTEGER,
    FOREIGN KEY (_description_id) REFERENCES _translation(_id)
);

-- Player's Handbook, Dungeon Master's Guide, Elemental Evil Player's Companion, Adventurer's Guide,
-- fan-made, etc....
DROP TABLE IF EXISTS _source;
CREATE TABLE _source (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _description_id INTEGER,
    FOREIGN KEY (_description_id) REFERENCES _translation(_id)
);

-- human, dwarf, elf, halfling, etc....
DROP TABLE IF EXISTS _super_race;
CREATE TABLE _super_race (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _name_id INTEGER,
    _source_id INTEGER,
    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
   FOREIGN KEY (_source_id) REFERENCES _source(_id)
);
--INSERT INTO _super_race (_id, _name_id) VALUES (1, 7); -- Human

-- default human, variant human, high elf, wood elf, drow, hills dwarf, etc....
DROP TABLE IF EXISTS _sub_race;
CREATE TABLE _sub_race (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _name_id INTEGER,
    _super_race_id INTEGER,
    _source_id INTEGER,
    _height TEXT,
    _weight TEXT,
    _size_id INTEGER,
    _adult_age_id INTEGER,
    _max_age_id INTEGER,
    _alignment_id INTEGER,
    _ability_id INTEGER,
    _speed INTEGER,
    _darkvision INTEGER,
    _languages_id INTEGER,
    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
    FOREIGN KEY (_super_race_id) REFERENCES _super_race(_id),
    FOREIGN KEY (_size_id) REFERENCES _size(_id),
    FOREIGN KEY (_source_id) REFERENCES _source(_id),
    FOREIGN KEY (_adult_age_id) REFERENCES _translation(_id),
    FOREIGN KEY (_max_age_id) REFERENCES _translation(_id),
    FOREIGN KEY (_alignment_id) REFERENCES _translation(_id),
    FOREIGN KEY (_ability_id) REFERENCES _translation(_id),
    FOREIGN KEY (_languages_id) REFERENCES _translation(_id)
);

-- child name, male name, female name, family/clan name, nickname
DROP TABLE IF EXISTS _name_type;
CREATE TABLE _name_type (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _description_id INTEGER,
    FOREIGN KEY (_description_id) REFERENCES _translation(_id)
);

-- name samples ...
DROP TABLE IF EXISTS _sample_name;
CREATE TABLE _sample_name (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _race_id INTEGER,
    _type_id INTEGER,
    _name TEXT,
    FOREIGN KEY (_race_id) REFERENCES _super_race(_id),
    FOREIGN KEY (_type_id) REFERENCES _name_type
);

-- generic table for feats, class' features, racial traits, etc.
DROP TABLE IF EXISTS _feature;
CREATE TABLE _feature (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _name_id INTEGER,
    _description_id INTEGER,
    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
    FOREIGN KEY (_description_id) REFERENCES _translation(_id)
);

-- N-N table to link races to their traits
DROP TABLE IF EXISTS _racial_trait;
CREATE TABLE _racial_trait (
    _sub_race_id INTEGER NOT NULL,
    _feature_id INTEGER NOT NULL,
    PRIMARY KEY (_sub_race_id, _feature_id)
);

-- classes
DROP TABLE IF EXISTS _class;
CREATE TABLE _class (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _name_id INTEGER,
    _hit_dice INTEGER,
    _armors_id INTEGER,
    _weapons_id INTEGER,
    _tools_id INTEGER,
    _saves_id INTEGER,
    _skills_id INTEGER,
    _multicl_prer_id INTEGER, -- prerequisites
    _multicl_prof_id INTEGER, -- proficiencies
    _money_id INTEGER, -- If your DM allows, you can choose to start with this amount of initial wealth. If you do, you doesn't receive any equipment from your class' initial equipment or your background.
    _equipment_id INTEGER,
    _source_id INTEGER,
    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
    FOREIGN KEY (_armors_id) REFERENCES _translation(_id),
    FOREIGN KEY (_weapons_id) REFERENCES _translation(_id),
    FOREIGN KEY (_tools_id) REFERENCES _translation(_id),
    FOREIGN KEY (_saves_id) REFERENCES _translation(_id),
    FOREIGN KEY (_skills_id) REFERENCES _translation(_id),
    FOREIGN KEY (_multicl_prer_id) REFERENCES _translation(_id),
    FOREIGN KEY (_multicl_prof_id) REFERENCES _translation(_id),
    FOREIGN KEY (_money_id) REFERENCES _translation(_id),
    FOREIGN KEY (_equipment_id) REFERENCES _translation(_id),
    FOREIGN KEY (_source_id) REFERENCES _source(_id)
);

---- classes
--DROP TABLE IF EXISTS _spellcasting;
--CREATE TABLE _spellcasting (
--    _id INTEGER PRIMARY KEY AUTOINCREMENT,
--    _class_id INTEGER,
--    _cantrips INTEGER, -- Starts with how many cantrips known?
--    _spells INTEGER, -- Starts with how many spells known? (-1 for all, ex: druids, clerics and paladins)
--    _list_id INTEGER,
--    _prepare INTEGER,
--    _ability_id INTEGER,
--    _focus_id INTEGER
--);

DROP TABLE IF EXISTS _class_option;
CREATE TABLE _class_option (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _name_id INTEGER,
    _description_id INTEGER,
    _class_id INTEGER,
    _source_id INTEGER,
    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
    FOREIGN KEY (_description_id) REFERENCES _translation(_id),
    FOREIGN KEY (_class_id) REFERENCES _class(_id),
    FOREIGN KEY (_source_id) REFERENCES _source(_id)
);

-- N-N table to link classes to their features
DROP TABLE IF EXISTS _class_feature;
CREATE TABLE _class_feature (
    _class_id INTEGER NOT NULL,
    _feature_id INTEGER NOT NULL,
    _class_option_id INTEGER, -- Or null for base class feature
    _level INTEGER, -- The level at which the character learns this feature
    FOREIGN KEY (_class_id) REFERENCES _class(_id),
    FOREIGN KEY (_feature_id) REFERENCES _feature(_id),
    PRIMARY KEY (_class_id, _feature_id),
    FOREIGN KEY (_class_option_id) REFERENCES _class_option(_id)
);

---- light armor, medium armor, heavy armor, extra (shield)
--DROP TABLE IF EXISTS _armor_type;
--CREATE TABLE _armor_type (
--    _id INTEGER PRIMARY KEY AUTOINCREMENT,
--    _name_id INTEGER,
--    FOREIGN KEY (_name_id) REFERENCES _translation(_id)
--);
--
---- leather armor, breastplate, chain mail, shield, etc....
--DROP TABLE IF EXISTS _armor;
--CREATE TABLE _armor (
--    _id INTEGER PRIMARY KEY AUTOINCREMENT,
--    _name_id INTEGER,
--    _type_id INTEGER,
--    _ac INTEGER, -- provided AC (type determines if it is a bonus or a base value)
--    _cost INTEGER, -- in copper pieces
--    _strength INTEGER, -- minimum Str value to use the armor without speed penalty
--    _stealth_dvg INTEGER, -- 1 = disadvantage, 0 = normal
--    _weight INTEGER, -- in pounds
--    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
--    FOREIGN KEY (_type_id) REFERENCES _armor_type(_id),
--);
--
---- slashing, bludgeoning, cold, thunder, poison, etc....
--DROP TABLE IF EXISTS _damage_type;
--CREATE TABLE _damage_type (
--    _id INTEGER PRIMARY KEY AUTOINCREMENT,
--    _name_id INTEGER,
--    _description_id INTEGER,
--    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
--    FOREIGN KEY (_description_id) REFERENCES _translation(_id)
--);
--
---- dagger, short bow, longsword, heavy crossbow, etc....
--DROP TABLE IF EXISTS _weapon;
--CREATE TABLE _weapon (
--    _id INTEGER PRIMARY KEY AUTOINCREMENT,
--    _name_id INTEGER,
--    _ranged INTEGER,
--    _martial INTEGER,
--    _damage_dice INTEGER,
--    _damage_type_id INTEGER
--    _cost INTEGER, -- in copper pieces
--    _weight INTEGER, -- in pounds
--    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
--    FOREIGN KEY (_damage_type_id) REFERENCES _damage_type(_id),
--);
--
--DROP TABLE IF EXISTS _weapon_property;
--CREATE TABLE_weapon_property (
--    _weapon_id INTEGER,
--    _feature_id INTEGER,
--    FOREIGN KEY (_weapon_id) REFERENCES _weapon(_id),
--    FOREIGN KEY (_feature_id) REFERENCES _feature(_id),
--    PRIMARY KEY (_weapon_id, _feature_id)
--);

DROP TABLE IF EXISTS _rollable_table;
CREATE TABLE _rollable_table (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _name_id INTEGER,
    _description_id INTEGER,
    _roll TEXT, -- 1d100 (em outras tabelas poderia ser: 2d10, 1d12+1d8, etc.)
    _source_id INTEGER,
    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
    FOREIGN KEY (_description_id) REFERENCES _translation(_id),
    FOREIGN KEY (_source_id) REFERENCES _source(_id)
);

DROP TABLE IF EXISTS _table_item;
CREATE TABLE _table_item (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _rollable_table_id INTEGER,
    _feature_id INTEGER, -- The ashes of a long dead hero
    _range_from INTEGER, -- 8 (this is the result for any roll from 8 to 10 in the table)
    _range_to INTEGER, -- 10
    FOREIGN KEY (_rollable_table_id) REFERENCES _rolllable_table(_id),
    FOREIGN KEY (_feature_id) REFERENCES _feature(_id)
);
