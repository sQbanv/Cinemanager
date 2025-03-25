import { useState } from "react";
import { useNavigate } from "react-router-dom"
import { validate_ticket } from "../services/ticket-service";

export default function Employee() {
    const navigation = useNavigate();

    const [id, setId] = useState("");
    const [message, setMessage] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (id !== "") {
            validate_ticket(id).then(res => {
                setMessage({
                    message: `Poprawnie zeskanowano bilet ${id}!`,
                    type: "success"
                });
            }).catch(err => {
                setMessage({
                    message: `Wystąpił problem ze skanowaniem biletu ${id}! Jest on niepoprawny lub został już skasowany!`,
                    type: "danger"
                });
            })
        } else {
            setMessage({
                message: `Wpisz poprawny numer biletu!`,
                type: "danger"
            });
        }
    }

    return (
        <><h1>Skanuj bilet</h1>
        {message ? <div className={"alert alert-" + message.type}>{message.message}</div> : <></>}

        <form onSubmit={handleSubmit}>
        <p>Wpisz ID biletu:</p>
            <input type="number" value={id} onChange={(e) => setId(e.target.value)} />
            <button type="submit" onClick={handleSubmit}>Skanuj!</button>
        </form>
            </>
    )
}