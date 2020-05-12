import PubSub from 'pubsub-js'

export const EVENT_DIALOG = 'dialog'
export const EVENT_ENTITY = 'entity'
export const EVENT_ENTITY_CHANGED = EVENT_ENTITY + '.changed'
export const EVENT_ALERT = 'alert'

export const sendEvent = (event, payload) => {
    console.debug({EVENT: event, payload})
    PubSub.publish(event, payload)
}

export const subscribeTo = (event, callback) => {
    return PubSub.subscribe(event, callback)
}

export const unsubscribeToken = token => {
    PubSub.unsubscribe(token)
}