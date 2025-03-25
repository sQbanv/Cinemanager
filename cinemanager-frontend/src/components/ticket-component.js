import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { fetch_tickets, fetch_ticket } from "../services/ticket-service";
import { API_STATIC_URL } from "../services/api-url-config"
import { useParams } from "react-router-dom";

function Ticket({ ticket }) {
    const navigate = useNavigate();

    const {
        id,
        seatRow,
        seatPosition,
        used,
        screening: {
            startDate,
            screeningType: { name: screeningTypeName },
            movie: { title, length,},
            cinemaRoom: { name: roomName },
        },
    } = ticket;

    const formattedDate = new Date(startDate).toLocaleDateString("pl-PL", {
        weekday: "long",
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
    });

    const handleClick = () => {
        navigate(`/ticket/${id}`)
    }

    return (
        <div className={`ticket-card ${used ? "ticket-used" : ""}`} onClick={handleClick} key={id}>
            <h3>{title}</h3>
            <div className="ticket-info">
                <p>Czas trwania: {length} min</p>
            </div>
            <p>Rodzaj seansu: {screeningTypeName}</p>
            <p>{formattedDate}</p>
            <div className="seat-info">
                <p>ID: {id}, Rząd: {seatRow}, Miejsce: {seatPosition}</p>
                <p className="room-name">Sala: {roomName}</p>
            </div>
            {used && <p className="used-status">Bilet użyty</p>}
        </div>
    )
}

function TicketDetails() {
    const { id } = useParams()
    const [ticket, setTicket] = useState(null)
    
    async function getTicket() {
        const fetchedTicket = await fetch_ticket(id);
        setTicket(fetchedTicket)
    }

    useEffect(() => {
        getTicket();
    }, [id])

    if (!ticket) {
        return <p>Bilet nie został znaleziony</p>
    }

    const {
        seatRow,
        seatPosition,
        used,
        screening: {
            startDate,
            screeningType: {
                name: screeningTypeName, 
                basePrice,
                discountPrice
            },
            movie: {
                title,
                director: {
                    firstName,
                    lastName
                },
                posterUrl,
                length,
                genre: {
                    name: genreName,
                },
            },
            cinemaRoom: {
                name: cinemaRoomName,
            }
        },
        discounted,
    } = ticket;

    const formattedDate = new Date(startDate).toLocaleDateString("pl-PL", {
        weekday: "long",
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
    });

    return (
        <div className="container mt-5">
            <h2 className="mb-4 text-center">{title}</h2>
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <img src={API_STATIC_URL + posterUrl} alt={title} className="img-fluid rounded shadow-sm mb-4" />
                    <div className="card p-4 shadow-sm bg-dark text-light">
                        <p><strong>Gatunek:</strong> {genreName}</p>
                        <p><strong>Reżyser:</strong> {firstName} {lastName}</p>
                        <p><strong>Czas trwania:</strong> {length} min</p>
                        <p><strong>Rodzaj seansu:</strong> {screeningTypeName}</p>
                        <p><strong>Data seansu:</strong> {formattedDate}</p>
                        <p><strong>ID biletu:</strong> {id}</p>
                        <p><strong>Rząd:</strong> {seatRow}, <strong>Miejsce:</strong> {seatPosition}</p>
                        <p><strong>Sala:</strong> {cinemaRoomName}</p>
                        <p><strong>Bilet:</strong> {discounted ? "Ulgowy" : "Normalny"}</p>
                        <p><strong>Cena:</strong> {discounted ? discountPrice : basePrice} zł</p>
                        {used && <p className="alert alert-info mt-3 bg-danger text-dark">Bilet użyty</p>}
                    </div>
                </div>
            </div>
        </div>
    );
}

function Tickets() {
    const [tickets, setTickets] = useState([]);
    const [page, setPage] = useState(0);
    const [past, setPast] = useState(false);
    const [pageInfo, setPageInfo] = useState({ totalPages: 0, totalElements: 0 });

    async function getTickets() {
        const fetchedTickets = await fetch_tickets(page, 5, past);
        setTickets(fetchedTickets.content);
        setPageInfo(fetchedTickets.page); 
    }

    useEffect(() => {
        getTickets();
    }, [page, past]);

    const canGoBack = page > 0;
    const canGoForward = page + 1 < pageInfo.totalPages;

    return (
        <div>
            <div className="btn-holder">
                <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => setPast(!past)}
                >
                    {past ? "Pokaż aktualne bilety" : "Pokaż wszystkie bilety"}
                </button>
            </div>
            <div>
                {tickets.map((ticket) => (
                    <Ticket key={ticket.id} ticket={ticket} />
                ))}
            </div>
            <div className="btn-holder">
                {canGoBack && (
                    <button
                        type="button"
                        className="btn btn-secondary mr-1"
                        onClick={() => setPage((prev) => Math.max(0, prev - 1))}
                    >
                        Poprzednia strona
                    </button>
                )}
                {canGoForward && (
                    <button
                        type="button"
                        className="btn btn-secondary mr-1"
                        onClick={() => setPage((prev) => prev + 1)}
                    >
                        Kolejna strona
                    </button>
                )}
            </div>
        </div>
    );
}

export {
    Tickets,
    TicketDetails
}