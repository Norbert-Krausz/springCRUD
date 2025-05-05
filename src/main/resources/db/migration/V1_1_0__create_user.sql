CREATE TABLE IF NOT EXISTS "USERS"(
    id      int primary key not null,
    firstName    varchar         not null,
    lastName     varchar         not null,
    email        varchar         not null,
    phoneNumber  varchar         not null
)