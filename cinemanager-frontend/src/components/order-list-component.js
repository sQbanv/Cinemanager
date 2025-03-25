import { useState, useEffect } from "react";
import { fetch_orders, fetch_order, order_payment } from "../services/order-service";
import { useNavigate } from "react-router-dom";
import { useParams } from "react-router-dom";

function Order({ order }) {
    const navigate = useNavigate();

    const {
        id,
        date,
        paid,
        totalPrice,
    } = order;

    const formattedDate = new Date(date).toLocaleDateString("pl-PL", {
        weekday: "long",
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
    });

    return (
        <div className="order-card" onClick={() => navigate(`/orders/${id}`)}>
            <p>Zamówienie nr {id}</p>
            <p>Data zamówienia: {formattedDate}</p>
            <p className={!paid ? "used-status" : ""}>Status: {paid ? "Zapłacone" : "Nie zapłacone"}</p>
            <p>Suma: {totalPrice} zł</p>
        </div>
    )
}

function OrderDetails() {
    const { id } = useParams();
    const [order, setOrder] = useState(null);

    async function getOrder() {
        const fetchedOrder = await fetch_order(id);
        setOrder(fetchedOrder);
        console.log(fetchedOrder);
    }

    useEffect(() => {
        getOrder();
    }, [id]);

    if (!order) {
        return <p>Zamówienie nie zostało znalezione</p>;
    }

    const {
        date,
        paid,
        tickets,
        totalPrice,
    } = order

    const formattedDate = (date) => {
        return new Date(date).toLocaleDateString("pl-PL", {
            weekday: "long",
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
        });
    } 

    return (
        <div className="container mt-5">
            <h2 className="mb-4">Szczegóły zamówienia</h2>
            <div className="card p-4 shadow-sm bg-dark text-light">
                <p><strong>Data zamówienia:</strong> {formattedDate(date)}</p>
                <p className={!paid ? "order-status" : ""} ><strong>Status:</strong> {paid ? "Zapłacone" : "Nie zapłacone"}</p>
                <p><strong>Suma:</strong> {totalPrice} zł</p>

                <h5 className="mt-4">Bilety:</h5>
                <ul className="list-group">
                    {tickets.map(ticket => (
                        <li key={ticket.id} className="list-group-item bg-secondary text-light">
                            <p>{ticket.id} | <strong>{ticket.screening.movie.title}</strong> | {ticket.screening.screeningType.name} | {formattedDate(ticket.screening.startDate)} | Rząd: {ticket.seatRow} | Miejsce: {ticket.seatPosition} | {ticket.discounted ? "Ulgowy" : "Normalny"}</p>
                        </li>
                    ))}
                </ul>

                {!paid && (
                    <button
                        className="btn btn-primary mt-3"
                        type="button"
                        onClick={() => { order_payment(id); }}
                    >
                        Zapłać
                    </button>
                )}
            </div>
        </div>
    );
}

function OrderList() {
    const [orders, setOrders] = useState([]);
    const [page, setPage] = useState(0);
    const [toPay, setToPay] = useState(false);
    const [pageInfo, setPageInfo] = useState({ totalPages: 0, totalElements: 0 });

    async function getOrders(paid) {
        const fetchedOrders = await fetch_orders(page, 5, paid, false);
        setOrders(fetchedOrders.content);
        setPageInfo(fetchedOrders.page);
    }

    useEffect(() => {
        toPay ? getOrders(false) : getOrders(null);
    }, [page, toPay]);

    const canGoBack = page > 0;
    const canGoForward = page + 1 < pageInfo.totalPages;

    return (
        <div>
            <div className="btn-holder">
                <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => setToPay(!toPay)}
                >
                    {toPay ? "Pokaż wszystkie zamówienia" : "Pokaż zamówienia do zapłaty"}
                </button>
            </div>
            <div>
                {orders.map((order) => (
                    <Order key={order.id} order={order} />
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
    )
}

export { OrderList, OrderDetails };