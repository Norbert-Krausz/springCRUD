CREATE TABLE IF NOT EXISTS "users"(
    id      int primary key not null,
    firstName    varchar         not null,
    lastName     varchar         not null,
    email        varchar         not null,
    phoneNumber  varchar         not null
)