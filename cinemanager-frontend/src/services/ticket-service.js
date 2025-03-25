import axios from "axios";
import { getCurrentUser } from "./authentication-service";
import { API_URL_BASE } from "./api-url-config";

const API_URL = API_URL_BASE + 'tickets'

export async function fetch_tickets(page, size, past) {
    return axios.get(API_URL + "?past=" + past + "&page=" + page + "&size=" + size + "&sort=screening.startDate,desc", {
            headers: {
                Authorization: "Bearer " + getCurrentUser()
            }
        }).then(res => {
            return res.data;
        }).catch(err => console.warn(err))
}

export async function fetch_ticket(id) {
    return axios.get(API_URL + `/${id}`, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    }).then(res => {
        return res.data;
    }).catch(err => console.warn(err))
}

export async function validate_ticket(id) {
    return axios.post(API_URL + `/${id}/validate`, {}, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    })
}