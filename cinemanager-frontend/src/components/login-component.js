import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import { getCurrentUser, login } from "../services/authentication-service";

import { useState } from "react";
import isEmail from "validator/lib/isEmail";

const required = value => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                To pole jest wymagane
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

export default function Login() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [form, setForm] = useState();
    const [checkBtn, setCheckBtn] = useState();

    function onChangeEmail(e) {
        setEmail(e.target.value);
    }
    function onChangePassword(e) {
        setPassword(e.target.value);
    }

    function handleLogin(e) {
        e.preventDefault();

        form.validateAll();

        if (checkBtn.context._errors.length === 0) {
            login(email, password).then(
                () => {
                    alert("Zalogowano.");
                    window.history.back();
                },
                error => {
                    alert("Logowanie nie powiodło się");
                }
            )
        }
    }

    return (
        ( !getCurrentUser() ? 
        (<div className="col-md-12">
            <div className="card card-container login-card">

                <h1 style={{"margin":"auto"}}>Zaloguj się</h1>

                <Form
                    onSubmit={handleLogin}
                    ref={c => {
                        setForm(c);
                    }}
                >
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
                            type="password"
                            className="form-control"
                            name="password"
                            value={password}
                            onChange={onChangePassword}
                            validations={[required]}
                            placeholder="Hasło"
                        />
                    </div>

                    <div className="form-group login-form-group">
                        <button
                            className="btn btn-light btn-block login-btn"
                        >
                            <span>Zaloguj</span>
                        </button>
                    </div>
                    <CheckButton
                        style={{ display: "none" }}
                        ref={c => {
                            setCheckBtn(c)
                        }}
                    />
                </Form>
            </div>
        </div>)
        : (<div>
            Jesteś już zalogowany
        </div>)
        )
    );
}