import { sendEvent, EVENT_DIALOG } from "./events"

export const EDIT_TRANSACTION = 'editTransaction'
export const EDIT_ACCOUNT = 'editAccount'
export const EDIT_CATEGORY = 'editCategory'

export const showEditTransaction = (payload, callback) => {
    sendEvent(EVENT_DIALOG, { action: 'open', type: EDIT_TRANSACTION, payload, callback })
}

export const showDialog = (type, callback) => {
    sendEvent(EVENT_DIALOG, { action: 'open', type: type, callback })
}

export const closeAnyOpen = () => {
    sendEvent(EVENT_DIALOG, { action: 'close' })
}