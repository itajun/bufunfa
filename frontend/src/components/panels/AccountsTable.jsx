import React, { useMemo } from 'react'
import { Table, Paper, TableContainer, TableHead, TableCell, TableRow, TableBody, makeStyles } from '@material-ui/core'
import { centToCurr } from '../../util/formatters'
import clsx from 'clsx';

const useStyles = makeStyles((theme) => ({
    tr: {
    },
    shaded: {
        backgroundColor: theme.palette.grey[200]
    },
}));

export default ({ statement: { transactions, initialAccountTotals }, initialBalance, removeCols = [], onDelete, onConfirm }) => {
    const classes = useStyles()

    const accountTotals = useMemo(() => {
        const result = initialAccountTotals.reduce((p, c) => {
            p[c.accountId] = {
                id: c.accountId,
                name: c.accountName,
                start: c.amount,
                end: c.amount
            };
            return p
        }, {})

        transactions.forEach(e => result[e.account.id]['end'] += e.amount)
        return result;
    }, [transactions, initialAccountTotals]);

    return (
        <>
            <TableContainer component={Paper} >
                <Table size="small" aria-label="a dense table">
                    <TableHead>
                        <TableRow className={clsx(classes.tr, classes.shaded)}>
                            {!removeCols.includes('name') && <TableCell>Account</TableCell>}
                            {!removeCols.includes('start') && <TableCell align='right'>Start</TableCell>}
                            {!removeCols.includes('end') && <TableCell align='right'>End</TableCell>}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {Object.values(accountTotals).map((row) => (
                            <TableRow hover key={row.id} className={classes.tr}>
                                {!removeCols.includes('name') && <TableCell component="th" scope="row">{row.name}</TableCell>}
                                {!removeCols.includes('start') && <TableCell align="right">{centToCurr(row.start)}</TableCell>}
                                {!removeCols.includes('end') && <TableCell align="right">{centToCurr(row.end)}</TableCell>}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </>
    )
}