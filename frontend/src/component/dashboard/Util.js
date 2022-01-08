import { Base_Url } from "../../config";

const axios = require('axios');

export const prepareAdvice = (personId) => {
    return axios
        .get(Base_Url + "/advice/prepare/" + personId)
        .then(response => response.status === 200 ? response : Promise.reject)
        .then(response => {
            console.log(response);

            return response.data;
        })
        .catch(error => {
            return error.response;
        });
}

export const generateAdvice = (personId, values) => {
    return axios
        .post(Base_Url + "/advice/generate/" + personId, values)
        .then(response => response.status === 200 ? response : Promise.reject)
        .then(response => {
            console.log(response);

            return response.data;
        })
        .catch(error => {
            return error.response;
        });
}

export const getAdvice = (personId) => {
    return axios
        .get(Base_Url + "/advice/" + personId)
        .then(response => response.status === 200 ? response : Promise.reject)
        .then(response => {
            console.log(response);

            return response.data;
        })
        .catch(error => {
            return error.response;
        });
}


export const formatDate = (date) => {
	let d = new Date(date),
		month = '' + (d.getMonth() + 1),
		day = '' + d.getDate(),
		year = d.getFullYear();

	if (month.length < 2) 
		month = '0' + month;
	if (day.length < 2) 
		day = '0' + day;

	return [day, month, year].join('-');
}
