import { useState, useEffect } from "react";
import { fetch_cinema_rooms } from "../services/cinema-room-service";

function RoomLayout({ rows, seatsPerRow, onClose }) {
    return (
        <div className="room-layout-overlay">
            <div className="room-layout" style={{overflowX: "auto"}}>
                <button className="btn btn-close" onClick={onClose}>&times;</button>
                <div className="screen-mock">
                    <span style={{ color: "black", marginLeft: "45%", opacity: "33%", fontWeight: "500" }}>
                        EKRAN
                    </span>
                </div>
                {Array.from({ length: rows }).map((_, rowIndex) => (
                    <div className="rows" key={rowIndex}>
                        <span className="row-identificator">{rowIndex + 1}</span>
                        {Array.from({ length: seatsPerRow }).map((_, seatIndex) => (
                            <div className="seat seat-icon" key={seatIndex}>
                                <span className="seat-identificator">{seatIndex + 1}</span>
                                <svg viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg" id={"seat1;row" + rowIndex + ";position" + seatIndex}>
                                    <g fill="white" stroke="gray" strokeWidth="5" >
                                        <rect id={"seat2;row" + rowIndex + ";position" + seatIndex} width="80" height="50" x="10" y="10" rx="10" ry="10" fill="gray" />
                                        <rect id={"seat3;row" + rowIndex + ";position" + seatIndex} width="60" height="20" x="20" y="70" rx="10" ry="10" fill="gray" />
                                    </g>
                                </svg>
                            </div>
                        ))}
                    </div>
                ))}
            </div>
        </div>
    );
}

export default function CinemaRooms() {
    const [cinemaRooms, setCinemaRooms] = useState([]);
    const [selectedRoom, setSelectedRoom] = useState(null);

    async function getCinemaRooms() {
        const fetchedCinemaRooms = await fetch_cinema_rooms();
        setCinemaRooms(fetchedCinemaRooms);
        console.log(fetchedCinemaRooms);
    }

    useEffect(() => {
        getCinemaRooms();
    }, []);

    return (
        <div className="container">
            <div className="card">
                <div className="card-header bg-dark text-white">
                    Sale kinowe
                </div>
                <div className="card-body bg-dark text-white">
                    {cinemaRooms.map((cinemaRoom) => (
                        <div
                            className="card m-2"
                            key={cinemaRoom.id}
                            onClick={() => setSelectedRoom(cinemaRoom)}
                            style={{ cursor: "pointer" }}
                        >
                            <div className="card-body bg-dark text-white">
                                <h5 className="card-title">{cinemaRoom.name}</h5>
                                <p className="card-text">Rzędy: {cinemaRoom.rows}</p>
                                <p className="card-text">Miejsca na rząd: {cinemaRoom.seatsPerRow}</p>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            {selectedRoom && (
                <RoomLayout
                    rows={selectedRoom.rows}
                    seatsPerRow={selectedRoom.seatsPerRow}
                    onClose={() => setSelectedRoom(null)}
                />
            )}
        </div>
    );
}