create table Users
(
    user_id  INTEGER
        primary key autoincrement,
    username TEXT not null,
    email    TEXT not null
        unique
);

