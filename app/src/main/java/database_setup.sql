DROP TABLE IF EXISTS _translation;
CREATE TABLE _translation (
    _id INTEGER NOT NULL,
    _language INTEGER NOT NULL,
    _text TEXT,
    PRIMARY KEY (textId, languageId)
);

-- tiny, small, medium, large, huge, gargantuan
DROP TABLE IF EXISTS _size;
CREATE TABLE _size (
    _id INTEGER PRIMARY KEY,
    FOREIGN KEY (_description_id) REFERENCES _translation(_id)
);

-- human, dwarf, elf, halfling, etc....
DROP TABLE IF EXISTS _super_race;
CREATE TABLE _super_race (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
    FOREIGN KEY (_names_description_id) REFERENCES _translation(_id),
);

-- default human, variant human, high elf, wood elf, drow, hills dwarf, etc....
DROP TABLE IF EXISTS _sub_race;
CREATE TABLE _sub_race (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
    FOREIGN KEY (_super_race_id) REFERENCES _super_race(_id),
    _tallness TEXT,
    _weight TEXT,
    FOREIGN KEY (_size_id) REFERENCES _size(_id),
    FOREIGN KEY (_adult_age_id) REFERENCES _translation(_id),
    FOREIGN KEY (_max_age_id) REFERENCES _translation(_id),
    FOREIGN KEY (_alignment_id) REFERENCES _translation(_id),
    FOREIGN KEY (_ability_id) REFERENCES _translation(_id),
    _speed INTEGER,
    _darkvision INTEGER,
    FOREIGN KEY (_languages_id) REFERENCES _translation(_id),
);

-- child name, male name, female name, family/clan name, nickname
DROP TABLE IF EXISTS _name_type;
CREATE TABLE _name_type (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    FOREIGN KEY (_description_id) REFERENCES _translation(_id)
);

-- name samples ...
DROP TABLE IF EXISTS _sample_name;
CREATE TABLE _sample_name (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    FOREIGN KEY (_sub_race_id) REFERENCES _sub_race(_id),
    FOREIGN KEY (_name_id) REFERENCES _translations(_id),
    FOREIGN KEY (_type_id) REFERENCES _name_type
);

-- darkvision, trance, weapon training, breath weapon, etc....
DROP TABLE IF EXISTS _racial_trait;
CREATE TABLE _racial_trait (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
    FOREIGN KEY (_description_id) REFERENCES _translation(_id)
);
