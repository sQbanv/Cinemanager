import { useEffect, useState } from "react"
import { fetch_user_info, update_user_info } from "../services/user-service";
import { getCurrentUser } from "../services/authentication-service";

export default function Profile(){

    const [loggedUserId, setLoggedUserId] = useState(JSON.parse(atob(getCurrentUser().split('.')[1])).userId);
    const [userInfo, setUserInfo] = useState(undefined);
    const [userName, setUserName] = useState("");
    const [userLastName, setUserLastName] = useState("");

    async function getUserInfo() {
        const fetchedInfo = await fetch_user_info(loggedUserId);
        setUserInfo(fetchedInfo);
    }

    useEffect(() => {
        getUserInfo();
    }, [])

    if(userInfo == undefined){
        return (
            <div>
                <h3>Ładowanie danych o użytkowniku</h3>
            </div>
        )
    }

    const changeUserDetails = (e) => {
        e.preventDefault();
        let user_details = {
            firstName: userInfo.firstName,
            lastName: userInfo.lastName
        }
        if(!userName == "") user_details["firstName"] = userName;
        if(!userLastName == "") user_details["lastName"] = userLastName;
        update_user_info(loggedUserId, user_details);
    }

    return (
        <div style={{"display":"flex", "flexDirection":"column", "gap":"2em", "margin":"auto", "marginTop":"2em", "borderColor":"white", "width":"80%", "height":"fit-content", "borderStyle":"solid", "borderRadius":"5px", "padding":"4em", "borderWidth": "0.05em"}}>
            <h1>Dane użytkownika</h1>
            <h4>Email: {userInfo.email}</h4>
            <form style={{"display":"contents"}} onSubmit={changeUserDetails}>
                <div style={{"display":"flex", "flexDirection":"row"}}>
                    <h4>Imię: {userInfo.firstName}</h4>
                    <textarea
                            id="userName"
                            className="form-control profile-form"
                            placeholder="Zmień"
                            value={userName}
                            onChange={(e) => setUserName(e.target.value)}
                            rows="1"
                        />
                </div>
                <div style={{"display":"flex", "flexDirection":"row", "height":"1.5em"}}>
                    <h4>Nazwisko: {userInfo.lastName}</h4>
                    <textarea
                            id="userLastName"
                            className="form-control profile-form"
                            placeholder="Zmień"
                            value={userLastName}
                            onChange={(e) => setUserLastName(e.target.value)}
                            rows="1"
                        />
                </div>
                <button type="submit" className="btn btn-primary">
                    Zmień dane
                </button>
            </form>
        </div>
    )
}