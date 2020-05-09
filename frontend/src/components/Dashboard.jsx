import React from 'react'
import { Typography } from '@material-ui/core'
import TransactionsTable from './TransactionsTable'
import { useQuery, useMutation } from '@apollo/react-hooks';
import { GET_TRANSACTIONS } from '../graphql/queries/transactions';
import CircularProgress from '@material-ui/core/CircularProgress';
import { DELETE_TRANSACTION } from '../graphql/mutations/transactions';

export default () => {
    const { data, loading, error, refetch } = useQuery(GET_TRANSACTIONS)
    const [ deleteTransactionMutation ] = useMutation(DELETE_TRANSACTION)

    const deleteTransaction = async transactionId => {
        await deleteTransactionMutation({ variables: { transactionId } })
        await refetch()
    }

    return (<>
        {(loading || error) && <CircularProgress />}
        {!(loading || error) && <>
            <Typography variant="h4">Dashboard</Typography>
            <TransactionsTable transactions={data.transactions} onDelete={deleteTransaction} onConfirm={() => {}} />
        </>}
    </>)
}