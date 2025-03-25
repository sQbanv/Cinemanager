import { useEffect, useState } from "react";
import { fetch_genries, create_genre } from "../services/genre-service";
import { fetch_directors, create_director } from "../services/director-service";
import { create_cinema_room, fetch_cinema_rooms } from "../services/cinema-room-service";
import { create_screening_type, fetch_screening_types } from "../services/screening-type-service";
import { create_movie, fetch_movies } from "../services/movie-service";
import { create_screening } from "../services/screening-service";
import Select from 'react-select';

function AddScreening() {
    const [movie, setMovie] = useState("");
    const [cinemaRoom, setCinemaRoom] = useState("");
    const [screeningType, setScreeningType] = useState("");
    const [date, setDate] = useState("");

    const [movies, setMovies] = useState([]);
    const [cinemaRooms, setCinemaRooms] = useState([]);
    const [screeningTypes, setScreeningTypes] = useState([]);

    async function getMovies() {
        let fetchedMovies = await fetch_movies(0, 100, "", "", 0.0);

        fetchedMovies = fetchedMovies.content.map((movie) => ({
            value: movie.id,
            label: movie.title,
        }));
        setMovies(fetchedMovies)
    }

    async function getCinemaRooms() {
        const fetchedCinemaRooms = await fetch_cinema_rooms();
        setCinemaRooms(fetchedCinemaRooms)
    }

    async function getScreeningTypes() {
        const fetchedScreeningTypes = await fetch_screening_types();
        setScreeningTypes(fetchedScreeningTypes)
    }

    async function addScreening() {
        const result = await create_screening(movie, cinemaRoom, date, screeningType)

        if (result != undefined && result.status === 201) {
            alert(`Seans filmu ${result.data.movie.title} został dodany pomyślnie!`)
            window.location.reload()
        } else {
            alert("Wystąpił błąd podczas dodawania seansu.")
        }
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        addScreening()
    }

    useEffect(() => {
        getMovies();
        getCinemaRooms();
        getScreeningTypes();
    }, [])    

    return (
        <div className="container mt-5">
            <h2 className="mb-4">Dodaj seans:</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="movie" className="form-label">Film</label>
                    <Select
                        id="movie"
                        className="form-select"
                        placeholder="Wybierz film"
                        options={movies}
                        value={movies.find((mov) => mov.value === movie) || null}
                        onChange={(e) => setMovie(e ? e.value : "")}
                        required
                        isClearable
                        styles={{
                            control: (base) => ({
                                ...base,
                                backgroundColor: "#fff",
                                color: "#000",
                            }),
                            singleValue: (base) => ({
                                ...base,
                                color: "#000",
                            }),
                            menu: (base) => ({
                                ...base,
                                backgroundColor: "#fff",
                                color: "#000",
                            }),
                            option: (base, { isFocused }) => ({
                                ...base,
                                backgroundColor: isFocused ? "#ddd" : "#fff",
                                color: "#000",
                            }),
                        }}
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="cinemaRoom" className="form-label">Sala kinowa</label>
                    <select
                        id="cinemaRoom"
                        className="form-select"
                        value={cinemaRoom}
                        onChange={(e) => setCinemaRoom(Number(e.target.value))}
                        required
                    >
                        <option value=""></option>
                        {cinemaRooms.map((room) => (
                            <option key={room.id} value={room.id}>
                                {room.name}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="mb-3">
                    <label htmlFor="screeningType" className="form-label">Typ seansu</label>
                    <select
                        id="screeningType"
                        className="form-select"
                        value={screeningType}
                        onChange={(e) => setScreeningType(Number(e.target.value))}
                        required
                    >
                        <option value=""></option>
                        {screeningTypes.map((type) => (
                            <option key={type.id} value={type.id}>
                                {type.name}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="mb-3">
                    <label htmlFor="date" className="form-label">Data</label>
                    <input
                        type="datetime-local"
                        id="date"
                        className="form-control"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-light">
                    Dodaj seans
                </button>
            </form>
        </div>
    )
}

function AddMovie() {
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [length, setLength] = useState("");
    const [director, setDirector] = useState("");
    const [genre, setGenre] = useState("");
    const [poster, setPoster] = useState(null);
    
    const [genres, setGenres] = useState([])
    const [directors, setDirectors] = useState([])

    async function getGenres() {
        const fetchedGenres = await fetch_genries();
        setGenres(fetchedGenres)
    }

    async function getDirectors() {
        let fetchedDirectors = await fetch_directors();

        fetchedDirectors = fetchedDirectors.map((director) => ({
            value: director.id,
            label: `${director.firstName} ${director.lastName}`,
        }));

        setDirectors(fetchedDirectors)
    }

    async function addMovie() {
        const result = await create_movie(title, description, length, genre, director, poster)

        console.log(result)

        if (result.status === 201) {
            alert(`Film ${result.data.title} został dodany pomyślnie!`)
            window.location.reload()
        } else {
            alert("Wystąpił błąd podczas dodawania filmu.")
        }
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        addMovie()
    }

    useEffect(() => {
        getGenres();
        getDirectors();
    }, [])

    return (
        <div className="container mt-5">
            <h2 className="mb-4">Dodaj film:</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="name" className="form-label">Tytuł</label>
                    <input
                        type="text"
                        id="name"
                        className="form-control"
                        placeholder="Wprowadź tytuł filmu"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="description" className="form-label">Opis</label>
                    <textarea
                        id="description"
                        className="form-control"
                        placeholder="Wprowadź opis filmu"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="length" className="form-label">Długość (min)</label>
                    <input
                        type="number"
                        id="length"
                        className="form-control"
                        placeholder="Wprowadź długość filmu"
                        value={length}
                        onChange={(e) => setLength(Number(e.target.value))}
                        min="1"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="genre" className="form-label">Gatunek</label>
                    <select
                        id="genre"
                        className="form-select"
                        value={genre}
                        onChange={(e) => setGenre(Number(e.target.value))}
                        required
                    >
                        <option value=""></option>
                        {genres.map((genre) => (
                            <option key={genre.id} value={genre.id}>
                                {genre.name}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="mb-3">
                    <label htmlFor="director" className="form-label">Reżyser</label>
                    <Select
                        id="director"
                        className="form-select"
                        placeholder="Wybierz reżysera"
                        options={directors}
                        value={directors.find((dir) => dir.value === director) || null}
                        onChange={(e) => setDirector(e ? e.value : "")}
                        required
                        isClearable
                        styles={{
                            control: (base) => ({
                                ...base,
                                backgroundColor: "#fff",
                                color: "#000",
                            }),
                            singleValue: (base) => ({
                                ...base,
                                color: "#000",
                            }),
                            menu: (base) => ({
                                ...base,
                                backgroundColor: "#fff",
                                color: "#000",
                            }),
                            option: (base, { isFocused }) => ({
                                ...base,
                                backgroundColor: isFocused ? "#ddd" : "#fff",
                                color: "#000",
                            }),
                        }}
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="poster" className="form-label">Plakat</label>
                    <input
                        type="file"
                        id="poster"
                        className="form-control"
                        accept="image/*"
                        onChange={(e) => setPoster(e.target.files[0])}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-light">
                    Dodaj film
                </button>
            </form>
        </div>
    );
}

function AddGenre() {
    const [name, setName] = useState("");

    async function addGenre() {
        const result = await create_genre(name);

        console.log(result)

        if (result.status === 201) {
            alert(`Gatunek ${result.data.name} został dodany pomyślnie!`)
            window.location.reload()
        } else {
            alert("Wystąpił błąd podczas dodawania gatunku.")
        }
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        addGenre()
    }

    return (
        <div className="container mt-5">
            <h2 className="mb-4">Dodaj gatunek:</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="name" className="form-label">Nazwa gatunku</label>
                    <input
                        type="text"
                        id="name"
                        className="form-control"
                        placeholder="Wprowadź nazwę gatunku"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-light">
                    Dodaj gatunek
                </button>
            </form>
        </div>
    );
}

function AddDirector() {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");

    async function addDirector() {
        const result = await create_director(firstName, lastName)

        console.log(result)

        if (result.status === 201) {
            alert(`Reżyser ${result.data.firstName} ${result.data.lastName} został dodany pomyślnie!`);
            window.location.reload()
        } else {
            alert("Wystąpił błąd podczas dodawania reżysera.");
        }
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        addDirector()
    }

    return (
        <div className="container mt-5">
            <h2 className="mb-4">Dodaj reżysera:</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="firstName" className="form-label">Imię</label>
                    <input
                        type="text"
                        id="firstName"
                        className="form-control"
                        placeholder="Wprowadź imię reżysera"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        required
                        maxLength={64}
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="lastName" className="form-label">Nazwisko</label>
                    <input
                        type="text"
                        id="lastName"
                        className="form-control"
                        placeholder="Wprowadź nazwisko reżysera"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        required
                        maxLength={64}
                    />
                </div>
                <button type="submit" className="btn btn-light">
                    Dodaj reżysera
                </button>
            </form>
        </div>
    )
}

function AddScreeningType() {
    const [name, setName] = useState("");
    const [basePrice, setBasePrice] = useState("");
    const [discountPrice, setDiscountPrice] = useState("");

    async function AddScreeningType() {
        const result = await create_screening_type(name, basePrice, discountPrice);

        if (result.status === 201) {
            alert(`Typ seansu ${result.data.name} został dodany pomyślnie!`);
            window.location.reload()
        } else {
            alert("Wystąpił błąd podczas dodawania typu seansu.");
        }
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        AddScreeningType()
    }

    return (
        <div className="container mt-5">
            <h2 className="mb-4">Dodaj typ seansu:</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="name" className="form-label">Nazwa typu seansu</label>
                    <input
                        type="text"
                        id="name"
                        className="form-control"
                        placeholder="Wprowadź nazwę typu seansu"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                        maxLength={32}
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="basePrice" className="form-label">Cena normalna</label>
                    <input
                        type="number"
                        step={0.01}
                        id="basePrice"
                        className="form-control"
                        placeholder="Wprowadź cenę normalną"
                        value={basePrice}
                        onChange={(e) => setBasePrice(Number(e.target.value))}
                        min={0}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="discountPrice" className="form-label">Cena ze zniżką</label>
                    <input
                        type="number"
                        step={0.01}
                        id="discountPrice"
                        className="form-control"
                        placeholder="Wproawdź cenę ze zniżką"
                        value={discountPrice}
                        onChange={(e) => setDiscountPrice(Number(e.target.value))}
                        min={0}
                        required
                    />
                </div>
                <button type="submit" className="btn btn-light">
                    Dodaj typ seansu
                </button>
            </form>
        </div>
    );
}

function AddCinemaRoom() {
    const [name, setName] = useState("");
    const [rows, setRows] = useState("");
    const [seatsPerRow, setSeatsPerRow] = useState("");

    async function addCinemaRoom() {
        const result = await create_cinema_room(name, rows, seatsPerRow);
        
        if (result.status === 201) {
            alert(`Sala kinowa ${result.data.name} została dodana pomyślnie!`);
            window.location.reload()
        } else {    
            alert("Wystąpił błąd podczas dodawania sali kinowej.");
        }
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        addCinemaRoom();
    }

    return (
        <div className="container mt-5">
            <h2 className="mb-4">Dodaj sale kinową:</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label htmlFor="name" className="form-label">Nazwa sali</label>
                    <input
                        type="text"
                        id="name"
                        className="form-control"
                        placeholder="Wprowadź nazwę sali"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                        maxLength={32}
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="rows" className="form-label">Liczba rzędów</label>
                    <input
                        type="number"
                        id="rows"
                        className="form-control"
                        placeholder="Wprowadź liczbę rzędów"
                        value={rows}
                        onChange={(e) => setRows(Number(e.target.value))}
                        min="1"
                        max="50"
                        required
                    />
                </div>
                <div className="mb-3">
                    <label htmlFor="seatsPerRow" className="form-label">Liczba miejsc</label>
                    <input
                        type="number"
                        id="seatsPerRow"
                        className="form-control"
                        placeholder="Wprowadź liczbę miejsc"
                        value={seatsPerRow}
                        onChange={(e) => setSeatsPerRow(Number(e.target.value))}
                        min="1"
                        max="50"
                        required
                    />
                </div>
                <button type="submit" className="btn btn-light">
                    Dodaj salę
                </button>
            </form>
        </div>
    );
}

export { 
    AddCinemaRoom,
    AddDirector,
    AddScreeningType,
    AddMovie,
    AddGenre,
    AddScreening
}