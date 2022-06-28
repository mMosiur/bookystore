# Bookystore

[![Continuous Integration](https://github.com/mMosiur/bookystore/actions/workflows/ci.yml/badge.svg)](https://github.com/mMosiur/bookystore/actions/workflows/ci.yml)

System księgarni do projektu zaliczeniowego z przedmiotu "Systemy klasy Enterprise - persystencje" na Uniwersytecie Marii Curie-Skłodowskiej w Lublinie.

## Zagadnienia

- [x] **3**: CRUD + Spring Security z podziałem na role : *admin* - dodaje i usuwa książki z księgarni, *user* możne przeglądać.
- [x] **3.5**: CRUD + koszyk - Dodawanie, usuwanie książek przez *usera*.
- [ ] **4**: Zamówienia - Dodanie funkcji które może wykonywać *user* - składanie zamówień na książki, zmiana statusu zamówienia przez *admina*.
- [ ] **4.5**: Wygląd aplikacji - Można użyć bootstrapa / własny bardziej rozbudowany projekt lub bardziej rozszerzony projekt z przykładu
- [ ] **5**: Płatności - podpięcie np. PayU

## Technologie

- [Spring Boot](https://spring.io/projects/spring-boot)
- [JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate](https://hibernate.org/)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Bootstrap](https://getbootstrap.com/)
- [Docker](https://www.docker.com/)

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

- #### Profil `docker`:

  Profil dedykowany dla uruchomienia z [`docker compose`](https://docs.docker.com/compose/).
  Podpina się do bazy danych PostgreSQL uruchamianej razem z aplikacją z URLem bazowanym
  na nazwie hosta pochodzącej z sieci wewnętrznej dockera.
  Baza jest aktualizowana do najnowszej wersji, a dane z niej persystują w utworzonym przez
  dockera [woluminie](https://docs.docker.com/storage/volumes/).

- #### Profil `prod`:

  Profil dedykowany dla uruchomienia w środowisku produkcyjnym.

## Uruchomienie

(najprostszy sposób z wykorzystaniem dockera na końcu sekcji)

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

## Obsługa

Domyślnie utworzony użytkownik:

- **email**: admin@bookystore.com
- **hasło**: password
- **rola**: admin
