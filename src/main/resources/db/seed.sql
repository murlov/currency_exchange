insert or ignore into currencies (code, full_name, sign)
values ('AED', 'UAE Dirham', 'Ð'),
       ('TRY', 'Turkish Lira', '₺'),
       ('JPY', 'Yen', '¥'),
       ('GBP', 'Pound Sterling', '£'),
       ('INR', 'Indian Rupee', '₹'),
       ('RUB', 'Russian Ruble', '₽');

insert or ignore into exchange_rates (base_currency_id, target_currency_id, rate)
values (2, 6, 1.78),
       (1, 5, 24.66),
       (4, 1, 5),
       (6, 3, 1.98);