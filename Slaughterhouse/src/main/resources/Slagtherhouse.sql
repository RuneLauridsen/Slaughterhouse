--
-- slaughterHouse_registraion: Til station 1-2
--

drop schema if exists slaughterHouse_registraion cascade ;
create schema slaughterHouse_registraion;
set search_path = slaughterHouse_registraion;

create table pig (
    reg_number serial NOT NULL PRIMARY KEY,
    weight float,
    farm varchar(100),
    date_of_reg date,
    date_of_split date
);

insert into pig (weight, farm, date_of_reg, date_of_split)
values(40, 'Simons Farm', '2023-09-12', null),
      (80, 'Julies Farm', '2023-09-30', null),
      (77, 'Runes Farm',  '2023-10-01', '2023-10-05'),
      (80, 'Majas Farm',  '2023-10-04', '2023-10-07');

--
-- slaughterHouse_distribution: Til station 2-3
--

drop schema if exists slaughterHouse_distribution cascade ;
create schema slaughterHouse_distribution;
set search_path = slaughterHouse_distribution;

create table tray (
    tray_id         serial              NOT NULL PRIMARY KEY,
    max_weight      int                 NOT NULL
);

create table package (
    package_id      serial              NOT NULL PRIMARY KEY,
    package_type    varchar             NOT NULL
);

create table pig_part (
    part_id         serial              NOT NULL PRIMARY KEY,
    part_name       varchar             NOT NULL,
    part_weight     double precision    NOT NULL,

    -- reg_number er foreign key til slaughterHouse_registration.pig.reg_number, men pga.
    -- det skal være splittet op i flere databaser, har vi ikke referentiel integritet.
    pig_reg_number  int                 NOT NULL,
    tray_id         int                 REFERENCES tray(tray_id),
    package_id      int                 REFERENCES package(package_id)
);



-- package -> pig parts -> tray -> pig

INSERT INTO package
    (package_type)
VALUES
    ('abc'), -- id 1
    ('def'), -- id 2
    ('hij'); -- id 3

INSERT INTO pig_part
    (part_name, part_weight, pig_reg_number, tray_id, package_id)
VALUES
    ('Butt', 2, 40, NULL, 1),
    ('Leg',  2, 40, NULL, 2),
    ('Belly', 1, 80, NULL, 3),
    ('Butt', 2, 80, NULL, 1),
    ('Leg', 1, 77, NULL, 2),
    ('Belly', 99, 77, NULL, 3);
