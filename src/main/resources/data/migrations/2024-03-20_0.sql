CREATE TABLE IF NOT EXISTS users(
    id INTEGER PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS users_worlds (
    user_id INTEGER NOT NULL,
    world_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (world_id) REFERENCES worlds(id)
);

CREATE TABLE IF NOT EXISTS worlds(
    id INTEGER PRIMARY KEY ,
    x_size INTEGER NOT NULL,
    y_size INTEGER NOT NULL
);