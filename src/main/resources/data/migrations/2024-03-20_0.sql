CREATE TABLE IF NOT EXISTS users(
    id INTEGER PRIMARY KEY,
    world_id INTEGER,
    FOREIGN KEY (world_id) REFERENCES worlds(id)
);

CREATE TABLE IF NOT EXISTS worlds(
    id INTEGER PRIMARY KEY ,
    x_size INTEGER NOT NULL,
    y_size INTEGER NOT NULL
);