import { sendEvent, EVENT_ALERT } from "./events"

export const showAlert = ( content, severity = 'success', autoHideDuration = 6000) => {
    sendEvent(EVENT_ALERT, { open: true, content, autoHideDuration, severity })
}

export const extractMessage = exception => {
    if (exception.graphQLErrors) {
        if (exception.graphQLErrors.length > 0) {
            return exception.graphQLErrors[0].message
        }
    }
    if (exception.networkError) {
        return exception.networkError.message
    }
    console.error(exception)
    return "" + exception
}