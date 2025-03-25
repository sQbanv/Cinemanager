import { useEffect, useState } from "react";
import { fetch_screening_info, fetch_screening_seats, place_order } from "../services/order-service";

export default function Order() {

    const Steps = {
        SEAT_SELECTION: 0,
        TICKET_TYPE_SELECTION: 1,
        ORDER_CONFIRMATION: 2
    }

    const [screeningInfo, setScreeningInfo] = useState();
    const [takenSeats, setTakenSeats] = useState([]);
    const [selectedSeats, setSelectedSeats] = useState([]);
    const [orderStep, setOrderStep] = useState(Steps.SEAT_SELECTION);

    const urlParams = new URLSearchParams(window.location.search);

    function changeStep(next) {
        if (selectedSeats.length != 0) {
            setOrderStep(next ? orderStep + 1 : orderStep - 1)
        } else {
            window.alert("Musisz wybrać przynajmniej jedno siedzenie")
        }
    }

    function determineSeatStatus(row, position) {
        for (var seat of takenSeats) {
            if (seat.row == row && seat.position == position) {
                return 1
            }
        }
        for (var seat of selectedSeats) {
            if (seat.row == row && seat.position == position) {
                return 2
            }
        }
        return 0
    }

    function selectSeat(row, position) {
        selectedSeats.push(
            {
                "row": row,
                "position": position,
                "type": "Normalny"
            }
        )
        document.getElementById("seat1;row" + row + ";position" + position).className.baseVal = "selected-seat"
    }

    function deselectSeat(row, position) {
        for (var seat of selectedSeats) {
            if (seat.row == row && seat.position == position) {
                selectedSeats.splice(selectedSeats.indexOf(seat), 1)
            }
        }
        document.getElementById("seat1;row" + row + ";position" + position).className.baseVal = ""
    }

    function changeSeatType(row, position) {
        for (var seat of selectedSeats) {
            if (seat.row == row && seat.position == position) {
                if(seat.type == "Ulgowy"){
                    seat.type = "Normalny"
                    break
                } else {
                    seat.type = "Ulgowy"
                    break
                }
            }
        }
        document.getElementById("ticket0;row" + row + ";position" + position).innerHTML = "Rodzaj biletu: " + seat.type
    }

    function GenerateRows(screeningInfo) {
        const layout = []
        if (screeningInfo != undefined) {
            for (var i = 0; i < screeningInfo.cinemaRoom.rows; i++) {
                const row = []
                for (var j = 0; j < screeningInfo.cinemaRoom.seatsPerRow; j++) {
                    row.push(j)
                }
                layout.push(row)
            }
        }
        return layout
    }

    function Seat(row, seatNumber, status) {

        const SeatStatus = {
            FREE: 0,
            TAKEN: 1,
            SELECTED: 2
        }

        var seatStatus = status;

        return (
            <div className={"seat seat-icon" + (seatStatus == SeatStatus.TAKEN ? " disabled-seat" : "")}
                id={"seat0;row" + row + ";position" + seatNumber} onClick={() => {
                    if (seatStatus != SeatStatus.TAKEN) {
                        if (seatStatus == SeatStatus.FREE) {
                            selectSeat(row, seatNumber)
                            seatStatus = SeatStatus.SELECTED
                        } else {
                            deselectSeat(row, seatNumber)
                            seatStatus = SeatStatus.FREE
                        }
                    }
                }}>
                <span className="seat-identificator">
                    {seatNumber}
                </span>
                <svg viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg" id={"seat1;row" + row + ";position" + seatNumber} className={seatStatus == SeatStatus.SELECTED ? "selected-seat" : ""}>
                    <g fill="white" stroke={seatStatus == SeatStatus.TAKEN ? "grey" : "green"} strokeWidth="5" >
                        <rect id={"seat2;row" + row + ";position" + seatNumber} width="80" height="50" x="10" y="10" rx="10" ry="10" fill={seatStatus == SeatStatus.TAKEN ? "grey" : "green"} />
                        <rect id={"seat3;row" + row + ";position" + seatNumber} width="60" height="20" x="20" y="70" rx="10" ry="10" fill={seatStatus == SeatStatus.TAKEN ? "grey" : "green"} />
                    </g>
                </svg>
            </div>
        )
    }

    function Ticket(row, position, type) {

        return (
            <div>
                <hr className="divider-horizontal" />
                <span id={"ticket0;row" + row + ";position" + position}>
                    Rodzaj biletu: {type}
                </span>
                <br />
                Rząd: {row} | Miejsce: {position}
                <br />
                <div class="dropdown">
                    <button class="btn btn-light" type="button" onClick={() => {changeSeatType(row, position)}}>
                        Zmień rodzaj biletu
                    </button>   
                </div>
            </div>
        )
    }

    function Summary(){

        var no_regular = 0;
        var no_discount = 0;
        var total_price = 0;

        for(var ticket of selectedSeats){
            if(ticket.type == "Ulgowy"){
                no_discount+=1
                total_price+=screeningInfo.screeningType.discountPrice
            } else {
                no_regular+=1
                total_price+=screeningInfo.screeningType.basePrice
            }
        }

        return (
            <div>
                <h2>Podsumowanie</h2>
                Ilość biletów: {no_discount+no_regular}
                <br/>
                W tym uglowych: {no_discount}, normalnych: {no_regular}
                <br/>
                <b>Razem: {total_price}zł</b>
                <br/>
                <button class="btn btn-light cnf-btn" type="button" onClick={() => {
                        place_order(selectedSeats, screeningInfo.id)
                    }}>
                        Potwierdź zamówienie
                </button> 
            </div>
        )
    }

    useEffect(() => {

        async function markSeats(takenSeats) {
            const seats = []
            for (var takenSeat of takenSeats) {
                seats.push(
                    {
                        "row": takenSeat.row,
                        "position": takenSeat.position
                    }
                )
            }
            setTakenSeats(seats)
        }

        async function getSeats(screeningId) {
            const fetchedSeats = (await fetch_screening_seats(screeningId)).data

            markSeats(fetchedSeats)
        }

        async function getScreeningInfo(screeningId) {
            const fetchedInfo = (await fetch_screening_info(screeningId)).data
            setScreeningInfo(fetchedInfo)

            getSeats(screeningId)
        }

        getScreeningInfo(urlParams.get('screening'))
    }, [])

    return (
        <div>
            {orderStep == Steps.SEAT_SELECTION && (<div className="order-details" id="seat-details">
                <div className="screen-mock"><span style={{ "color": "black", "margin-left": "45%", "opacity": "33%", "fontWeight": "500" }}>EKRAN</span></div>
                {GenerateRows(screeningInfo).map((row, rowIndex) => {
                    return (
                        <div className="rows">
                            <span className="row-identificator">
                                {rowIndex}
                            </span>
                            {row.map((seatNumber, seatIndex) => {
                                return Seat(rowIndex, seatNumber, determineSeatStatus(rowIndex, seatNumber))
                            })}
                        </div>
                    )
                })}
            </div>)}

            {orderStep == Steps.TICKET_TYPE_SELECTION && (
                <div className="order-details ticket-types">
                    <h2>Wybierz rodzaj biletów</h2>
                    <h4>Ceny:</h4>
                    <h5>Normalny: {screeningInfo.screeningType.basePrice}zł | Ulgowy: {screeningInfo.screeningType.discountPrice}zł</h5>
                    {selectedSeats.map((seat, index) => {
                        return Ticket(seat.row, seat.position, seat.type)
                    })}
                </div>
            )}

            {orderStep == Steps.ORDER_CONFIRMATION && (
                <div className="order-details ticket-types">
                    {Summary()}
                </div>
            )}

            <div className="btn-holder">
                {orderStep != Steps.SEAT_SELECTION && (<button type="button" className="btn btn-light page-btn" onClick={() => changeStep(false)}>Wróć</button>)}
                {orderStep != Steps.ORDER_CONFIRMATION && (<button type="button" className="btn btn-light page-btn" onClick={() => changeStep(true)}>Dalej</button>)}
            </div>

        </div>

    )
}