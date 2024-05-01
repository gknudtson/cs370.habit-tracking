create table Habits
(
    habit_id     INTEGER
        primary key autoincrement,
    user_id      INTEGER
        references Users,
    habit_name   TEXT not null,
    custom_label TEXT
);

