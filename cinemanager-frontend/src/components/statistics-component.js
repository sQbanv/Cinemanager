import { useEffect, useState } from "react";
import { fetchHighestAttendanceScreenings, fetchHighestRatedMovies, fetchMostChosenSeats, fetchTicketsSold } from "../services/statistics-service";
import { Link } from "react-router-dom";
import { fetch_cinema_rooms } from "../services/cinema-room-service";

export default function Statistics() {
    const [highestRatedMovies, setHighestRatedMovies] = useState([]);
    const [highestAttendanceScreenings, setHighestAttendanceScreenings] = useState([]);

    const [after, setAfter] = useState(null);
    const [before, setBefore] = useState(null);
    const [ticketsSold, setTicketsSold] = useState([]);

    const [options, setOptions] = useState([]);
    const [selectedOption, setSelectedOption] = useState(-1);
    const [selectedCinemaRoom, setSelectedCinemaRoom] = useState(null);
    const [mostChosenSeats, setMostChosenSeats] = useState([]);
    const [maxTickets, setMaxTickets] = useState(0);
    
    async function updateHighestRatedMovies() {
        setHighestRatedMovies(await fetchHighestRatedMovies())
    }

    async function updateHighestAttendanceScreenings() {
        setHighestAttendanceScreenings(await fetchHighestAttendanceScreenings())
    }

    async function updateTicketsSold(after, before) {
        setTicketsSold(await fetchTicketsSold(after, before))
    }

    async function updateOptions() {
        setOptions(await fetch_cinema_rooms());
    }

    function getTicketsSold(row, seatPosition) {
        const seat = mostChosenSeats.find(seat => seat.row === row && seat.position === seatPosition);
        return seat ? seat.ticketsSold : 0;
    }

    function getSeatColor(row, seatPosition) {
        const tickets = getTicketsSold(row, seatPosition);

        const ratio = tickets / maxTickets;
        
        let red, green, blue;
        if (ratio == 0) {
            return '#808080';
        }else if (ratio <= 0.5) {
            // Transition from gray to yellow
            const grayToYellowRatio = ratio / 0.5;
            red = Math.floor(128 + (255 - 128) * grayToYellowRatio);
            green = Math.floor(128 + (255 - 128) * grayToYellowRatio);
            blue = 0;
        } else {
            // Transition from yellow to red
            const yellowToRedRatio = (ratio - 0.5) / 0.5;
            red = 255;
            green = Math.floor(255 * (1 - yellowToRedRatio));
            blue = 0;
        }
        
        return `rgb(${red}, ${green}, ${blue})`;
    }

    async function updateMostChosenSeats() {
        if (selectedCinemaRoom !== undefined && selectedCinemaRoom !== null) {
            const fetchedMostChosenSeats = await fetchMostChosenSeats(selectedCinemaRoom.id)
            setMaxTickets(fetchedMostChosenSeats.reduce((max, current) => {
                return Math.max(max, current.ticketsSold)
            }, 0))
            setMostChosenSeats(fetchedMostChosenSeats)
        }
    }

    useEffect(() => {
        updateHighestRatedMovies();
        updateHighestAttendanceScreenings();
        updateOptions()
    }, []);

    useEffect(() => {
        updateTicketsSold(after, before);
    }, [after, before]);

    useEffect(() => {
        updateMostChosenSeats();
    }, [selectedCinemaRoom])

    return (
        <div className="container mt-5">
            <div className="row">
                <div className="col-md-6 mb-4">
                    <div className="card-stats card">
                        <div className="card-header">
                            <h5 className="card-title">Najwyżej oceniane filmy</h5>
                        </div>
                        <div className="card-body">
                            {highestRatedMovies ? 
                                <table className="table stats-table">
                                    <thead>
                                        <tr>
                                            <th scope="col">Tytuł</th>
                                            <th scope="col">Ocena</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {highestRatedMovies.map((movie, index) => (
                                            <tr key={index}>
                                                <td><Link className="table-link" to={`/movies/${movie.movie.id}`}>{movie.movie.title}</Link></td>
                                                <td>{movie.rating}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            : <></>}
                        </div>
                    </div>
                </div>
                <div className="col-md-6 mb-4">
                    <div className="card-stats card">
                        <div className="card-header">
                            <h5 className="card-title">Liczba biletów na film</h5>
                        </div>
                        <div className="card-body">
                            <div className="row mb-4">
                                <div className="col-md-6">
                                    <label htmlFor="afterDate" className="form-label">Od</label>
                                    <input
                                        type="date"
                                        id="afterDate"
                                        className="form-control"
                                        value={after ? after.toISOString().split('T')[0] : ''}
                                        onChange={(e) => {
                                            const date = e.target.value ? new Date(Date.UTC(new Date(e.target.value).getUTCFullYear(), new Date(e.target.value).getUTCMonth(), new Date(e.target.value).getUTCDate(), 0, 0, 0)) : null;
                                            setAfter(date);
                                        }}
                                    />
                                </div>
                                <div className="col-md-6">
                                    <label htmlFor="beforeDate" className="form-label">Do</label>
                                    <input
                                        type="date"
                                        id="beforeDate"
                                        className="form-control"
                                        value={before ? before.toISOString().split('T')[0] : ''}
                                        onChange={(e) => {
                                            const date = e.target.value ? new Date(Date.UTC(new Date(e.target.value).getUTCFullYear(), new Date(e.target.value).getUTCMonth(), new Date(e.target.value).getUTCDate(), 23, 59, 59)) : null;
                                            setBefore(date);
                                        }}
                                    />
                                </div>
                            </div>
                            {highestRatedMovies ? 
                                <table className="table stats-table">
                                    <thead>
                                        <tr>
                                            <th scope="col">Tytuł</th>
                                            <th scope="col">Bilety</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {ticketsSold.map((stats, index) => (
                                            <tr key={index}>
                                                <td><Link className="table-link" to={`/movies/${stats.movie.id}`}>{stats.movie.title}</Link></td>
                                                <td>{stats.ticketsSold}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            : <></>}
                        </div>
                    </div>
                </div>
                <div className="col-md-12 mb-4">
                    <div className="card-stats card">
                        <div className="card-header">
                            <h5 className="card-title">Seanse z największym obłożeniem sali</h5>
                        </div>
                        <div className="card-body">
                            {highestAttendanceScreenings ? 
                                <table className="table stats-table">
                                    <thead>
                                        <tr>
                                            <th scope="col">Tytuł</th>
                                            <th scope="col">Data</th>
                                            <th scope="col">Rodzaj</th>
                                            <th scope="col">Obłożenie</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {highestAttendanceScreenings.map((screening, index) => (
                                            <tr key={index}>
                                                <td><Link className="table-link" to={`/movies/${screening.screening.movie.id}`}>{screening.screening.movie.title}</Link></td>
                                                <td>
                                                    <Link className="table-link" to={`/order?screening=${screening.screening.id}`}>
                                                    {
                                                        new Date(screening.screening.startDate).toLocaleDateString("pl-PL", {
                                                            weekday: "short",
                                                            year: "numeric",
                                                            month: "2-digit",
                                                            day: "2-digit",
                                                            hour: "2-digit",
                                                            minute: "2-digit",
                                                        })
                                                    }
                                                    </Link>
                                                </td>
                                                <td>{screening.screening.screeningType.name}</td>
                                                <td>{parseFloat(screening.attendancePercentage * 100).toFixed(1)} %</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            : <></>}
                        </div>
                    </div>
                </div>
                <div className="col-md-12 mb-4">
                    <div className="card-stats card">
                        <div className="card-header">
                            <h5 className="card-title">Najczęściej wybierane siedzenia</h5>
                        </div>
                        <div className="card-body">
                            <div className="mb-3">
                                <select
                                    id="selectOption"
                                    className="form-select"
                                    value={selectedOption}
                                    onChange={(e) => {
                                        setSelectedOption(e.target.value)
                                        setSelectedCinemaRoom(e.target.value !== -1 ? options[e.target.value] : null)
                                    }}
                                >
                                    <option value={-1}>Wybierz salę</option>
                                    {options.map((option, index) => (
                                        <option key={index} value={index}>{option.name}</option>
                                    ))}
                                </select>
                            </div>
                            {selectedCinemaRoom ? Array.from({ length: selectedCinemaRoom.rows }).map((_, rowIndex) => (
                                <div className="rows" key={rowIndex}>
                                    <span className="row-identificator">{rowIndex + 1}</span>
                                    {Array.from({ length: selectedCinemaRoom.seatsPerRow }).map((_, seatIndex) => (
                                        <div className="seat seat-icon" key={seatIndex}>
                                            <span className="seat-identificator">{seatIndex + 1}</span>
                                            <svg viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg" id={"seat1;row" + rowIndex + ";position" + seatIndex}>
                                                <g fill="white" stroke={getSeatColor(rowIndex + 1, seatIndex + 1)} strokeWidth="5" >
                                                    <rect id={"seat2;row" + rowIndex + ";position" + seatIndex} width="80" height="50" x="10" y="10" rx="10" ry="10" fill={getSeatColor(rowIndex + 1, seatIndex + 1)} />
                                                    <rect id={"seat3;row" + rowIndex + ";position" + seatIndex} width="60" height="20" x="20" y="70" rx="10" ry="10" fill={getSeatColor(rowIndex + 1, seatIndex + 1)} />
                                                </g>
                                            </svg>
                                        </div>
                                    ))}
                                </div>
                            )) : <></>}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}