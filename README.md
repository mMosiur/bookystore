# BookyStore

**ARCHIVED**, środowisko produkcyjne wyłączone.

[![Continuous Integration](https://github.com/mMosiur/bookystore/actions/workflows/ci.yml/badge.svg)](https://github.com/mMosiur/bookystore/actions/workflows/ci.yml) \
[![Continuous Deployment](https://github.com/mMosiur/bookystore/actions/workflows/cd.yml/badge.svg)](https://github.com/mMosiur/bookystore/actions/workflows/cd.yml)

System księgarni do projektu zaliczeniowego z przedmiotu "Systemy klasy Enterprise - persystencje" na Uniwersytecie Marii Curie-Skłodowskiej w Lublinie.

## Zagadnienia

- [x] **3**: CRUD + Spring Security z podziałem na role : *admin* - dodaje i usuwa książki z księgarni, *user* możne przeglądać.
- [x] **3.5**: CRUD + koszyk - Dodawanie, usuwanie książek przez *usera*.
- [x] **4**: Zamówienia - Dodanie funkcji które może wykonywać *user* - składanie zamówień na książki, zmiana statusu zamówienia przez *admina*.
- [x] **4.5**: Wygląd aplikacji - Można użyć bootstrapa / własny bardziej rozbudowany projekt lub bardziej rozszerzony projekt z przykładu
- [x] **5**: Płatności - podpięcie np. PayU

## Technologie

- [Spring Boot](https://spring.io/projects/spring-boot)
- [JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate](https://hibernate.org/)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Bootstrap](https://getbootstrap.com/)
- [Docker](https://www.docker.com/)
- [Azure](https://azure.microsoft.com/pl-pl/)
- [PayU](https://developers.payu.com/pl/overview.html)

## Kompilacja

Projekt jest tworzony z użyciem [Mavena](https://maven.apache.org/), zbudować go więc można za pomocą:

``` bash
./mvnw clean package
```

Argument `clean` zapewnia że zbudowany zostanie najświeższy wariant projektu, a cykl `package` pobierze zależności,
skompiluje projekt, uruchomi testy jednostkowe oraz utworzy paczkę `jar` w folderze `target`.

### Profile

W projekcie zdefiniowane są trzy profile:

- #### Profil `dev`
  
  Profil domyślny, wybierany, gdy żaden inny nie zostanie sprecyzowany.
  Uruchamia bazę danych H2 in-memory oraz dodaje do niej konsolę pod adresem */h2-console*.
  Baza jest usuwana i tworzona od nowa za każdym uruchomieniem.
  Jawnie możemy go sprecyzować do kompilacji dodając flagę `-Pdev`.
  UWAGA: w środowisku `dev` nie będzie działał callback z PayU (no bo `localhost`),
  więc trzeba symulować jego przyjście ręcznie.

- #### Profil `docker`:

  Profil dedykowany dla uruchomienia z [`docker compose`](https://docs.docker.com/compose/).
  Podpina się do bazy danych PostgreSQL uruchamianej razem z aplikacją z URLem bazowanym
  na nazwie hosta pochodzącej z sieci wewnętrznej dockera.
  Baza jest aktualizowana do najnowszej wersji, a dane z niej persystują w utworzonym przez
  dockera [woluminie](https://docs.docker.com/storage/volumes/).
  UWAGA: w środowisku `docker` nie będzie działał callback z PayU (no bo `localhost`),
  więc trzeba symulować jego przyjście ręcznie.

- #### Profil `prod`:

  Profil dedykowany dla uruchomienia w środowisku produkcyjnym.
  Używane jest przez pipeline `cd.yml` uruchamiany jako GitHub action,
  który buduje z nim aplikację wstawiając wrażliwe dane ze zmiennych środowiskowych,
  które są przekazywane z GitHub secrets. Deploy aplikacji znajduje się w platformie [Azure](https://azure.microsoft.com/pl-pl/).

## Uruchomienie

(najprostszy sposób z wykorzystaniem dockera na końcu sekcji)

Nie trzeba tak na prawdę uruchamiać aplikacji u siebie.
Jest ona dostępna w środowisku produkcyjnym pod adresem
[https://bookystore.azurewebsites.net](https://bookystore.azurewebsites.net).

Jeśli jednak chcemy lokalnie:

Projekt uruchomić można bezpośrednio z kodu za pomocą cyklu mavena:

``` bash
./mvnw spring-boot:run
```

Wykorzystany wtedy jest domyślny profil [dev](#profil-dev).
Aplikacja będzie dostępna na porcie `8080`.

Można także uruchomić zbudowany wcześniej plik `jar`:

```
java -jar target/bookystore-*.jar
```

Wykorzysta to profil który posłużył do zbudowania pliku `jar` (przy wywołaniu `package`).

Można także zbudować obraz dockera oraz uruchomić go:

``` bash
docker build -t bookystore .
docker run bookystore
```

Zbudowanie bezpośrednio przez `docker` wykorzysta `Dockerfile`, który z kolei używa profilu [`dev`](#profil-dev),
więc obraz den będzie wykorzystywał bazę H2 bez persystencji między uruchomieniami.

Najprościej jednak aby uruchomić lokalnie z PostgreSQL,
można aplikację zbudować oraz uruchomić z użyciem `docker compose`:

``` bash
docker compose build # alternatywnie docker-compose build
docker compose up # alternatywnie docker-compose up
```

Uruchomi to obraz bazy danych oraz obraz aplikacji (wykorzysta do tego `Dockerfile.compose`).
Wykorzysta więc profil [`docker`](#profil-docker).
Aplikacja będzie dostępna na porcie `8080`.

## Deploy

Środowisko produkcyjne działa pod adresem [https://bookystore.azurewebsites.net](https://bookystore.azurewebsites.net).
Z każdym *pushem* nowego kodu na gałąź `main` uruchamia się proces deployu na platformę Azure najświeższej wersji aplikacji.

## Obsługa

Domyślnie utworzony użytkownik:

- **email**: `admin@bookystore.com`
- **hasło**: `password`
- **rola**: `admin`

Utworzyć można także swoich nowych użytkowników,
każdy zarejestrowany użytkownik domyślnie otrzymuje rolę `user`.
