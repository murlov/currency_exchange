# Currency Exchange

## Описание

Проект представляет собой цифровой обменник валют. Умеет хранить и добавлять валюты, хранить, добавлять и изменять курсы валют, конвертировать валюты по прямому, обратном и кросс-курсу.


## Используемые технологии

- Java
- Jakarta Servlet
- Apache Tomcat
- SQLite
- Maven
- JDBC

## Требования

- JDK 21+
- Tomcat 11+
- Nginx (для фронтенда в рамках деплоя)

## Локальный запуск

1. Клонировать репозиторий
2. Задать переменные окружения на основе .env.example (каталог для файла БД должен уже существовать)
3. Отредактировать `...\tomcat\conf\server.xml` для обработки тела PATCH:
   ```
   ...
   <Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" 
               parseBodyMethods="POST,PUT,PATCH"/>
   ...
   ```
4. Собрать war-файл
5. Запустить Tomcat
6. Перейти по адресу `http://localhost:8080/`
7. Для тестирования с использованием фронтенда запустить index.html



## Деплой (на примере Ubuntu)

1. Клонируем репозиторий `git clone https://github.com/murlov/currency_exchange.git`
2. Создаем файл .env `cp .env.example /opt/currency_exchange/.env` (прописать свои значения, каталог для файла БД должен уже существовать)
3. Отредактировать `/etc/systemd/system/tomcat.service`:
   ```
   ...
   [Service]
   EnvironmentFile=/opt/currency_exchange/.env
   ...
   ```
4. Выполнить сборку war-файла `./mvnw clean package`
5. Переместить его в каталог Tomcat'a `cp target/currency_exchange-1.0-SNAPSHOT.war /opt/tomcat/apache-tomcat-<version>/webapps/ROOT.war`
6. Отредактировать `/opt/tomcat/apache-tomcat-<version>/conf/server.xml` для обработки тела PATCH:
   ```
   ...
   <Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" 
               parseBodyMethods="POST,PUT,PATCH"/>
   ...
   ```
7. Скопировать фронтенд `cp -r frontend/. /opt/currency_exchange/frontend/`
8. Отредактировать `/etc/nginx/sites-available/default`:
   ```
   server {
          listen 80 default_server;
          listen [::]:80 default_server;
  
          root /opt/currency_exchange/frontend;
          index index.html;
  
          server_name _;
  
          location / {
                  try_files $uri $uri/ =404;
          }
  
          location /api/ {
                  proxy_pass http://127.0.0.1:8080/;
                  proxy_set_header Host $host;
                  proxy_set_header X-Real-IP $remote_addr;
          }
   }
   ```

9. Отредактировать `frontend/js/app.js`:
   ```
   const host = "/api";
   ```
10. Запустить Tomcat `sudo systemctl start tomcat`


## API

- Базовый URL:
`http://<server-api>/api`

- Доступные пути
  - GET /currencies - получить список валют
  - GET /currency/{CODE} - получить определенную валюту
  - POST /currencies - добавление валюты. Формат полей - x-www-form-urlencoded. Поля:
    - name (длина не более 30 символов)
    - code (3 символа из A-Z)
    - sign (не более 5)
  - GET /exchangeRates - получить список курсов обмена
  - GET /exchangeRate/{BASE_CURRENCY_CODE}{TARGET_CURRENCY_CODE} - получить определенный курс обмена
  - POST /exchangeRates - добавить курс обмена. Формат полей - x-www-form-urlencoded. Поля:
    - baseCurrencyCode (3 символа из A-Z)
    - targetCurrencyCode (3 символа из A-Z)
    - rate
  - PATCH /exchangeRate/{BASE_CURRENCY_CODE}{TARGET_CURRENCY_CODE} - обновление курса обмена. Формат полей - x-www-form-urlencoded. Поля:
    - rate
  - GET /exchange?from={BASE_CURRENCY_CODE}&to={TARGET_CURRENCY_CODE}&amount={AMOUNT}
