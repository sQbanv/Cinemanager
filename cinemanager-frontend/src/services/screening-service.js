import axios from "axios";
import { API_STATIC_URL, API_URL_BASE } from "./api-url-config";
import { getCurrentUser } from "./authentication-service";

const API_URL = API_URL_BASE + 'screenings'

export async function fetch_screenings(page, size){
    const now = new Date().toISOString().slice(0, -1);

    return axios.get(API_URL + "?page=" + page + "&size=" + size + "&after=" + now, {}, {}).catch(err => {console.warn(err)})
}

export async function fetch_screenings_by_movie_id(movieId, page, size) {
    const now = new Date().toISOString().slice(0, -1);
    return axios.get(API_URL + "?page=" + page + "&size=" + size + "&movieId=" + movieId + "&after=" + now, {}, {})
    .then(res => { return res.data})
    .catch(err => {console.warn(err)})
}

export async function fetch_movie_poster(posterUrl){
    return axios.get(API_STATIC_URL + posterUrl,{},{}).catch(err => {console.warn(err)})
}

export async function create_screening(movieId, cinemaRoomId, startDate, screeningTypeId) {
    return axios.post(API_URL, {
        movieId: movieId,
        cinemaRoomId: cinemaRoomId,
        startDate: startDate,
        screeningTypeId: screeningTypeId
    }, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    })
    .then(res => {
        return res;
    })
    .catch(err => console.warn(err))
}