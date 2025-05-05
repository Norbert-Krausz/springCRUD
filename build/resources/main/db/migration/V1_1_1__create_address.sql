CREATE TABLE IF NOT EXISTS "address"(
    id              int primary key not null,
    streetName      varchar         not null,
    streetNumber    varchar         not null,
    city            varchar         not null,
    postcode        varchar         not null,
    isCurrent       boolean         not null
)