import { useNavigate } from "react-router-dom"

export default function User() {
    const navigation = useNavigate();

    return (
        <div className="btn-holder">
                <button
                    type="button"
                    className="btn btn-secondary mr-1"
                    onClick={() => navigation('/ticket')}
                >
                    Bilety
                </button>
                <button
                    type="button"
                    className="btn btn-secondary mr-1"
                    onClick={() => navigation('/orders')}
                >
                    Zamówienia
                </button>
        </div>
    )
}