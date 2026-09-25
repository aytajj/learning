# Bank App — Backend Oyrenme Layihesi

Sade bir bank tetbiqi: istifadeci qeydiyyati, login (JWT), hesab balansi,
pul yatirma ve cixarma.

**Stack:**
- Backend: Java 17 + Spring Boot 3 (Spring Web, Spring Security, Spring Data JPA) — IntelliJ IDEA
- Frontend: React 18 + Vite — WebStorm
- Database: PostgreSQL — DataGrip

## Qovluq strukturu

```
bank-app/
  backend/          -> IntelliJ IDEA-da ac (pom.xml)
  frontend/          -> WebStorm-da ac (package.json)
  docker-compose.yml -> PostgreSQL konteyneri
```

## 1. Verilenler bazasini qaldir (DataGrip ucun)

Bu komputerde Docker aliniqli deyil (yoxladim). Iki secim var:

**A) Docker Desktop qur (tovsiye olunur):**
https://www.docker.com/products/docker-desktop/ -> qur, ac, sonra layihe
kokunden:

```bash
docker compose up -d
```

**B) PostgreSQL-i birbasa qur (Docker istemirsense):**
Homebrew ile: `brew install postgresql@16 && brew services start postgresql@16`,
sonra `bankapp` adinda bir database ve `bankapp/bankapp` user/password yarat
(ve ya `backend/src/main/resources/application.yml`-deki `datasource` bolmesini
oz PostgreSQL melumatlarinla deyish).

Bu `bankapp-postgres` adinda konteyner qaldirir:
- host: `localhost`
- port: `5432`
- database: `bankapp`
- user: `bankapp`
- password: `bankapp`

**DataGrip-de qosulma:** New > Data Source > PostgreSQL, yuxaridaki melumatlari
daxil et, Test Connection > OK. Backend ilk defe ishe dushende Hibernate
`users` ve `accounts` cedvellerini avtomatik yaradacaq (`ddl-auto: update`),
sonra onlari DataGrip-de gore bilersen.

## 2. Backend-i ishe sal (IntelliJ IDEA)

1. IntelliJ-de `bank-app/backend` qovlugunu ac (pom.xml-i taniyacaq, Maven
   dependency-lerini ozu yukleyecek).
2. `BankAppApplication.java`-ni tap ve Run et (yasil oxla).
3. Backend `http://localhost:8080` ustunde qalxir.

Konfiqurasiya: `backend/src/main/resources/application.yml` — DB baglantisi
ve JWT sirri burdadir.

## 3. Frontend-i ishe sal (WebStorm)

1. WebStorm-de `bank-app/frontend` qovlugunu ac.
2. Terminalda:
   ```bash
   npm install
   npm run dev
   ```
3. Brauzerde ac: `http://localhost:5173`

## API siyahisi

| Metod | Endpoint              | Aciqlama                       | Auth teleb olunur |
|-------|------------------------|---------------------------------|--------------------|
| POST  | `/api/auth/register`  | Yeni istifadeci + hesab yaradir | Yox                |
| POST  | `/api/auth/login`     | Login, JWT qaytarir             | Yox                |
| GET   | `/api/account/me`     | Cari istifadecinin hesabi       | Beli (Bearer token)|
| POST  | `/api/account/deposit`| Hesaba pul yatirmaq             | Beli                |
| POST  | `/api/account/withdraw`| Hesabdan pul cixarmaq          | Beli                |

## Test axini

1. `/register` sehifesinde qeydiyyatdan kec -> avtomatik login olub
   dashboard-a yonlendirilirsen, arxa planda sene 0 balansli hesab yaradilir.
2. Dashboard-da "Pul yatir" ile balansi artir.
3. "Pul cixar" ile balansdan cixart (balans kifayet etmirse xeta gorersen).
4. Cixis edib yeniden `/login` ile daxil ol.
5. DataGrip-de `users` ve `accounts` cedvellerine bax, elave etdiyin
   qeydleri ve balans deyishikliklerini canli izle.

## Oyrenme ucun nezere alinacaq nöqteler

- `SecurityConfig.java` — hansi endpoint-lerin acig, hansilarinin JWT teleb
  etdiyini gosterir.
- `JwtAuthFilter.java` — her sorguda `Authorization: Bearer <token>`
  basliginin nece yoxlandigini gosterir.
- `AuthService.java` / `AccountService.java` — biznes mentiqi (registration,
  balans emeliyyatlari) burdadir, controller-ler sadece HTTP qatidir.
- `User` ve `Account` arasinda `@OneToOne` elaqe var — real bankda bu
  `@OneToMany` olardi (bir istifadecinin bir nece hesabi ola biler), sonraki
  addim kimi bunu genişlendirmeyi düşün.
