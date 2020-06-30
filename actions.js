import * as ACTIONS_TYPES from '../actions/actions_types.js';
import * as API from '../constants/params.js';
import axios from "axios";

//SEARCH TRANSFER
export function searchTransfer(transaction, offset, limit) {
    return (dispatch) => {
        axios.post(API.API_SEARCH_TRANSFER, transaction, {params: {offset: offset, limit: limit}})
            .then((response) => {
                dispatch(parseSearchResult(response.data, transaction));
            })
    }
}

export function parseSearchResult(transfers, transaction) {
    return {
        type: ACTIONS_TYPES.SEARCH_TRANSFER,
        transfers: transfers.list_transactionBackend,
        transfersCount: transfers.transfersCount,
        transaction: transaction
    }
}


//SAVE SEARCH TRANSFER
export function saveSearchTransfer(transaction) {
    return (dispatch) => {
        axios.post(API.API_SAVE_SEARCH_TRANSFER, transaction)
            .then((response) => {
                dispatch(parseSaveSearchResult(response.data, transaction));
            })
    }
}

export function parseSaveSearchResult(transfers, transaction) {
    return {
        type: ACTIONS_TYPES.SAVE_SEARCH_TRANSFER,
        transfers: transfers.list_transactionBackend,
        transfersCount: transfers.transfersCount,
        transaction: transaction
    }
}

//GET ALL TRANSFER
export function fetchAllTransfers(offset, limit) {
    return (dispatch) => {
        return axios.get(API.API_GET_ALL_TRANSFER, {params: {offset: offset, limit: limit}})
            .then((response) => {
                dispatch(getAllTransfers(response.data));
            })
    }
}

export function getAllTransfers(transfers) {
    return {
        type: ACTIONS_TYPES.GET_ALL_TRANSFER,
        transfers: transfers.list_transactionBackend,
        transfersCount: transfers.transfersCount
    }
}

//GET BAD TRANSFER
export function fetchBadTransfers(offset, limit) {
    return (dispatch) => {
        return axios.get(API.API_GET_BAD_TRANSFER, {params: {offset: offset, limit: limit}})
            .then((response) => {
                dispatch(getBadTransfers(response.data));
            })
    }
}

export function getBadTransfers(transfers) {
    return {
        type: ACTIONS_TYPES.GET_BAD_TRANSFER,
        badtransfers: transfers.list_transactionBackend,
        badtransfersCount: transfers.transfersCount
    }
}

//GET DETAIL OF TRANSFER
export function fetchTransfer(transferid) {
    return (dispatch) => {
        return axios.get(API.API_GET_TRANSFER, {params: {transferid: transferid}})
            .then((response) => {
                dispatch(getTransfer(response.data));
            })
    }
}

export function getTransfer(transfer) {
    return {
        type: ACTIONS_TYPES.DETAIL_OF_TRANSFER,
        transfer: transfer
    }
}


//GET ALL RULES
export function fetchAllRules() {
    return (dispatch) => {
        return axios.get(API.API_GET_ALL_RULES)
            .then((response) => {
                dispatch(getAllRules(response.data));
            })
    }
}

export function getAllRules(transfers) {
    return {
        type: ACTIONS_TYPES.GET_ALL_RULES,
        rules: transfers.list_rules,
        rulesCount: transfers.rulesCount,
        isloading: false
    }
}

//GET ALL FEATURES
export function fetchAllFeatures() {
    return (dispatch) => {
        return axios.get(API.API_GET_ALL_FEATURES)
            .then((response) => {
                dispatch(getAllFeatures(response.data));
            })
    }
}

export function getAllFeatures(features) {
    return {
        type: ACTIONS_TYPES.GET_ALL_FEATURES,
        features: features.listFeatures,
        featuresCount: features.featuresCount,
        isloading: false
    }
}

//ENABLE ALL FEATURES
export function enableAllFeatures() {
    return (dispatch) => {
        return axios.get(API.API_ENABLE_ALL_FEATURES)
            .then((response) => {
                dispatch(enableAllFeaturesCAllBack(response.data));
            })
    }
}

export function enableAllFeaturesCAllBack(responseBackend) {
    return {
        type: ACTIONS_TYPES.ENABLE_ALL_FEATURES,
        message: responseBackend.message,
    }
}

//SCORING MODEL

export function fetchAllScoringModels() {
    return (dispatch) => {
        return axios.get(API.API_GET_ALL_SCORING)
            .then((response) => {
                dispatch(getAllScoring(response.data));
            })
    }
}

export function getAllScoring(scorings) {
    return {
        type: ACTIONS_TYPES.GET_ALL_SCORING,
        scorings: scorings.list_scoring,
        scoringsCount: scorings.scoringcount,
        isloading: false
    }
}


//GET ALL STATISTICS
export function fetchStatistics() {
    return (dispatch) => {
        return axios.get(API.API_GET_ALL_STATISTICS)
            .then((response) => {
                dispatch(getAllStatistics(response.data));
            })
    }
}

export function getAllStatistics(stats) {
    return {
        type: ACTIONS_TYPES.GET_ALL_STATISTICS,
        chartDatab: stats,
        isloading: false
    }
}



//GET ALL SEARCH PROFILES
export function fetchSearchProfiles() {
    return (dispatch) => {
        return axios.get(API.API_GET_ALL_SEARCH_PROFILES)
            .then((response) => {                
                dispatch(getSearchProfiles(response.data));
            })
    }
}

export function getSearchProfiles(listsearchprofiles) {
    return {
        type: ACTIONS_TYPES.GET_ALL_SEARCH_PROFILES,
        listsearchprofiles: listsearchprofiles
        
    }
}

//LOAD TRANSACTIONS FROM SEARCH PROFILE
export function loadTransactionsOfSearchProfiles(idprofile, offset, limit) {
    return (dispatch) => {
        axios.get(API.API_GET_ALL_TRANSACTIONS_OF_SEARCH_PROFILES, {params: {idprofile: idprofile, offset: offset, limit: limit}})
            .then((response) => {
                dispatch(parseTransactionsOfSearchProfiles(response.data, idprofile));
            })
    }
}

export function parseTransactionsOfSearchProfiles(transfers, idprofile) {
    return {
        type: ACTIONS_TYPES.GET_ALL_TRANSACTIONS_OF_SEARCH_PROFILES,
        transfersOfSearchProfile: transfers.list_transactionBackend,
        transfersCountOfSearchProfile: transfers.transfersCount,
        idprofile: idprofile
    }
}

