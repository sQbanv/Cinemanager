import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import { useEffect, useState } from "react";
import { delete_user, fetch_users } from "../services/user-service";
import { getCurrentUser, add_user } from "../services/authentication-service";
import isEmail from "validator/lib/isEmail";

const required = value => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                To pole jest wymagane do usunięcia użytkownika
            </div>
        );
    }
}

const email_valid = value => {
    if (!isEmail(value)) {
        return (
            <div className="alert alert-danger" role="alert">
                Podany email jest nieprawidłowy
            </div>
        );
    }
}

export default function Manager() {
    const [id, setId] = useState();
    const [form, setForm] = useState();
    const [checkBtn, setCheckBtn] = useState();
    const [content, setContent] = useState([]);

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [role, setRole] = useState("CUSTOMER");


    const user = getCurrentUser();
    const [currentUser, setCurrentUser] = useState(user ? JSON.parse(atob(user.split('.')[1])) : null);
    const hasAccess = (currentUser.scope == "ROLE_MANAGER" || currentUser.scope == "ROLE_ADMINISTRATOR");

    function onChangeEmail(e) {
        setEmail(e.target.value);
    }
    function onChangePassword(e) {
        setPassword(e.target.value);
    }
    function onChangeFirstName(e) {
        setFirstName(e.target.value);
    }
    function onChangeLastName(e) {
        setLastName(e.target.value);
    }
    function onChangeRole() {
        setRole(document.getElementById("roleSelect").value);
    }
    function onChangeId(e) {
        setId(e.target.value);
    }

    function handleAddUser(e) {
        e.preventDefault();

        form.validateAll();

        if (checkBtn.context._errors.length === 0) {
            add_user(email, firstName, lastName, password, role).then(
                () => {
                    alert("Rejestracja użytkownika powiodła się");
                    window.location.reload();
                },
                error => {
                    alert("Rejestracja użytkownika nie powiodła się");
                }
            )
        }
    }

    function handleRemoveUser(e){
        e.preventDefault();

        delete_user(id).then(
            () => {
                alert("Usunięcie użytkownika powiodło się");
                window.location.reload();
            },
            error => {
                alert("Usunięcie użytkownika nie powiodło się");
            }
        )
    }

    useEffect(() => {
        async function show_users() {
            setContent(await fetch_users());
        }
        show_users();
    })

    return (
        (hasAccess && content != undefined &&
            (
                <div>
                    <div className="col-md-12">
                        <div className="card card-container login-card">

                            <h1 style={{"margin":"auto"}}>Dodaj użytkownika</h1>

                            <Form
                                onSubmit={handleAddUser}
                                ref={c => {
                                    setForm(c);
                                }}
                            >
                                <div>

                                    <div className="form-group login-form-group">
                                        <Input
                                            type="text"
                                            className="form-control"
                                            name="email"
                                            value={email}
                                            onChange={onChangeEmail}
                                            validations={[required, email_valid]}
                                            placeholder="Email"
                                        />
                                    </div>

                                    <div className="form-group login-form-group">
                                        <Input
                                            type="text"
                                            className="form-control"
                                            name="firstname"
                                            value={firstName}
                                            onChange={onChangeFirstName}
                                            validations={[required]}
                                            placeholder="Imię"
                                        />
                                    </div>

                                    <div className="form-group login-form-group">
                                        <Input
                                            type="text"
                                            className="form-control"
                                            name="lastname"
                                            value={lastName}
                                            onChange={onChangeLastName}
                                            validations={[required]}
                                            placeholder="Nazwisko"
                                        />
                                    </div>

                                    <div className="form-group login-form-group">
                                        <Input
                                            type="password"
                                            className="form-control"
                                            name="password"
                                            value={password}
                                            onChange={onChangePassword}
                                            validations={[required]}
                                            placeholder="Hasło"
                                        />
                                    </div>

                                    <div className="form-group login-form-group" style={{"display":"flex", "justifyContent":"center", "gap":"2em"}}>
                                        <label htmlFor="Role" style={{"fontWeight":"600"}}>Nadaj rolę</label>
                                        <select id="roleSelect" defaultValue="CUSTOMER" onChange={onChangeRole}>
                                            <option value="CUSTOMER">Klient</option>
                                            <option value="EMPLOYEE">Pracownik</option>
                                            <option value="MANAGER">Manager</option>
                                            <option value="ADMINISTRATOR">Admin</option>
                                        </select>
                                    </div>

                                    <div className="form-group login-form-group">
                                        <button className="btn btn-light btn-block login-btn">Dodaj użytkownika</button>
                                    </div>
                                </div>

                                <CheckButton
                                    style={{ display: "none" }}
                                    ref={c => {
                                        setCheckBtn(c);
                                    }}
                                />
                            </Form>
                        </div>
                    </div>
                    <div className="users-group" style={{"marginTop":"1em"}}>
                        <h1 style={{"margin":"auto"}}>Użytkownicy systemu</h1>
                        {content.map((user) => (
                            <h4 key={user.id} style={{"margin":"auto"}}>{"ID użytkownika: " + user.id + " | Email: " + user.email + " | Rola: " + user.role}</h4>
                        ))}

                    </div>

                    <Form
                        onSubmit={handleRemoveUser}
                    >
                        <div>
                            <div className="form-group login-form-group">
                                <Input
                                    type="text"
                                    className="form-control"
                                    name="id"
                                    value={id}
                                    onChange={onChangeId}
                                    placeholder="ID użytkownika"
                                />
                            </div>

                            <div className="form-group login-form-group">
                                <button className="btn btn-light btn-block login-btn">Usuń użytkownika</button>
                            </div>
                        </div>

                        <CheckButton
                            style={{ display: "none" }}
                        />
                    </Form>
                </div>
            )
        )
    )

}