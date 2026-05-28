create table if not exists currencies (
        id integer primary key autoincrement,
        code text not null unique,
        full_name text not null,
        sign text not null
);

create table if not exists exchange_rates (
        id integer primary key autoincrement ,
        base_currency_id id not null,
        target_currency_id id not null,
        rate decimal(6) not null,
        foreign key (base_currency_id) references currencies(id),
        foreign key (target_currency_id) references currencies(id),
        unique(base_currency_id, target_currency_id)
);

