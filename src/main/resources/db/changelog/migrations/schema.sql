--changeset ya:1
CREATE TABLE client_bank
(
    id                UUID default random_uuid() primary key,
    first_name        varchar(255) not null,
    last_name         varchar(255) not null,
    phone_number      varchar(255) not null,
    email             varchar(255) not null,
    modification_time TIME,
    created_time      TIME
);
CREATE TABLE account
(
    id             UUID default random_uuid() primary key,
    number_account varchar(255) not null,
    currency       varchar(255) not null,
    status         boolean      not null,
    balance        decimal      not null,
    user_id        UUID         not null,
    foreign key (user_id) references client_bank (id)
);
CREATE TABLE operation
(
    id                       UUID default random_uuid() primary key,
    amount                   decimal not null,
    bank_confirmation_status boolean not null,
    account_id               UUID    not null,
    foreign key (account_id) references account (id)
);
CREATE TABLE client_history
(
    id                UUID default random_uuid() primary key,
    first_name        varchar(255) not null,
    last_name         varchar(255) not null,
    phone_number      varchar(255) not null,
    email             varchar(255) not null,
    client_id         UUID         not null,
    modification_time TIME,
    created_time      TIME,
    foreign key (client_id) references client_bank (id)
);
//создаем 2 юзера
INSERT INTO client_bank (id, first_name, last_name, phone_number, email, created_time)
VALUES ('d733dc7e-30d6-41eb-8aca-0aa670e318d9', 'Татьяна', 'Буянова', '+7-9672115269', 'in_sdo@mail.ru', now()),
       ('48e59b03-1c53-4a13-a42b-462c2a2652df', 'Евгений', 'Буянов', '+7-9672115269', 'in_sdo@mail.ru', now());

//у 2-х юзеров по 2 счета у каждого
INSERT INTO account(id, number_account, currency, status, balance, user_id)
VALUES ('cff31aa3-f4b6-4bf8-b2fa-431402f8ebd8', '12345678910111213149', 'RUB', true, '1000',
        'd733dc7e-30d6-41eb-8aca-0aa670e318d9'),
       ('d733dc7e-55d6-41eb-8aca-0aa670e318d5', '12345678910111213148', 'EUR', false, '1000',
        'd733dc7e-30d6-41eb-8aca-0aa670e318d9'),
       ('b444c47c-ed6d-4b77-b40d-29019349496a', '12312u39812830123982', 'RUB', false, '1000',
        '48e59b03-1c53-4a13-a42b-462c2a2652df'),
       ('97aa75ab-ff34-437c-b0bc-1b0b72f2b45b', '21515154515151515151', 'USD', true, '1000',
        '48e59b03-1c53-4a13-a42b-462c2a2652df');

//операции по счету у каждого юзера, ранее созданных
INSERT INTO operation(amount, bank_confirmation_status, account_id)
VALUES ('1000', true, 'd733dc7e-55d6-41eb-8aca-0aa670e318d5'),
       ('-500', true, 'd733dc7e-55d6-41eb-8aca-0aa670e318d5'),
       ('300', true, 'b444c47c-ed6d-4b77-b40d-29019349496a'),
       ('700', true, 'b444c47c-ed6d-4b77-b40d-29019349496a');

