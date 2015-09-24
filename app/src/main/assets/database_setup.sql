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
    FOREIGN KEY (_name_id) REFERENCES _translation(_id)
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
    FOREIGN KEY (_name_id) REFERENCES _translations(_id),
    FOREIGN KEY (_description_id) REFERENCES _translation(_id)
);

-- N-N table to link races to their traits
DROP TABLE IF EXISTS _racial_trait;
CREATE TABLE _racial_trait (
    _sub_race_id INTEGER NOT NULL,
    _feature_id INTEGER NOT NULL,
    PRIMARY KEY (_sub_race_id, _feature_id)
);