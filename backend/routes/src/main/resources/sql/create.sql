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
