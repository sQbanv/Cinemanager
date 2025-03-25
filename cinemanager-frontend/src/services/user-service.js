import axios from "axios";
import { getCurrentUser } from "./authentication-service";
import { API_URL_BASE } from "./api-url-config";

const API_URL = API_URL_BASE + "users";

export function delete_user(id){
    return axios.delete(API_URL + "/" + id, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    }).then(res => {
        return res.data;
    }).catch(err => console.log(err))
}

export function update_user_info(id, details){
    return axios.put(API_URL + "/" + id, {
        firstName: details.firstName,
        lastName: details.lastName
    }, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        } 
    }).then(() =>{
        window.alert("Zmiana udana. Aby wyświetlić nowe dane odśwież okno.")
    })
}

export async function fetch_user_info(id){
    return axios.get(API_URL + "/" + id, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    }).then(res => {
        return res.data;
    }).catch(err => console.log(err))
}

export async function fetch_users(){
    return axios.get(API_URL , {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    }).then(res => {
        return res.data;
    }).catch(err => console.log(err))
}