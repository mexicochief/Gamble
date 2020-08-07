CREATE TABLE bets
(
    id             bigint auto_increment,
    bet            bigint NOT NULL,
    balance_change bigint NOT NULL,
    user_id        bigint NOT NULL
);
CREATE TABLE users
(
    id      BIGINT auto_increment,
    name    varchar NOT NULL,
    balance INT     NOT NULL
);