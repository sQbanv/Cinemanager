import axios from "axios";
import { API_URL_BASE } from "./api-url-config";
import { getCurrentUser } from "./authentication-service";

const API_URL = API_URL_BASE + 'genres'

export async function fetch_genries() {
    return axios.get(API_URL, {}, {})
    .then(res => {
        return res.data;
    })
    .catch(err => {console.warn(err)})
}

export async function create_genre(name) {
    return axios.post(API_URL, {
        name: name,
    }, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    })
    .then(res => {
        return res
    })
    .catch(err => console.warn(err))
}