import React, { useState, useMemo } from 'react'
import { Table, Paper, TableContainer, TableHead, TableCell, TableRow, TableBody, makeStyles, IconButton } from '@material-ui/core'
import DeleteOutlineIcon from '@material-ui/icons/DeleteOutline'
import PlaylistAddCheckIcon from '@material-ui/icons/PlaylistAddCheck'
import CircularProgress from '@material-ui/core/CircularProgress'
import { centToCurr, localDate } from '../../util/formatters'
import _ from "lodash"
import clsx from 'clsx';

const useStyles = makeStyles((theme) => ({
    tr: {
        '&:hover $actionBar': {
            left: 'auto'
        },
    },
    shaded: {
        backgroundColor: theme.palette.grey[200]
    },
    td: {
        width: '60px',
        verticalAlign: 'top',
    },
    actionBar: {
        position: 'absolute',
        left: '-9999px',
        display: 'inline-block',
        zIndex: 100,
        marginLeft: '-20px',
        marginTop: '-2px'
    },
    actionButton: {
        padding: '2px',
        marginLeft: '2px'
    },
}));

const ActionBar = ({ rowId, onDelete, onConfirm }) => {
    const classes = useStyles()
    const [waiting, setWaiting] = useState(false)

    const handleDelete = transactionId => async () => {
        setWaiting(true)
        try {
            await onDelete(transactionId)
        } catch (e) {
            setWaiting(false) // Only in case of error, otherwise it's unmounted
        }
    }

    const handleConfirm = transactionId => async () => {
        setWaiting(true)
        try {
            await onConfirm(transactionId)
        } finally {
            setWaiting(false)
        }
    }

    return (
        <>
            {waiting && <CircularProgress size={12} color='secondary' />}
            {!waiting &&
                <div className={classes.actionBar}>
                    {onConfirm && <IconButton className={classes.actionButton} onClick={handleConfirm(rowId)}><PlaylistAddCheckIcon fontSize="small" /></IconButton>}
                    {onDelete && <IconButton className={classes.actionButton} onClick={handleDelete(rowId)}><DeleteOutlineIcon fontSize="small" /></IconButton>}
                </div>
            }
        </>
    )
}

export default ({ transactions, initialBalance, removeCols = [], onDelete, onConfirm }) => {
    const classes = useStyles()

    const sortedTransactions = useMemo(() => {
        const result = _.sortBy(transactions, ['date, amount']).reverse()
        let currentBalance = initialBalance
        _.forEachRight(result, e => e.balance = (currentBalance += e.amount))
        return result
    }, [transactions, initialBalance])

    return (
        <>
            <TableContainer component={Paper} >
                <Table size="small" aria-label="a dense table">
                    <TableHead>
                        <TableRow className={clsx(classes.tr, classes.shaded)}>
                            {!removeCols.includes('description') && <TableCell>Description</TableCell>}
                            {!removeCols.includes('account') && <TableCell>Account</TableCell>}
                            {!removeCols.includes('category') && <TableCell>Category</TableCell>}
                            {!removeCols.includes('amount') && <TableCell align="right">Amount</TableCell>}
                            {!removeCols.includes('balance') && <TableCell align="right">Balance</TableCell>}
                            {!removeCols.includes('date') && <TableCell align="right">Date</TableCell>}
                            <TableCell />
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {sortedTransactions.map((row) => (
                            <TableRow hover key={row.id} className={classes.tr}>
                                {!removeCols.includes('description') && <TableCell component="th" scope="row">{row.description}</TableCell>}
                                {!removeCols.includes('account') && <TableCell>{row.account.name}</TableCell>}
                                {!removeCols.includes('category') && <TableCell>{row.category.name}</TableCell>}
                                {!removeCols.includes('amount') && <TableCell align="right">{centToCurr(row.amount)}</TableCell>}
                                {!removeCols.includes('balance') && <TableCell align="right">{centToCurr(row.balance)}</TableCell>}
                                {!removeCols.includes('date') && <TableCell align="right">{localDate(row.date)}</TableCell>}
                                <TableCell className={classes.td}><ActionBar rowId={row.id} onDelete={onDelete} onConfirm={onConfirm} /></TableCell>
                            </TableRow>
                        ))}
                        <TableRow className={clsx(classes.tr, classes.shaded)}>
                            <TableCell>Previous balance</TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
                            <TableCell align="right">{centToCurr(initialBalance)}</TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </TableContainer>
        </>
    )
}