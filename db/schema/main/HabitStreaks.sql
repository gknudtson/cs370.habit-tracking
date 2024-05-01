create table HabitStreaks
(
    streak_id      INTEGER
        primary key autoincrement,
    habit_id       INTEGER
        references Habits,
    date_completed DATE,
    streak_count   INTEGER
);

