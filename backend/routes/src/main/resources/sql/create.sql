DROP TABLE IF EXISTS routes, locations;

CREATE TABLE locations (
                           id bigserial PRIMARY KEY,
                           x bigint NOT NULL,
                           y float NOT NULL,
                           name VARCHAR(256)
);

CREATE TABLE routes (
                       id bigserial PRIMARY KEY,
                       creation_date timestamp NOT NULL,
                       name VARCHAR(256) NOT NULL,
                       from_location_id bigint REFERENCES locations(id) ON DELETE CASCADE NOT NULL,
                       to_location_id bigint REFERENCES locations(id) ON DELETE CASCADE NOT NULL,
                       distance float NOT NULL
);

INSERT INTO locations (x, y, name)
VALUES
    (123, 56.70, 'Локация №1'),
    (23,  21.40, 'Локация №2'),
    (15,  82.91, 'Локация №3'),
    (214, 24.32, 'Локация №4'),
    (13,  59.31, 'Локация №5'),
    (12,  83.42, 'Локация №6'),
    (54,  49.12, 'Локация №7'),
    (36,  84.40, 'Локация №8'),
    (73,   6.70, 'Локация №9');

INSERT INTO routes (creation_date, name, from_location_id, to_location_id, distance)
VALUES
    ('2022-01-01 5:30:00', 'Путь №1', 1, 2, 369.69),
    ('2022-01-02 6:30:00', 'Путь №2', 1, 3, 481.22),
    ('2022-01-03 7:30:00', 'Путь №3', 2, 4, 124.51),
    ('2022-01-04 8:30:00', 'Путь №4', 3, 8, 2394.5),
    ('2022-01-05 9:30:00', 'Путь №5', 4, 6, 682.33),
    ('2022-01-06 10:30:00', 'Путь №6', 5, 4, 127.1),
    ('2022-01-07 11:30:00', 'Путь №7', 5, 7, 888.2),
    ('2022-01-08 12:30:00', 'Путь №8', 5, 8, 2079.98),
    ('2022-01-09 13:30:00', 'Путь №9', 6, 5, 326.45),
    ('2022-01-10 14:30:00', 'Путь №10', 7, 8, 550.12),
    ('2022-01-11 15:30:00', 'Путь №11', 8, 4, 1582.95),
    ('2022-01-12 16:30:00', 'Путь №12', 8, 5, 2079.98),
    ('2022-01-13 17:30:00', 'Путь №13', 8, 9, 245.15),
    ('2022-01-14 18:30:00', 'Путь №14', 9, 1, 3890.8),
    ('2022-01-15 19:30:00', 'Путь №15', 9, 5, 2292.5);