import "bootstrap/dist/css/bootstrap.min.css";
import Login from "./components/login-component";
import { logout } from "./services/authentication-service";
import { Routes, Route, Link } from "react-router-dom";
import { useState } from "react";
import { getCurrentUser } from "./services/authentication-service";
import Register from "./components/register-component";
import Manager from "./components/manager-component";
import CinemaManagement from "./components/cinema-management-component";
import Screenings from "./components/screening-component";
import "./styles/overall.css"
import Order from "./components/order-component";
import { Tickets, TicketDetails } from "./components/ticket-component";
import { OrderList, OrderDetails} from "./components/order-list-component";
import User from "./components/user-component";
import { Movies, Movie } from "./components/movie-component";
import CinemaRooms from "./components/cinema-room-component";
import Employee from "./components/employee-component";
import Statistics from "./components/statistics-component";
import Home from "./components/home-component";
import Profile from "./components/profile-component";


function App() {

  const user = getCurrentUser();
  const [currentUser, setCurrentUser] = useState( user ? JSON.parse(atob(user.split('.')[1])) : null);

  const userHasRole = role => {
    switch(role){
      case "ADMIN":
        if (currentUser.scope == "ROLE_ADMINISTRATOR"){
          return true;
        }
        break;
      case "MANAGER":
        if (currentUser.scope == "ROLE_MANAGER" || currentUser.scope == "ROLE_ADMINISTRATOR"){
          return true;
        }
        break;
      case "EMPLOYEE":
        if (currentUser.scope == "ROLE_EMPLOYEE" || currentUser.scope == "ROLE_ADMINISTRATOR" || currentUser.scope == "ROLE_MANAGER" ){
          return true;
        }
        break;
      default:
        return true;
    }
    return false;
  }


  return (
    <div>
      <nav className="navbar navbar-expand navbar-dark">
        <Link to={"/"} className="navbar-brand">
          <span className="navbar-brand-text">CineManager</span>
        </Link>
        <div className="navbar-nav mr-auto">
          <li className="nav-item">
            <Link to={"/"} className="nav-link">
              Strona główna
            </Link>
          </li>

          {currentUser && (
            <li className="nav-item">
              <Link to={"/user"} className="nav-link">
                Bilety i zamówienia
              </Link>
            </li>
          )}

          {currentUser && userHasRole("EMPLOYEE") && (
            <li className="nav-item">
              <Link to={"/employee"} className="nav-link">
                Kasowanie biletów
              </Link>
            </li>
          )}

          {currentUser && userHasRole("MANAGER") && (
            <li className="nav-item">
              <Link to={"/statistics"} className="nav-link">
                Statystyki
              </Link>
            </li>
          )}

          {currentUser && userHasRole("MANAGER") && (
            <li className="nav-item">
              <Link to={"/user-manager"} className="nav-link">
                Użytkownicy
              </Link>
            </li>
          )}

          {currentUser && userHasRole("MANAGER") && (
            <li className="nav-item">
              <Link to={"/cinema-management"} className="nav-link">
                Kino
              </Link>
            </li>
          )}
        </div>

        {currentUser ? (
          <div className="navbar-nav ml-auto">
            <li className="nav-item">
              <Link to={"/profile"} className="nav-link">
                {currentUser.sub}
              </Link>
            </li>
            <li className="nav-item">
              <a href="/login" className="nav-link" onClick={logout}>
                Wyloguj się
              </a>
            </li>
          </div>
        ) : (
          <div className="navbar-nav ml-auto">
            <li className="nav-item">
              <Link to={"/login"} className="nav-link">
                Zaloguj się
              </Link>
            </li>

            <li className="nav-item">
              <Link to={"/register"} className="nav-link">
                Zarejestruj się
              </Link>
            </li>
          </div>
        )}
      </nav>
      <nav className="navbar navbar-secondary navbar-expand navbar-dark">
        <div className="navbar-nav mr-auto">
          <li className="nav-item">
            <Link to={"/repertuar?page=0&size=5"} className="nav-link">
              Repertuar
            </Link>
          </li>
          <li className="nav-item">
            <Link to={"/movies"} className="nav-link">
              Filmy
            </Link>
          </li>
          <li className="nav-item">
            <Link to={"/cinema-rooms"} className="nav-link">
              Sale kinowe
            </Link>
          </li>
        </div>
      </nav>
      <div className="container-xl mt-3">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/user-manager" element={<Manager />} />
          <Route path="/statistics" element={<Statistics />} />
          <Route path="/employee" element={<Employee />} />
          <Route path="/cinema-management" element={<CinemaManagement />} />
          <Route path="/repertuar" element={<Screenings />} />
          <Route path="/order" element={<Order />} />
          <Route path="/user" element={<User />} />
          <Route path="/ticket" element={<Tickets />} />
          <Route path="/ticket/:id" element={<TicketDetails />} />
          <Route path="/orders/:id" element={<OrderDetails />} />
          <Route path="/orders" element={<OrderList />} />
          <Route path="/movies" element={<Movies />} />
          <Route path="/movies/:id" element={<Movie />} />
          <Route path="/cinema-rooms" element={<CinemaRooms />} />
          <Route path="/profile" element={<Profile />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;
