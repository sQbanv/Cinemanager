import axios from "axios";
import { API_URL_BASE } from "./api-url-config";
import { getCurrentUser } from "./authentication-service";

const API_URL = API_URL_BASE + 'screening-types'

export async function create_screening_type(name, basePrice, discountPrice) {
    return axios.post(API_URL, {
        name: name,
        basePrice: basePrice,
        discountPrice: discountPrice
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

export async function fetch_screening_types() {
    return axios.get(API_URL, {}, {})
    .then(res => {
        return res.data
    })
    .catch(err => console.warn(err))
}