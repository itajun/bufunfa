import React, { useState, useEffect } from 'react';
import Snackbar from '@material-ui/core/Snackbar';
import MuiAlert from '@material-ui/lab/Alert';
import { subscribeTo, unsubscribeToken, EVENT_ALERT } from '../../util/events'

function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const ALL_CLEAR = {
    open: false,
    content: 'No message',
    autoHideDuration: 5000,
    severity: 'success'
}

export default () => {
    const [state, setState] = useState(ALL_CLEAR);

    useEffect(() => {
        const token = subscribeTo(EVENT_ALERT, (_, payload) => {
            setState({ ...ALL_CLEAR, ...payload })
        })

        return () => {
            unsubscribeToken(token)
        }
    })

    const handleClose = (reason) => {
        if (reason === 'clickaway') {
            return;
        }

        setState(ALL_CLEAR)
    };

    const { open, content, autoHideDuration, severity } = state

    if (!open) {
        return null
    }

    return (
        <Snackbar open={open} autoHideDuration={autoHideDuration} onClose={handleClose}>
            <Alert onClose={handleClose} severity={severity}>
                {content}
            </Alert>
        </Snackbar>
    );
}