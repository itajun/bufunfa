import React, { useEffect, useState } from 'react'
import EditTransaction from '../dialogs/EditTransaction'
import EditAccount from '../dialogs/EditAccount'
import EditCategory from '../dialogs/EditCategory'
import { subscribeTo, unsubscribeToken, EVENT_DIALOG } from '../../util/events'

const ALL_CLEAR = {
    type: null,
    payload: null,
    callback: null,
    action: null
}

export default () => {
    const [currentDialog, setCurrentDialog] = useState(ALL_CLEAR)

    useEffect(() => {
        const token = subscribeTo(EVENT_DIALOG, (_, payload) => {
            if (payload.action === 'open') {
                setCurrentDialog(payload)
            } else if (payload.action === 'close') {
                setCurrentDialog(ALL_CLEAR)
            } else {
                console.error('Unknown action: ' + payload.action)
            }
        })

        return () => {
            unsubscribeToken(token)
        }
    })

    return (<>
        {currentDialog.type === 'editTransaction' && <EditTransaction callback={currentDialog.callback} payload={currentDialog.payload} />}
        {currentDialog.type === 'editAccount' && <EditAccount callback={currentDialog.callback} payload={currentDialog.payload} />}
        {currentDialog.type === 'editCategory' && <EditCategory callback={currentDialog.callback} payload={currentDialog.payload} />}
    </>)
}