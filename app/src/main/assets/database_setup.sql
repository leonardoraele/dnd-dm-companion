-- Localized texts
DROP TABLE IF EXISTS _translation;
CREATE TABLE _translation (
    _id INTEGER NOT NULL,
    _language CHAR(3) NOT NULL, -- ISO 639-2/5 language code, upper case
    _text TEXT,
    PRIMARY KEY (_id, _language)
);
INSERT INTO _translation (_id, _language, _text) VALUES (1, 'ENG', 'Tiny');
INSERT INTO _translation (_id, _language, _text) VALUES (1, 'POR', 'Minúsculo');
INSERT INTO _translation (_id, _language, _text) VALUES (2, 'ENG', 'Small');
INSERT INTO _translation (_id, _language, _text) VALUES (2, 'POR', 'Pequeno');
INSERT INTO _translation (_id, _language, _text) VALUES (3, 'ENG', 'Medium');
INSERT INTO _translation (_id, _language, _text) VALUES (3, 'POR', 'Médio');
INSERT INTO _translation (_id, _language, _text) VALUES (4, 'ENG', 'Large');
INSERT INTO _translation (_id, _language, _text) VALUES (4, 'POR', 'Grande');
INSERT INTO _translation (_id, _language, _text) VALUES (5, 'ENG', 'Huge');
INSERT INTO _translation (_id, _language, _text) VALUES (5, 'POR', 'Gigantesco');
INSERT INTO _translation (_id, _language, _text) VALUES (6, 'ENG', 'Gargantuan');
INSERT INTO _translation (_id, _language, _text) VALUES (6, 'POR', 'Colossal');
INSERT INTO _translation (_id, _language, _text) VALUES (7, 'ENG', 'Human');
INSERT INTO _translation (_id, _language, _text) VALUES (7, 'POR', 'Humano');
INSERT INTO _translation (_id, _language, _text) VALUES (8, 'ENG', 'Human (Default)');
INSERT INTO _translation (_id, _language, _text) VALUES (8, 'POR', 'Humano (Padrão)');
INSERT INTO _translation (_id, _language, _text) VALUES (9, 'ENG', '18-20');
INSERT INTO _translation (_id, _language, _text) VALUES (9, 'POR', '18-20');
INSERT INTO _translation (_id, _language, _text) VALUES (10, 'ENG', '80-90');
INSERT INTO _translation (_id, _language, _text) VALUES (10, 'POR', '80-90');
INSERT INTO _translation (_id, _language, _text) VALUES (11, 'ENG', 'Any');
INSERT INTO _translation (_id, _language, _text) VALUES (11, 'POR', 'Todos');
INSERT INTO _translation (_id, _language, _text) VALUES (12, 'ENG', '+1 on all ability scores');
INSERT INTO _translation (_id, _language, _text) VALUES (12, 'POR', '+1 em cada habilidade');
INSERT INTO _translation (_id, _language, _text) VALUES (13, 'ENG', 'Common and one more language of your choise');
INSERT INTO _translation (_id, _language, _text) VALUES (13, 'POR', 'Comum e mais uma linguagem à sua escolha');
INSERT INTO _translation (_id, _language, _text) VALUES (14, 'ENG', 'Human (Variant)');
INSERT INTO _translation (_id, _language, _text) VALUES (14, 'POR', 'Humano (Variante)');
INSERT INTO _translation (_id, _language, _text) VALUES (15, 'ENG', '18-20');
INSERT INTO _translation (_id, _language, _text) VALUES (15, 'POR', '18-20');
INSERT INTO _translation (_id, _language, _text) VALUES (16, 'ENG', '80-90');
INSERT INTO _translation (_id, _language, _text) VALUES (16, 'POR', '80-90');
INSERT INTO _translation (_id, _language, _text) VALUES (17, 'ENG', 'Any');
INSERT INTO _translation (_id, _language, _text) VALUES (17, 'POR', 'Todos');
INSERT INTO _translation (_id, _language, _text) VALUES (18, 'ENG', '+1 on two ability scores of your choise');
INSERT INTO _translation (_id, _language, _text) VALUES (18, 'POR', '+1 em duas habilidades à sua escolha');
INSERT INTO _translation (_id, _language, _text) VALUES (19, 'ENG', 'Common and one more language of your choise');
INSERT INTO _translation (_id, _language, _text) VALUES (19, 'POR', 'Comum e mais uma linguagem à sua escolha');
INSERT INTO _translation (_id, _language, _text) VALUES (20, 'ENG', 'Skill');
INSERT INTO _translation (_id, _language, _text) VALUES (20, 'POR', 'Habilidade');
INSERT INTO _translation (_id, _language, _text) VALUES (21, 'ENG', 'You gain proficiency in one skill of your choise.');
INSERT INTO _translation (_id, _language, _text) VALUES (21, 'POR', 'Você ganha proficiência em uma perícia à sua escolha.');
INSERT INTO _translation (_id, _language, _text) VALUES (22, 'ENG', 'Feat');
INSERT INTO _translation (_id, _language, _text) VALUES (22, 'POR', 'Talento');
INSERT INTO _translation (_id, _language, _text) VALUES (23, 'ENG', 'You gain one feat of your choise.');
INSERT INTO _translation (_id, _language, _text) VALUES (23, 'POR', 'Você ganha um talento à sua escolha.');
INSERT INTO _translation (_id, _language, _text) VALUES (24, 'ENG', 'Child names');
INSERT INTO _translation (_id, _language, _text) VALUES (24, 'POR', 'Nomes infantis');
INSERT INTO _translation (_id, _language, _text) VALUES (25, 'ENG', 'Male names');
INSERT INTO _translation (_id, _language, _text) VALUES (25, 'POR', 'Nomes masculinos');
INSERT INTO _translation (_id, _language, _text) VALUES (26, 'ENG', 'Female names');
INSERT INTO _translation (_id, _language, _text) VALUES (26, 'POR', 'Nomes femininos');
INSERT INTO _translation (_id, _language, _text) VALUES (27, 'ENG', 'Clan/Family names');
INSERT INTO _translation (_id, _language, _text) VALUES (27, 'POR', 'Nomes de clã/família');
INSERT INTO _translation (_id, _language, _text) VALUES (28, 'ENG', 'Nickname');
INSERT INTO _translation (_id, _language, _text) VALUES (28, 'POR', 'Apelido');
INSERT INTO _translation (_id, _language, _text) VALUES (29, 'ENG', 'Huey');
INSERT INTO _translation (_id, _language, _text) VALUES (29, 'POR', 'Huguinho');
INSERT INTO _translation (_id, _language, _text) VALUES (30, 'ENG', 'Dewey');
INSERT INTO _translation (_id, _language, _text) VALUES (30, 'POR', 'Zezinho');
INSERT INTO _translation (_id, _language, _text) VALUES (31, 'ENG', 'Louie');
INSERT INTO _translation (_id, _language, _text) VALUES (31, 'POR', 'Luizinho');
INSERT INTO _translation (_id, _language, _text) VALUES (32, 'ENG', 'John Snow');
INSERT INTO _translation (_id, _language, _text) VALUES (32, 'POR', 'João Neves');
INSERT INTO _translation (_id, _language, _text) VALUES (33, 'ENG', 'Edward Elric');
INSERT INTO _translation (_id, _language, _text) VALUES (33, 'POR', 'Eduardo Eurico');
INSERT INTO _translation (_id, _language, _text) VALUES (34, 'ENG', 'Peter Parker');
INSERT INTO _translation (_id, _language, _text) VALUES (34, 'POR', 'Pedro Manobrista');
INSERT INTO _translation (_id, _language, _text) VALUES (35, 'ENG', 'Mary Jane');
INSERT INTO _translation (_id, _language, _text) VALUES (35, 'POR', 'Maria Jânia');
INSERT INTO _translation (_id, _language, _text) VALUES (36, 'ENG', 'Lucy');
INSERT INTO _translation (_id, _language, _text) VALUES (36, 'POR', 'Lúcia');
INSERT INTO _translation (_id, _language, _text) VALUES (37, 'ENG', 'Bill');
INSERT INTO _translation (_id, _language, _text) VALUES (37, 'POR', 'Biu');

-- tiny, small, medium, large, huge, gargantuan
DROP TABLE IF EXISTS _size;
CREATE TABLE _size (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _description_id INTEGER,
    FOREIGN KEY (_description_id) REFERENCES _translation(_id)
);
INSERT INTO _size (_id, _description_id) VALUES (1, 1); -- Tiny
INSERT INTO _size (_id, _description_id) VALUES (2, 2); -- Small
INSERT INTO _size (_id, _description_id) VALUES (3, 3); -- Medium
INSERT INTO _size (_id, _description_id) VALUES (4, 4); -- Large
INSERT INTO _size (_id, _description_id) VALUES (5, 5); -- Huge
INSERT INTO _size (_id, _description_id) VALUES (6, 6); -- Gargantuan

-- human, dwarf, elf, halfling, etc....
DROP TABLE IF EXISTS _super_race;
CREATE TABLE _super_race (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _name_id INTEGER,
    FOREIGN KEY (_name_id) REFERENCES _translation(_id)
);
INSERT INTO _super_race (_id, _name_id) VALUES (1, 7); -- Human

-- default human, variant human, high elf, wood elf, drow, hills dwarf, etc....
DROP TABLE IF EXISTS _sub_race;
CREATE TABLE _sub_race (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _name_id INTEGER,
    _super_race_id INTEGER,
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
    FOREIGN KEY (_adult_age_id) REFERENCES _translation(_id),
    FOREIGN KEY (_max_age_id) REFERENCES _translation(_id),
    FOREIGN KEY (_alignment_id) REFERENCES _translation(_id),
    FOREIGN KEY (_ability_id) REFERENCES _translation(_id),
    FOREIGN KEY (_languages_id) REFERENCES _translation(_id)
);
-- Default Human
INSERT INTO _sub_race (_id, _name_id, _super_race_id,   _height,       _weight, _size_id, _adult_age_id, _max_age_id, _alignment_id, _ability_id, _speed, _darkvision, _languages_id)
               VALUES (  1,        8,              1, '5-6 ft.', '120-240 lb.',        3,             9,          10,            11,          12,     30,           0,            13);
-- Variant Human
INSERT INTO _sub_race (_id, _name_id, _super_race_id,   _height,       _weight, _size_id, _adult_age_id, _max_age_id, _alignment_id, _ability_id, _speed, _darkvision, _languages_id)
               VALUES (  2,       14,              1, '5-6 ft.', '120-240 lb.',        3,            15,          16,            17,          18,     30,           0,            19);

-- child name, male name, female name, family/clan name, nickname
DROP TABLE IF EXISTS _name_type;
CREATE TABLE _name_type (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _description_id INTEGER,
    FOREIGN KEY (_description_id) REFERENCES _translation(_id)
);
INSERT INTO _name_type (_id, _description_id) VALUES (1, 24); -- Child names
INSERT INTO _name_type (_id, _description_id) VALUES (2, 25); -- Male names
INSERT INTO _name_type (_id, _description_id) VALUES (3, 26); -- Female names
INSERT INTO _name_type (_id, _description_id) VALUES (4, 27); -- Clan/Family names
INSERT INTO _name_type (_id, _description_id) VALUES (5, 28); -- Nicknames

-- name samples ...
DROP TABLE IF EXISTS _sample_name;
CREATE TABLE _sample_name (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _race_id INTEGER,
    _name_id INTEGER,
    _type_id INTEGER,
    FOREIGN KEY (_race_id) REFERENCES _super_race(_id),
    FOREIGN KEY (_name_id) REFERENCES _translations(_id),
    FOREIGN KEY (_type_id) REFERENCES _name_type
);
INSERT INTO _sample_name (_race_id, _name_id, _type_id) VALUES (1, 29, 1);
INSERT INTO _sample_name (_race_id, _name_id, _type_id) VALUES (1, 30, 1);
INSERT INTO _sample_name (_race_id, _name_id, _type_id) VALUES (1, 31, 1);
INSERT INTO _sample_name (_race_id, _name_id, _type_id) VALUES (1, 32, 2);
INSERT INTO _sample_name (_race_id, _name_id, _type_id) VALUES (1, 33, 2);
INSERT INTO _sample_name (_race_id, _name_id, _type_id) VALUES (1, 34, 2);
INSERT INTO _sample_name (_race_id, _name_id, _type_id) VALUES (1, 37, 2);
INSERT INTO _sample_name (_race_id, _name_id, _type_id) VALUES (1, 35, 3);
INSERT INTO _sample_name (_race_id, _name_id, _type_id) VALUES (1, 36, 3);

-- darkvision, trance, weapon training, breath weapon, etc....
DROP TABLE IF EXISTS _racial_trait;
CREATE TABLE _racial_trait (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    _sub_race_id INTEGER,
    _name_id INTEGER,
    _description_id INTEGER,
    FOREIGN KEY (_sub_race_id) REFERENCES _sub_race(_id),
    FOREIGN KEY (_name_id) REFERENCES _translation(_id),
    FOREIGN KEY (_description_id) REFERENCES _translation(_id)
);
INSERT INTO _racial_trait (_sub_race_id, _name_id, _description_id) VALUES (2, 20, 21); -- Variant Human - Skill
INSERT INTO _racial_trait (_sub_race_id, _name_id, _description_id) VALUES (2, 22, 23); -- Variant Human - Feat