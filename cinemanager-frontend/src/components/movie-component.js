import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { fetch_movies, fetch_movie, fetch_rating, fetch_reviews, post_review, delete_review, update_review } from "../services/movie-service";
import { fetch_genries } from "../services/genre-service";
import { fetch_screenings_by_movie_id } from "../services/screening-service";
import { API_STATIC_URL } from "../services/api-url-config"
import Rating from "react-rating"
import { getCurrentUser } from "../services/authentication-service";

function MovieCard({ movie }) {
    const navigate = useNavigate();

    const {
        id,
        title,
        posterUrl,
    } = movie;

    return (
        <div className="card m-2" onClick={() => navigate(`/movies/${id}`)}>
            <img src={API_STATIC_URL + posterUrl} className="card-img-top" alt={title} />
            <div className="card-overlay">{title}</div>
        </div>
    )
}

function ScreeningCard({ screening }) {
    const navigate = useNavigate();

    const {
        id,
        startDate,
        screeningType: {name: screeningTypeName},
        cinemaRoom: {name: roomName},
    } = screening;

    const formattedDate = new Date(startDate).toLocaleDateString("pl-PL", {
        weekday: "long",
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
    });

    return (
        <div className="card m-2" onClick={() => navigate(`/order?screening=${id}`)}>
            <div className="card-body">
                <h5 className="card-title">{screeningTypeName}</h5>
                <p className="card-text">{formattedDate}</p>
                <p className="card-text">{roomName}</p>
            </div>
        </div>
    )
}

function Movie() {
    const { id } = useParams();
    const [movie, setMovie] = useState(null);
    const [rating, setRating] = useState(0.0);

    const user = getCurrentUser();
    const [currentUser, setCurrentUser] = useState(user ? JSON.parse(atob(user.split('.')[1])) : null);
    const hasAccess = currentUser && (currentUser.scope == "ROLE_MANAGER" || currentUser.scope == "ROLE_ADMINISTRATOR");

    const [reviewPage, setReviewPage] = useState(0);
    const [reviewPageInfo, setReviewPageInfo] = useState({ totalPages: 0, totalElements: 0 });
    const [reviews, setReviews] = useState([]);

    const [userReview, setUserReview] = useState("");
    const [userReviewRating, setUserReviewRating] = useState(0.0);

    const [screenings, setScreenings] = useState([])
    const [screeningPage, setScreeningPage] = useState(0)
    const [screeningPageInfo, setScreeningPageInfo] = useState({ totalPages: 0, totalElements: 0 })

    async function getMovie() {
        const fetchedMovie = await fetch_movie(id);
        setMovie(fetchedMovie);
    }

    async function getMovieRating() {
        const fetchedRating = await fetch_rating(id);
        setRating(fetchedRating);
    }

    async function getMovieReviews() {
        const fetchedReviews = await fetch_reviews(id, reviewPage, 3);
        setReviews(fetchedReviews.content);
        setReviewPageInfo(fetchedReviews.page);
    }

    async function getMovieScreenings() {
        const fetchedScreenings = await fetch_screenings_by_movie_id(id, screeningPage, 3);
        setScreenings(fetchedScreenings.content);
        setScreeningPageInfo(fetchedScreenings.page);
    }
    
    useEffect(() => {
        getMovie();
        getMovieRating();
    }, [id]);

    useEffect(() => {
        getMovieReviews();
    }, [id, reviewPage]);

    useEffect(() => {
        getMovieScreenings();
    }, [id, screeningPage]);

    if (!movie) {
        return <p>Film nie został znaleziony</p>;
    }

    async function handleSubmitReview() {
        if (userReview !== "") {
            post_review(id, userReview, userReviewRating)
            .then(res => {
                setUserReview("");
                setUserReviewRating(0);
                setReviewPage(0);
                getMovieRating();
                getMovieReviews();
            })
            .catch(err => console.warn(err))
        }
    }

    async function handleDeleteReview(id) {
        delete_review(id)
        .then(res => {
            getMovieRating();
            getMovieReviews();
        })
        .catch(err => console.warn(err))
    }

    async function handleEditReview(reviewId, content, rating) {
        let newContent = window.prompt("Podaj nową treść recenzji:", content);
        let newRating = Number(window.prompt("Podaj nową ocenę (0.0 - 5.0):", rating));

        if (newContent !== null && newRating !== null) {
            update_review(reviewId, id, newContent, newRating)
            .then(res => {
                getMovieRating();
                getMovieReviews();
            })
            .catch(err => console.warn(err))
        }
    }

    const canGoBackReview = reviewPage > 0;
    const canGoForwardReview = reviewPage + 1 < reviewPageInfo.totalPages;

    const canGoBackScreening = screeningPage > 0;
    const canGoForwardScreening = screeningPage + 1 < screeningPageInfo.totalPages;

    const {
        title,
        genre: {
            name
        },
        length,
        director: {
            firstName,
            lastName
        },
        posterUrl,
        description,
    } = movie;

    const FilledStar = (props) => <svg xmlns="http://www.w3.org/2000/svg" width={props.size} height={props.size} fill="currentColor" className="bi bi-star-fill" viewBox="0 0 16 16"><path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z"/></svg>
    const EmptyStar = (props) => <svg xmlns="http://www.w3.org/2000/svg" width={props.size} height={props.size} fill="currentColor" className="bi bi-star" viewBox="0 0 16 16"><path d="M2.866 14.85c-.078.444.36.791.746.593l4.39-2.256 4.389 2.256c.386.198.824-.149.746-.592l-.83-4.73 3.522-3.356c.33-.314.16-.888-.282-.95l-4.898-.696L8.465.792a.513.513 0 0 0-.927 0L5.354 5.12l-4.898.696c-.441.062-.612.636-.283.95l3.523 3.356-.83 4.73zm4.905-2.767-3.686 1.894.694-3.957a.56.56 0 0 0-.163-.505L1.71 6.745l4.052-.576a.53.53 0 0 0 .393-.288L8 2.223l1.847 3.658a.53.53 0 0 0 .393.288l4.052.575-2.906 2.77a.56.56 0 0 0-.163.506l.694 3.957-3.686-1.894a.5.5 0 0 0-.461 0z"/></svg>

    return (
        <div>
            <div className="container">
                <div className="row">
                    <div className="col-md-4">
                        <img src={API_STATIC_URL + posterUrl} alt={title} className="img-thumbnail" />
                    </div>
                    <div className="col-md-8">
                        <h1>{title}</h1>
                        <div className="container row" style={{gap: 10, marginBottom: 10, alignItems: "center"}}>
                        <div>
                        <Rating
                            fullSymbol={<FilledStar size={25} />}
                            emptySymbol={<EmptyStar size={25} />}
                            fractions={2}
                            initialRating={rating}
                            readonly
                        />
                        </div>
                        
                            <div>
                                ({rating})
                            </div>
                        </div>
                        
                        <h4>{name} | {length} min</h4>
                        <h4>Reżyser: {firstName} {lastName}</h4>
                        <p>{description}</p>
                    </div>
                </div>
                <br></br>
                <div className="container">
                    <h3>Najbliższe seanse:</h3>
                    <div className="d-flex align-items-center">
                        <button 
                            className="btn btn-outline-secondary me-3"
                            onClick={() => setScreeningPage((prev) => Math.max(0, prev - 1))}
                            disabled={!canGoBackScreening}
                        >
                            &#8592;
                        </button>

                        <div className="d-flex overflow-hidden flex-grow-1">
                            {screenings && screenings.length > 0 ? (
                                <div className="row flex-nowrap">
                                    {screenings.map(screening => (
                                        <div className="col" key={screening.id}>
                                            <ScreeningCard screening={screening} />
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <div className="d-flex justify-content-center align-items-center w-100">
                                    <p>Brak seansów</p>
                                </div>
                            )}
                        </div>
                        
                        <button 
                            className="btn btn-outline-secondary ms-3"
                            onClick={() => setScreeningPage((prev) => prev + 1)}
                            disabled={!canGoForwardScreening}
                        >
                            &#8594;
                        </button>
                    </div>
                </div>
                <div className="row d-flex justify-content-center mb-5 mt-5">
                    <div className="col-md-8">
                        <div className="list-group mb-3" style={{gap: 10}}>
                            <div className="list-group-item" style={{backgroundColor: "#212529", color: "white"}}>
                                {currentUser 
                                    ? <>
                                        <textarea className="form-control mt-2 review-box" rows="3" placeholder="Wpisz swoją recenzję..." onChange={(e) => setUserReview(e.target.value)} value={userReview}></textarea>
                                        <div className="d-flex justify-content-between mt-2">
                                            <div className="d-flex align-items-center">
                                                <div style={{marginTop: -3}}>
                                                    <Rating
                                                        fullSymbol={<FilledStar size={20} />}
                                                        emptySymbol={<EmptyStar size={20} />}
                                                        fractions={2}
                                                        initialRating={userReviewRating}
                                                        onChange={setUserReviewRating}
                                                    />
                                                </div>
                                            </div>
                                            <button className="btn btn-sm btn-primary" onClick={handleSubmitReview}>Opublikuj</button>
                                        </div>
                                    </> 
                                    : <p className="m-0">Zaloguj się, aby napisać recenzję.</p>
                            }
                        </div>
                            {
                                reviews.map(review => 
                                    
                                    <div className="list-group-item" style={{backgroundColor: "#212529", color: "white"}} key={review.id}>
                                        <div className="d-flex justify-content-between align-items-center">
                                            <div className="d-flex align-items-center" style={{gap: 10}}>
                                               <div style={{marginTop: -3}}>
                                                    <Rating
                                                        fullSymbol={<FilledStar size={20} />}
                                                        emptySymbol={<EmptyStar size={20} />}
                                                        fractions={2}
                                                        initialRating={review.rating}
                                                        readonly
                                                    />
                                                </div>
                                                <h5 className="mb-0">{review.user.firstName}</h5>
                                            </div>
                                            <div className="d-flex align-items-center" style={{gap: 5, minHeight: 40}}>
                                                {currentUser && currentUser.userId === review.user.id || hasAccess ? 
                                                    <>
                                                        <button className="btn btn-sm btn-warning me-2" onClick={() => handleEditReview(review.id, review.content, review.rating)}>Edytuj</button>
                                                        <button className="btn btn-sm btn-danger" onClick={() => handleDeleteReview(review.id)}>Usuń</button>
                                                    </>  
                                                : <></>}
                                            </div>
                                        </div>
                                        <p className="mt-2">{review.content}</p>

                                    </div>
                                
                                )
                            }
                        </div>
                        <div className="d-flex justify-content-center">
                            {canGoBackReview && (
                                <button
                                    type="button"
                                    className="btn btn-secondary mr-1"
                                    onClick={() => setReviewPage((prev) => Math.max(0, prev - 1))}
                                >
                                    Poprzednia strona
                                </button>
                            )}
                            {canGoForwardReview && (
                                <button
                                    type="button"
                                    className="btn btn-secondary mr-1"
                                    onClick={() => setReviewPage((prev) => prev + 1)}
                                >
                                    Kolejna strona
                                </button>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

function Movies() {
    const [movies, setMovies] = useState([]);
    const [page, setPage] = useState(0);
    const [pageInfo, setPageInfo] = useState({ totalPages: 0, totalElements: 0 });
    const [selectedGenre, setSelectedGenre] = useState("");
    const [genres, setGenres] = useState([]);
    const [searchQuery, setSearchQuery] = useState("");
    const [minRating, setMinRating] = useState(0.0);

    async function getMovies() {
        const fetchedMovies = await fetch_movies(page, 16, selectedGenre, searchQuery, minRating);
        setMovies(fetchedMovies.content);
        setPageInfo(fetchedMovies.page);
    }

    async function getGenres() {
        const fetchedGenres = await fetch_genries();
        setGenres(fetchedGenres);
    }

    const handleReset = () => {
        setSearchQuery("")
        setMinRating(0.0)
        setSelectedGenre("")
    }

    useEffect(() => {
        getGenres();
    }, []);

    useEffect(() => {
        getMovies();
    }, [page, selectedGenre, searchQuery, minRating]);

    const canGoBack = page > 0;
    const canGoForward = page + 1 < pageInfo.totalPages;

    const FilledStar = (props) => <svg xmlns="http://www.w3.org/2000/svg" width={props.size} height={props.size} fill="currentColor" className="bi bi-star-fill" viewBox="0 0 16 16"><path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z"/></svg>
    const EmptyStar = (props) => <svg xmlns="http://www.w3.org/2000/svg" width={props.size} height={props.size} fill="currentColor" className="bi bi-star" viewBox="0 0 16 16"><path d="M2.866 14.85c-.078.444.36.791.746.593l4.39-2.256 4.389 2.256c.386.198.824-.149.746-.592l-.83-4.73 3.522-3.356c.33-.314.16-.888-.282-.95l-4.898-.696L8.465.792a.513.513 0 0 0-.927 0L5.354 5.12l-4.898.696c-.441.062-.612.636-.283.95l3.523 3.356-.83 4.73zm4.905-2.767-3.686 1.894.694-3.957a.56.56 0 0 0-.163-.505L1.71 6.745l4.052-.576a.53.53 0 0 0 .393-.288L8 2.223l1.847 3.658a.53.53 0 0 0 .393.288l4.052.575-2.906 2.77a.56.56 0 0 0-.163.506l.694 3.957-3.686-1.894a.5.5 0 0 0-.461 0z"/></svg>
    
    return (
        <div className="container">
            <div className="row mb-4">
                <div className="mb-3 offset-md-1">
                    <select
                        className="form-select"
                        value={selectedGenre}
                        onChange={(e) => {
                            setSelectedGenre(e.target.value);
                            setPage(0);
                        }}
                    >
                        <option value="">Wszystkie gatunki</option>
                        {genres.map((genre) => (
                            <option key={genre.id} value={genre.name}>
                                {genre.name}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="mb-3 offset-md-1">
                    <div className="d-flex justify-content-between mt-2">
                        <div className="d-flex align-items-center">
                            <div style={{marginTop: -3}}>
                                <Rating
                                    fullSymbol={<FilledStar size={25} />}
                                    emptySymbol={<EmptyStar size={25} />}
                                    fractions={2}
                                    initialRating={minRating}
                                    onChange={setMinRating}
                                />
                            </div>
                        </div>
                    </div>
                </div>
                <div className="md-1 offset-md-1">
                    <input 
                        type="button"
                        className="form-control"
                        value="Reset"
                        onClick={handleReset}
                    />
                </div>
                <div className="col-md-10 offset-md-1">
                    <input 
                        type="text"
                        className="form-control"
                        placeholder="Wyszukaj film..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                </div>
            </div>

            
            <div className="container">
                <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3">
                    {movies.map((movie) => (
                        <div className="col" key={movie.id}>
                            <MovieCard movie={movie} />
                        </div>
                    ))}
                </div>
            </div>
            <div className="btn-holder">
                {canGoBack && (
                    <button
                        type="button"
                        className="btn btn-light mr-1"
                        onClick={() => setPage((prev) => Math.max(0, prev - 1))}
                    >
                        Poprzednia strona
                    </button>
                )}
                {canGoForward && (
                    <button
                        type="button"
                        className="btn btn-light mr-1"
                        onClick={() => setPage((prev) => prev + 1)}
                    >
                        Kolejna strona
                    </button>
                )}
            </div>
        </div>
    )
}

export {
    Movies,
    Movie
}