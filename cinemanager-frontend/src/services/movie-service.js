import axios from "axios";
import { API_URL_BASE } from "./api-url-config";
import { getCurrentUser } from "./authentication-service";

const API_URL = API_URL_BASE + 'movies'

export async function fetch_movies(page, size, genre, title, minRating){
    const params = {
        page: page,
        size: size,
        genre: genre,
        title: title,
        minRating: minRating,
    }

    const queryString = new URLSearchParams(params).toString()

    return axios.get(`${API_URL}?${queryString}`, {}, {})
    .then(res => {
        return res.data;
    })
    .catch(err => {console.warn(err)})
}

export async function fetch_popular_movies(){
    return axios.get(API_URL + `/popular`, {}, {}).then((res) => {
        return res.data.movies
    })
}

export async function fetch_movie(id) {
    return axios.get(API_URL + `/${id}`, {
    }).then(res => {
        return res.data;
    }).catch(err => console.warn(err))
}

export async function fetch_rating(id) {
    return axios.get(API_URL + `/${id}/rating`)
        .then(res => {
            return res.data
        }).catch(err => console.warn(err))
}

export async function fetch_reviews(id, page, size) {
    const params = {
        movieId: id,
        page: page,
        size: size
    }

    const queryString = new URLSearchParams(params).toString()

    return axios.get(`${API_URL_BASE}reviews?${queryString}`, {}, {})
    .then(res => {
        return res.data;
    })
    .catch(err => {console.warn(err)})
}

export async function post_review(id, content, rating) {
    return axios.post(`${API_URL_BASE}reviews`, {
        content: content,
        rating: rating,
        movieId: id,
    }, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    })
}

export async function delete_review(id) {
    return axios.delete(`${API_URL_BASE}reviews/${id}`, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    })
}

export async function update_review(reviewId, movieId, content, rating) {
    return axios.put(`${API_URL_BASE}reviews/${reviewId}`, {
        content: content,
        rating: rating,
        movieId: movieId,
    }, {
        headers: {
            Authorization: "Bearer " + getCurrentUser()
        }
    })
}

export async function create_movie(title, description, length, genreId, directorId, poster) {
    const formData = new FormData();

    formData.append(
        "movie",
        JSON.stringify({
            title: title,
            description: description,
            length: length,
            genreId: genreId,
            directorId: directorId,
            posterUrl: ""
        })
    );

    formData.append("poster", poster);

    return axios.post(API_URL, formData , {
        headers: {
            Authorization: "Bearer " + getCurrentUser(),
        }
    })
    .then(res => {
        return res
    })
    .catch(err => console.warn(err))
}