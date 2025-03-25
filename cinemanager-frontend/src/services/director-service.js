import axios from "axios";
import { API_URL_BASE } from "./api-url-config";
import { getCurrentUser } from "./authentication-service";

const API_URL = API_URL_BASE + 'directors'

export async function fetch_directors() {
    return axios.get(API_URL, {}, {})
    .then(res => {return res.data})
    .catch(err => {console.warn(err)})
}

export async function create_director(firstName, lastName) {
    return axios.post(API_URL, {
        firstName: firstName,
        lastName: lastName
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