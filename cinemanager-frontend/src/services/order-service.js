import axios from "axios";
import { API_URL_BASE, PAGE_URL_BASE } from "./api-url-config";
import { getCurrentUser } from "./authentication-service";

const API_URL = API_URL_BASE

export async function fetch_screening_info(screeningId){
    return axios.get(API_URL + "screenings/" + screeningId, {}, {}).catch(err => console.warn(err))
}

export async function fetch_screening_seats(screeningId){
    return axios.get(API_URL + "screenings/" + screeningId + "/seats" , {}, {}).catch(err => console.warn(err))
}

export async function fetch_order(orderId) {
    return axios.get(API_URL + "orders/" + orderId, {
        headers: { Authorization: "Bearer " + getCurrentUser() }
    }).then(res => {     
        return res.data;
    }).catch(err => console.warn(err))       
}

export async function order_payment(orderId) {
    return axios.post(API_URL + "token", {}, {
            headers: {
                Authorization: "Basic " + btoa("jan@mail.com:password")
        
            }
        }).then(res => {
            return res.data
        }).then(res => {
            return axios.post(API_URL + "orders/" + orderId + "/payment", {}, {
                headers: { Authorization: "Bearer " + res }
            }).then(res => { 
                setTimeout(() => window.location.reload(), 1000) 
            }).catch(err => console.warn(err))
        }).catch(err => console.warn(err))
}

export async function fetch_orders(page, size, paid, cancelled) {
    const params = {
        page: page,
        size: size,
        cancelled: cancelled,
        ...(paid != null && { paid: paid })
    }

    const queryString = new URLSearchParams(params).toString()

    return axios.get(`${API_URL}orders?${queryString}`, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    }).then(res => {
        return res.data;
    }).catch(err => console.warn(err))
}

export function place_order(tickets, screeningId){
    var user = getCurrentUser();
    if(user == null){
        window.alert("Przed dokonaniem zakupu musisz się zalogować")
        return;
    }
    const ticketsDto = []
    for(var ticket of tickets){
        ticketsDto.push(
            {
                "row": ticket.row,
                "seatNumber": ticket.position,
                "ticketType": ticket.type=="Ulgowy"?"DISCOUNTED":"REGULAR"
            }
        )
    }
    return axios.post(API_URL + "orders", {
        "screeningId": screeningId,
        "tickets": ticketsDto
    }, {
        headers: {
            Authorization: "Bearer " + user
        }
    })
    .then(res => { setTimeout(() => window.location.replace(PAGE_URL_BASE + `orders/${res.data.id}`), 1000) })
    .catch(err => console.warn(err))
}