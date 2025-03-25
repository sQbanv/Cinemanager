import axios from "axios";
import { API_URL_BASE } from "./api-url-config";
import { getCurrentUser } from "./authentication-service";

export async function fetchHighestRatedMovies() {
    return axios.get(API_URL_BASE + `movies/highest-rated`, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    }).then(res => {
        return res.data.moviesWithRatings;
    }).catch(err => console.warn(err))
}


export async function fetchHighestAttendanceScreenings() {
    return axios.get(API_URL_BASE + `screenings/highest-attendance`, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    }).then(res => {
        return res.data.attendanceData;
    }).catch(err => console.warn(err))
}

export async function fetchTicketsSold(after, before) {
    const params = {};
    if (after !== null) {
        params.after = after.toISOString().slice(0, 16);
    }
    if (before !== null) {
        params.before = before.toISOString().slice(0, 16);
    }

    const queryString = new URLSearchParams(params).toString()
    return axios.get(API_URL_BASE + `movies/tickets-sold?` + queryString, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    }).then(res => {
        return res.data;
    }).catch(err => console.warn(err))
}

export async function fetchMostChosenSeats(cinemaRoomId) {
    return axios.get(API_URL_BASE + `cinema-rooms/${cinemaRoomId}/most-chosen-seats`, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    }).then(res => {
        return res.data;
    }).catch(err => console.warn(err))
}