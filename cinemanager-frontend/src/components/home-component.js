import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom";
import { API_STATIC_URL } from "../services/api-url-config";
import { fetch_popular_movies } from "../services/movie-service";

function MovieCard({ movie }) {
    const navigate = useNavigate();

    const {
        id,
        title,
        posterUrl,
    } = movie;

    return (
        <div className="card m-2 recommended" onClick={() => navigate(`/movies/${id}`)}>
            <img src={API_STATIC_URL + posterUrl} className="img-thumbnail" alt={title} />
            <div className="card-overlay">{title}</div>
        </div>
    )
}

export default function Home() {

    const [popularMovies, setPopularMovies] = useState([]);

    async function getPopularMovies() {
        const fetchedMovies = await fetch_popular_movies();
        setPopularMovies(fetchedMovies);
    }

    useEffect(() => {
        getPopularMovies();
    }, [])

    return (
        <div style={{"display":"flex", "flexDirection":"column"}}>
            <h3 style={{"margin":"auto", "marginTop":"1em", "marginBottom":"1em"}}>Najlepiej oceniane filmy z ostatniego tygodnia</h3>
            {popularMovies.map(movie => (
                <MovieCard movie={movie} />
            ))}
        </div>
    )
}