import { AddCinemaRoom, AddScreeningType, AddMovie, AddDirector, AddGenre, AddScreening } from "./cinema-management-add-component";

export default function CinemaManagement(){
    return (
        <div className="container">
            <AddScreening />
            <AddMovie />
            <AddGenre />
            <AddDirector />
            <AddScreeningType />
            <AddCinemaRoom />
        </div>
    )
}