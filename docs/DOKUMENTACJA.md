# CineManager

## Model bazodanowy
### Schemat
![](./img/Database4.png)

### Tabele
- `Users` - dane o użytkownikach
  - `first_name` - imię użytkownika
  - `last_name` - nazwisko użytkownika
  - `email` - mail wykorzystywany do logowania
  - `password` - zaszyfrowane hasło
  - `role` - rola użytkownika (`CUSTOMER`, `EMPLOYEE`, `MANAGER`, `ADMINISTRATOR`)

- `Movies` - dane o filmach
  - `title` - tytuł filmu
  - `description` - opis filmu
  - `director` - reżyser filmu
  - `poster_url` - adres url do plakatu filmu
  - `length` - długość filmu w minutach
  - `genre_id` - klucz obcy do tabeli `Genres`, oznaczający gatunek filmu

- `Genres` - dane o gatunkach filmowych
  - `name` - nazwa gatunku

- `Directors` - dane o reżyserach
  - `first_name` - imię/imiona reżysera
  - `last_name` - nazwisko reżysera

- `Reviews` - dane o recenzjach filmów dodane przez użytkowników
    - `user_id` - klucz obcy do tabeli `Users`, użytkownik wystawiający recenzję
    - `movie_id` - klucz obcy do tabeli `Movies`, oceniany film
    - `rating` - wystawiona ocena
    - `content` - treść recenzji filmu
    - `review_date` - data i godzina wystawienia recenzji

- `CinemaRooms` - dane o salach kinowych
  - `name` - nazwa sali
  - `rows` - liczba rzędów siedzeń
  - `seats_per_row` - liczba siedzeń w każdym rzędzie

- `ScreningType` - dane o rodzajach seansów
  - `name` - nazwa rodzaju seansu (np. 2D, 3D)
  - `base_price` - podstawowa cena biletu
  - `discount_price` - ulgowa cena biletu

- `Screening` - dane o seansach filmowych
  - `movie_id` - klucz obcy do tabeli `Movies`, jaki film będzie na seansie
  - `room_id` - klucz obcy do tabeli `CinemaRooms`, gdzie odbędzie się seans
  - `start_date` - data seansu filmowego
  - `screening_type_id` - klucz obcy do tabeli `ScreeningType`, jakiego rodzaju jest seans

- `Orders` - dane o zamówieniach
  - `user_id` - klucz obcy do tabeli `Users`, kto dokonał zamówienia
  - `date` - data zamówienia
  - `total_price` - całkowita wartość zamówienia
  - `paid` - informacja, czy zamówienie zostało opłacone
  - `cancelled` - informacja, czy zamówienie zostało anulowane (np. z powodu nieopłacenia w wymaganym czasie)

- `Tickets` - dane o biletach
  - `user_id` - klucz obcy do tabeli `Users`, do kogo należy bilet
  - `screening_id` - klucz obcy do tabeli `Screenings`, na jaki seans
  - `seat_row` - numer rzędu w sali kinowej
  - `seat_position` - numer siedzenia w rzędzie
  - `used` - ważność biletu, po użyciu `true`
  - `discounted` - informacja czy bilet jest biletem ulgowym
  - `order_id` - klucz obcy do tabeli `Orders`, w jakim zamówieniu zakupiono dany bilet

### Relacje

- `Reviews` 
  - Recenzja jest wystawiana przez jednego użytkownika na jeden film

- `Movies`
  - Film jest przypisany do jednego gatunku i do jednego reżysera

- `Screenings`
  - Seans jest przypisany do jednej sali kinowej i do jednego filmu oraz posiada jeden rodzaj

- `Tickets`
  - Bilet jest przypisany do jednego zamówienia, do jednego użytkownika i na jeden seans

- `Orders`
  - Zamówienie jest przypisane do jednego użytkownika

### Mapowanie

W projekcie zastosowano Hibernate jako framework do mapowania obiektowo-relacyjnego (`ORM`). Każda tabela w bazie danych została odwzorowana na odpowiednią klasę encji w Javie, co pozwala na wygodne operowanie danymi w kodzie przy użyciu obiektów.

Poniżej znajduje się przykład mapowania tabeli `Users`

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    @Column(length = 64)
    private String firstName;

    @NotBlank
    @Column(length = 64)
    private String lastName;

    @Email
    @Column(length = 128, unique = true)
    private String email;

    @NotBlank
    @Column(length = 256)
    private String password;

    @NotNull
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters & Setters ...
}
```

## Model obiektowy
Poniżej przedstawiony jest diagram UML modelu obiektowego dla użytkownika.
![](./img/UML.png)

Pozostałe elementy systemu są zbudowane analogicznie - klasa modelowa, repozytorium, serwis oraz kontroler.

![](./img/UML2.png)

## Autentykacja i autoryzacja
### Autentykacja
Dostęp do API jest zabezpieczony za pomocą Spring Security. Autentykacja użytkowników odbywa się za pośrednictwem tokenów JWT. 

Użytkownik wysyła zapytanie POST o token na `/api/token`, podając w nagłówku Authorization zakodowane za pomocą Base64 `email:haslo`.

```
Base64("jan@mail.com:password") -> amFuQG1haWwuY29tOnBhc3N3b3Jk

POST /api/token
Authorization: Basic amFuQG1haWwuY29tOnBhc3N3b3Jk
```

Jeśli podane dane są poprawne, użytkownik otrzymuje token ważny przez godzinę.

```
HTTP/1.1 200 

header.payload.signature
```

Token zawiera algorytm szyfrowania użyty do wygenerowania podpisu, niewrażliwe dane użytkownika, czas wygaśnięcia tokenu oraz podpis wygenerowany za pomocą klucza prywatnego na serwerze.

Przykładowa zawartość tokenu:
```json
// Header
{
  "alg": "RS256"
}

// Payload
{
  "iss": "self",
  "sub": "jan@mail.com",
  "exp": 1733220945,
  "iat": 1733217345,
  "scope": "ROLE_ADMINISTRATOR",
  "userId": 1,
}

// Signature
```

Jeżeli dane są niepoprawne, użytkownik otrzyma odpowiedź 401 Unauthorized.

Aby uzyskać dostęp do zabezpieczonych endpointów, należy dodać do zapytania token w nagłówku Authorization.

Przykładowe zapytanie:
```
GET /api/users
Authorization: Bearer <token>
```

### Autoryzacja
Użytkownicy mogą mieć jedną z 4 ról:

- ADMINISTRATOR - administrator systemu,
- MANAGER - menedżer,
- EMPLOYEE - zwykły pracownik,
- CUSTOMER - klient.

Role są hierarchiczne, więc np. ADMINISTRATOR posiada uprawnienia MANAGER, EMPLOYEE i CUSTOMER.

Dzięki podziałowi na role, różni użytkownicy posiadają różne uprawnienia do danych dostępnych za pomocą endpointów.

### Klasy potrzebne do zabezpieczenia API
#### SecurityConfig
Główna klasa Spring Security, pozwalająca na zabezpieczenie systemu. W metodzie 
`securityFilterChain(HttpSecurity http)` ustawiane są główne ustawienia:
- ustawienia CORS i CSRF,
- stworzenie serwera zasobów OAuth2, do autentykacji przez JWT
- tryb STATELESS.

W metodach `roleHierarchy` oraz `methodSecurityExpressionHandler` ustawiana jest hierarchia ról.

W metodach `jwtDecoder`, `jwtEncoder` oraz klasie `CustomAuthenticatorConverter` tworzymy system tworzenia, kodowania tokenów JWT oraz przetwarzania ról użytkowników.

W metodzie `passwordEncoder` tworzymy używany w aplikacji algorytm szyfrowania haseł.

#### RsaKeyProperties
Rekord umożliwa odczyt kluczy ustawionych w `application.properties`, potrzebnych do tworzenia podpisu tokenu JWT.

Na potrzeby developmentu zostały utworzone przykładowe klucze `private.example.pem` i `public.example.pem`, których **NIE MOŻNA BĘDZIE STOSOTWAĆ W ŚRODOWISKU PRODUKCYJNYM**. W folderze `/resourses/certs` został umieszczony skrypt `generate.sh` umożliwiający wygenerowanie nowych kluczy.

#### JpaUserDetailsService
Aby zapewnić poprawne działanie systemu logowania, zaimplementowany został interfejs `UserDetailsService` - metoda `loadUserByUsername(String email)` korzysta z bazy danych użytkowników, aby odnaleźć i zwrócić użytkownika o podanym mailu lub zwraca błąd, gdy taki użytkownik nie istnieje. Korzystamy w tej metodzie z klasy `SecurityUser`.

#### SecurityUser
Klasa opakowująca `User`a, będącego encją JPA. Implementuje `UserDetails` potrzebny do poprawnego działania systemu logowania i autoryzacji.

#### AuthService
Serwis pozwalający w prosty sposób za pomocą metody `hasRole(userRole, authorities)` sprawdzić, czy użytkownik o danych `authorities`, czyli danej roli, spełnia wymagania `userRole` - ta metoda jest wymagana ze względu na hierarchiczność ról.

## REST API

### Autentykacja
#### POST /api/token

##### Body: 
puste

##### Nagłówki:
Authorization: 'Basic ' + Base64(email + ':' + password)

##### Zwraca:
200 OK - Logowanie powiodło się. W polu danych zwracany jest wygenerowany token.

401 UNAUTHORIZED - Logowanie nie powiodło się z powodu błędnego hasła lub emailu.


#### POST /api/register

##### Body: 
firstName, lastName, email, password (plaintext), role (ADMINISTRATOR, MANAGER, EMPLOYEE, CUSTOMER)

##### Nagłówki:
`Opcjonalnie` Authorization: 'Bearer ' + Token

##### Specyfikacja:
Niezalogowana osoba może stworzyć tylko 
użytkownika o roli Customer. Administrator oraz Manager mogą tworzyć użytkowników o roli swojej lub niższej.

##### Zwraca:
201 CREATED - Dodatkowo header Location z URL /api/users/{id} oraz w body id, firstname, lastname, email, role.

403 FORBIDDEN - Nie ma prawa do stworzenia osoby tej rangi.

409 CONFLICT - Jeśli istnieje już użytkownik z takim adresem email. 

### Użytkownicy
#### GET /api/users

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Użytkownik musi być zalogowany. Ponadto jego rola musi być równa co najmniej Managerowi.

##### Zwraca:
200 OK - W polu data zwracana jest lista użytkowników zarejestrowanych w systemie.

403 FORBIDDEN - Nie ma prawa do otrzymania listy użytkowników.

#### GET /api/users/{id}

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Użytkownik musi być zalogowany. Użytkownik może podejrzeć sam siebie lub jego ranga musi być równa co najmniej managerowi.

##### Zwraca:
200 OK - W polu data zwracane są dane o użytkowniku.

403 FORBIDDEN - Nie ma prawa do otrzymania danych o użytkowniku.

#### DELETE /api/users/{id}

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Użytkownik musi być zalogowany. Ponadto jego rola musi być równa co najmniej Managerowi. Pozwala na usunięcie siebie lub użytkownika co najwyżej tej samej rangi.

##### Zwraca:
204 NO CONTENT - Usunięcie powiodło się.

403 FORBIDDEN - Nie można usunąć użytkownika takiej rangi.

### Gatunki
#### GET /api/genres

##### Specyfikacja:
Pozwala na wylistowanie wszystkich gatunków filmowych, do których należą filmy w bazie kina.

##### Zwraca:
200 OK - Lista gatunków postaci `id, name`.

#### GET /api/genres/{id}

##### Specyfikacja:
Pozwala na wylistowanie gatunku filmowego.

##### Zwraca:
200 OK - Gatunek postaci `id, name`.

#### POST /api/genres

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala dodać gatunek filmowy. Należy podać jedynie pole `name`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
201 CREATED - Dodanie gatunku powiodło się. Dodatkowo w odpowiedzi jest header `Location` z URL `/api/genres/{id}` oraz w body znajduje się `id, name`.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### PUT /api/genres/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala uaktualnić gatunek filmowy. W body należy podać jedynie pole `name`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
204 NO CONTENT - Uaktualnienie powiodło się.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

### Reżyserzy
#### GET /api/directors

##### Specyfikacja:
Pozwala na wylistowanie wszystkich reżyserów filmowych, którzy tworzyli filmy w bazie kina.

##### Zwraca:
200 OK - Lista reżyserów postaci `id, firstName, lastName`.

#### GET /api/directors/{id}

##### Specyfikacja:
Pozwala na wylistowanie danych reżysera filmowego.

##### Zwraca:
200 OK - Dane reżysera postaci `id, firstName, lastName`.

#### POST /api/directors

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala dodać reżysera. Należy podać pola `firstName`, `lastName`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
201 CREATED - Dodanie reżysera powiodło się. Dodatkowo w odpowiedzi jest header `Location` z URL `/api/directors/{id}` oraz w body znajduje się `id, firstName, lastName`.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### PUT /api/directors/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala uaktualnić dane reżysera. W body należy podać pola `firstName`, `lastName`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
204 NO CONTENT - Uaktualnienie powiodło się.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

### Filmy
#### GET /api/movies

##### Specyfikacja:
Pozwala na wylistowanie wszystkich filmów w bazie kina. Wspiera paginację oraz sortowanie (np. `?page=0&size=10&sort=length,desc`).
Dodatkowo, można filtrować listę, podając odpowiedni(e) parametr(y) w zapytaniu:
- `title` - listuje filmy o tytule zawierającej podaną frazę,
- `genre` - listuje filmy o gatunku o podanej nazwie,
- `minRating` - listuje filmy o ocenie co najmniej takiej, jak podana.

##### Zwraca:
200 OK - Lista filmów postaci `id, title, descrption, director (id, firstName, lastName), posterUrl, length, genre (id, name)`, znajdujące się pod kluczem `content`. 
Dodatkowo dostępne są dane strony w `page` takie jak `number, size, totalElements, totalPages`.

#### GET /api/movies/{id}

##### Specyfikacja:
Pozwala na wylistowanie danych filmu.

##### Zwraca:
200 OK - Film postaci `id, title, descrption, director (id, firstName, lastName), posterUrl, length, genre (id, name)`.

400 BAD REQUEST - Film o podanym id nie istnieje.

#### GET /api/movies/{id}/rating
##### Specyfikacja:
Pozwala na uzyskanie oceny filmu na podstawie recenzji. Jest to liczba od `0.0` do `5.0`, z jednym miejscem po przecinku. 
Recenzja jest zaokrąglana zgodnie z typowymi zasadami zaokrąglania. Jeżeli film nie posiada żadnych rezencji, zwraca `0`.

##### Zwraca:
200 OK - Ocena filmu od `0.0` do `5.0`.

400 BAD REQUEST - Film o podanym id nie istnieje.

#### POST /api/movies

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala dodać film. 
Należy podać pola `movie(title, descrption, directorId, length, genreId), poster`, gdzie `poster` to plik plakatu.
Wymaga uprawnień co najmniej managera.

##### Zwraca:
201 CREATED - Dodanie filmu powiodło się. Dodatkowo w odpowiedzi jest header `Location` z URL `/api/movies/{id}` 
oraz w body znajduje się `id, title, descrption, director (id, firstName, lastName), posterUrl, length, genre (id, name)`.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### PUT /api/movies/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala uaktualnić film. W body należy podać pola `title, descrption, directorId, posterUrl, length, genreId`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
204 NO CONTENT - Uaktualnienie powiodło się.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### GET /api/movies/highest-rated
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Zwraca najwyżej oceniane filmy wraz z oceną. Wymaga rangi co najmniej managera.

##### Zwraca:
200 OK - Dane postaci `movie, rating`

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### GET /api/movies/tickets-sold
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Zwraca filmy oraz liczbę biletów sprzedanych (z zamówień zapłaconych, nieanulowanych) na seansy. Wymaga rangi co najmniej managera.
Pozwala filtrować po seansach przed (`before`) lub po (`after`) danej dacie. Dane są posortowane malejąco po liczbie biletów.

##### Zwraca:
200 OK - Lista postaci `movie, ticketsSold`. Film zwracany anlogicznie do endpointu dla filmu po id.

400 BAD REQUEST - Filtry są niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

### Sale kinowe
#### GET /api/cinema-rooms
##### Specyfikacja:
Pozwala na wylistowanie wszystkich sal kinowych.

##### Zwraca:
200 OK - Lista gatunków postaci `id, name, rows, seatsPerRow`.

#### GET /api/cinema-rooms/{id}
##### Specyfikacja:
Pozwala na wylistowanie danych konkretnej sali kinowej

##### Zwraca:
200 OK - Dane w postaci `id, name, rows, seatsPerRow`.

400 BAD REQUEST - Podana sala nie istnieje.

#### GET /api/cinema-rooms/{id}/most-chosen-seats
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Zwraca najczęściej wybierane siedzenia w sali o podanym id. Wymaga rangi co najmniej managera.
Zwraca jedynie siedzenia, które zostały zakupione (z zamówienia zapłaconego, nieanulowanego) co najmniej raz. 

##### Zwraca:
200 OK - Lista postaci `row, position, ticketsSold`

400 BAD REQUEST - Podana sala nie istnieje.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### POST /api/cinema-rooms
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala dodać nową salę kinową. Wymaga rangi co najmniej managera. W body należy podać `name, rows, seatsPerRow`.

##### Zwraca:
201 CREATED - Dodanie sali powiodło się. Dodatkowo w odpowiedzi jest header `Location` z URL `/api/cinema-rooms/{id}` oraz w body znajduje się `id, name, rows, seatsPerRow`.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### PUT /api/cinema-rooms/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala uaktualnić salę kinową. W body należy podać pola `name, rows, seatsPerRow`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
204 NO CONTENT - Uaktualnienie powiodło się.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

### Typy seansów
#### GET /api/screening-types
##### Specyfikacja:
Pozwala na wylistowanie wszystkich rodzajów seansów (np. 2D, 3D).

##### Zwraca:
200 OK - Lista gatunków postaci `id, name, basePrice, discountPrice`.

#### GET /api/screening-types/{id}
##### Specyfikacja:
Pozwala na wylistowanie danych konkretnego typu seansu.

##### Zwraca:
200 OK - Dane w postaci `id, name, basePrice, discountPrice`.

400 BAD REQUEST - Podana sala nie istnieje.

#### POST /api/screening-types
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala dodać nowy typ seansu. Wymaga rangi co najmniej managera. W body należy podać `name, basePrice, discountPrice`.

##### Zwraca:
201 CREATED - Dodanie typu seansu powiodło się. Dodatkowo w odpowiedzi jest header `Location` z URL `/api/screening-types/{id}` oraz w body znajduje się `id, name, basePrice, discountPrice`.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### PUT /api/cinema-rooms/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala uaktualnić typ seansu. W body należy podać pola `name, basePrice, discountPrice`. Wymaga uprawnień co najmniej managera.

##### Zwraca:
204 NO CONTENT - Uaktualnienie powiodło się.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

### Seanse
#### GET /api/screenings

##### Specyfikacja:
Pozwala na wylistowanie wszystkich seansów. Wspiera paginację oraz sortowanie (np. `?page=0&size=10&sort=startDate,desc`). 
Dodatkowo, można filtrować listę, podając odpowiedni(e) parametr(y) w zapytaniu:
- `movieId` - listuje tylko seanse dla danego filmu,
- `after` - listuje tylko seanse odbywające się po (`>=`) danej dacie (przekazanej w formacie ISO-8601, np. `2025-01-01T12:00:00`).

##### Zwraca:
200 OK - Lista seansów postaci `id, startDate, screeningType, movie, cinemaRoom`, znajdujące się pod kluczem `content`. 
Film, typ seansu i sala zwracane są w analogicznej postaci, co endpointy GET tych zasobów dla danego id.
Dodatkowo dostępne są dane strony w `page` takie jak `number, size, totalElements, totalPages`.

400 BAD REQUEST - Podane filtry są niepoprawne.

#### GET /api/screenings/{id}

##### Specyfikacja:
Pozwala na wylistowanie danych seansu.

##### Zwraca:
200 OK - Seans postaci `id, startDate, screeningType, movie, cinemaRoom`.
Film, typ seansu i sala zwracane są w analogicznej postaci, co endpointy GET tych zasobów dla danego id.

#### POST /api/screenings

##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala dodać seans. Należy podać pola `startDate, screeningTypeId, movieId, cinemaRoomId`. 
Wymaga uprawnień co najmniej managera.
Zostaje sprawdzone, czy dany seans nie pokrywa się z jakimś innym seansem odbywającym się w tym samym czasie w tej samej sali.

##### Zwraca:
201 CREATED - Dodanie seansu powiodło się. Dodatkowo w odpowiedzi jest header `Location` z URL `/api/screenings/{id}` oraz w body znajduje się `id, startDate, screeningType, movie, cinemaRoom`.
Film, typ seansu i sala zwracane są w analogicznej postaci, co endpointy GET tych zasobów dla danego id.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne (w tym gdy istnieje inny seans w tym samym czasie i w tej samej sali).

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### PUT /api/screenings/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala uaktualnić seans. W body należy podać pola `startDate, screeningTypeId, movieId, cinemaRoomId`. 
Wymaga uprawnień co najmniej managera.
Zostaje sprawdzone, czy dany seans nie pokrywa się z jakimś innym seansem odbywającym się w tym samym czasie w tej samej sali.

##### Zwraca:
204 NO CONTENT - Uaktualnienie powiodło się.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne (w tym gdy istnieje inny seans w tym samym czasie i w tej samej sali).

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### GET /api/screenings/{id}/seats
##### Specyfikacja:
Zwraca listę zajętych siedzeń na dany seans postaci: `row, position`.

##### Zwraca:
200 OK - Lista zajętych siedzeń na dany seans postaci: `row, position`

400 BAD REQUEST - Podany seans nie istnieje.

#### GET /api/screenings/highest-attendance
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Zwraca seanse na które został wykupiony choć jeden bilet wraz z procentem obłożenia sali. Wymaga rangi co najmniej managera.

##### Zwraca:
200 OK - Dane postaci `screening, percentage`

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

### Zamówienia
#### GET /api/orders
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Pozwala na wylistowanie wszystkich zamówień. 
Dla roli przynajmniej managera wylistowuje wszystkie zamówienia, w przeciwnym przypadku jedynie zamówienia użytkownika, którego token został przesłany.
Wspiera paginację oraz sortowanie (np. `?page=0&size=10&sort=date,desc`). 
Dodatkowo, można filtrować listę, podając odpowiedni(e) parametr(y) w zapytaniu:
- `userId` - listuje tylko zamówienia dla danego użytkownika (lecz zwykły użytkownik dalej ma dostęp jedynie do swoich danych),
- `after` - listuje tylko zamówienia dokonane po (`>=`) danej dacie (przekazanej w formacie ISO-8601, np. `2025-01-01T12:00:00`),
- `before` - listuje tylko zamówienia dokonane przed (`<=`) daną datą (przekazaną w formacie ISO-8601, np. `2025-01-01T12:00:00`),
- `paid` - listuje tylko zamówienia zapłacone,
- `cancelled` - listuje tylko zamówienia anulowane.

##### Zwraca:
200 OK - Lista zamówień w postaci analogicznej do endpointu dla zamówienia o danym id, znajdujące się pod kluczem `content`. 
Dodatkowo dostępne są dane strony w `page` takie jak `number, size, totalElements, totalPages`.

400 BAD REQUEST - Podane filtry są niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

#### GET /api/orders/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Zwraca dane dotyczące danego zamówienia: `id, date, cancelled, paid, totalPrice, userId, tickets`. 
`tickets` to lista biletów w analogicznym formacie jak zwracany przez endpoint dla biletów.
Wymaga, aby zamówienie należało do użytkownika, którego token jest przesłany lub rangi przynajmniej managera.
`userId` jest dodawane w przypadku posiadania roli co najmniej managera.

##### Zwraca:
200 OK - Dane zamówienia

400 BAD REQUEST - Zamówienie nie istnieje lub użytkownik nie ma uprawnień, aby je zobaczyć.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

#### POST /api/orders
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Wymaga podania tokenu JWT, na podstawie którego wystawiane są bilety.
W body przesyłamy:
 - `screeningId` - id seansu,
 - `tickets` - lista zamówionych miejsc postaci `row, seatNumber, ticketType`, gdzie `ticketType` to `REGULAR` dla zwykłego biletu lub `DISCOUNTED` dla biletu ulgowego.

##### Zwraca:
201 CREATED - Zamówienie oraz nagłówek Location

400 BAD REQUEST - w przypadku podania złego użytkownika, złego seansu lub kiedy któreś miejsce zostało już zajęte

#### POST /api/orders/{id}/payment
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Symuluje otrzymanie informacji o płatności od zewnętrznego systemu płatności. Wymaga roli administratora.

##### Zwraca:
204 NO CONTENT - Zapisano infromację o udanej płatności.

400 BAD REQUEST - Zamówienie nie istnieje lub jest już opłacone.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

### Bilety

#### GET /api/tickets
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Zwraca listę "przydatnych" biletów przypisanych do użytkownika, którego token został przesłany.
"Przydatne" bilety to te, które nie zostały użyte, dotyczą seansów w przyszłości (z buforem 2 godziny do tyłu), 
z zamówień opłaconych i nieanulowanych.

W przypadku potrzeby zwrócenia wszystkich biletów (z zamówień opłaconych i nieanulowanych), 
należy podać parametr GET `past=true`.

Wspiera paginację oraz sortowanie (np. `?page=0&size=10&sort=id,desc`). 

##### Zwraca:
200 OK - Lista zamówień w postaci analogicznej do endpointu dla biletu o danym id, znajdujące się pod kluczem `content`. 
Dodatkowo dostępne są dane strony w `page` takie jak `number, size, totalElements, totalPages`.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

#### GET /api/tickets/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Zwraca dane o bilecie o podanym id, jeżeli przypisany jest do użytkownika, którego token został przesłany.

##### Zwraca:
200 OK - Bilet postaci `id, discounted, screening, seatPosition, seatRow, used`, 
gdzie `screening` jest w postaci analogicznej do endpointu dla seansu o danym id.

400 BAD REQUEST - Bilet nie istnieje lub użytkownik nie posiada uprawnień do jego wyświetlenia.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

### Recenzje
#### GET /api/reviews
##### Specifykacja:
Zwraca listę wszystkich recenzji. 
Wspiera paginację oraz sortowanie (np. `?page=0&size=10&sort=rating,desc`). 
Dodatkowo, można filtrować listę, podając odpowiedni(e) parametr(y) w zapytaniu:
- `movieId` - listuje tylko recenzje dla danego filmu,
- `userId` - listuje tylko recenzje danego użytkownika.

##### Zwraca:
200 OK - Lista postaci `id, movieId, user (id, firstName), rating, content, reviewDate`, znajdujące się pod kluczem `content`. 
Dodatkowo dostępne są dane strony w `page` takie jak `number, size, totalElements, totalPages`.

400 BAD REQUEST - Podane filtry są niepoprawne.

#### GET /api/reviews/{id}
##### Specifykacja:
Zwraca dane recenzji o podanym id.

##### Zwraca:
200 OK - Dane recenzji postaci `id, movieId, user (id, firstName), rating, content, reviewDate`.

400 BAD REQUEST - Recenzja nie istnieje.

#### POST /api/reviews
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Dodaje recenzję, jako autora ustawiając właściciela podanego tokenu. Należy podać `movieId, rating, content`.
`rating` musi być liczbą od `0.0` do `5.0`, z krokiem `0.5` (`0.0, 0.5, 1.0, 1.5, ..., 5.0`).

##### Zwraca:
201 CREATED - Dodanie recenzji powiodło się. Dodatkowo w odpowiedzi jest header `Location` z URL `/api/reviews/{id}` oraz w body znajduje się `id, movieId, user (id, firstName), rating, content`.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

#### PUT /api/reviews
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Aktualizuje recenzję. Należy podać `movieId, rating, content`. Użytkownik, którego token został przesłany, musi być autorem recenzji lub co najmniej managerem.
`rating` musi być liczbą od `0.0` do `5.0`, z krokiem `0.5` (`0.0, 0.5, 1.0, 1.5, ..., 5.0`).

##### Zwraca:
204 NO CONTENT - Aktualizacja recenzji powiodła się.

400 BAD REQUEST - Nie podano wszystkich wymaganych pól w body lub są one niepoprawne.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.

#### DELETE /api/reviews/{id}
##### Nagłówki:
Authorization: 'Bearer ' + Token

##### Specyfikacja:
Usuwa recenzję. Należy podać `movieId, rating, content`. Użytkownik, którego token został przesłany, musi być autorem recenzji lub co najmniej managerem.

##### Zwraca:
204 NO CONTENT - Usunięcie recenzji powiodło się.

401 UNAUTHORIZED - Nagłówek `Authorization` nie został podany w zapytaniu.

403 FORBIDDEN - Brak uprawnień do wykonania akcji.