import React from 'react'
import { DialogContent, Dialog, TextField, DialogActions, Button, DialogTitle, CircularProgress, FormControl, InputLabel, Select, MenuItem, Grid } from '@material-ui/core'
import { useForm, Controller } from "react-hook-form";
import { CREATE_TRANSACTION } from '../../graphql/mutations/transactions';
import { useMutation, useQuery } from '@apollo/react-hooks';
import { showAlert, extractMessage } from '../../util/alert';
import { floatToCents, graphqlDate } from '../../util/formatters'
import NumberFormatEx from '../extension/NumberFormatEx'
import { GET_CATEGORIES, GET_ACCOUNTS } from '../../graphql/queries/basics';
import { KeyboardDatePicker } from '@material-ui/pickers';

const CLEAR_DATA = {
    id: null,
    description: '',
    accountId: null,
    categoryId: null,
    amount: null,
    date: Date(),
}

export default ({ payload, callback }) => {
    const [createTransactionMutation] = useMutation(CREATE_TRANSACTION)
    const { data: categoriesData, loading: loadingCategories, error: errorCategories } = useQuery(GET_CATEGORIES)
    const { data: accountsData, loading: loadingAccounts, error: errorAccounts } = useQuery(GET_ACCOUNTS)

    const transaction = (payload && payload.transaction) ? payload.transaction : CLEAR_DATA
    const title = transaction && transaction.id ? 'Edit transaction' : 'Create transaction'
    const { register, handleSubmit, errors, control } = useForm({ mode: 'onBlur', defaultValues: transaction });

    const handleCancel = () => {
        callback && callback()
    }

    const onSubmit = async input => {
        try {
            let entity = input
            if (input.id) {
                // TODO
            } else {
                entity = await createTransactionMutation({
                    variables: {
                        input: {
                            ...input,
                            amount: floatToCents(input.amount),
                            date: graphqlDate(input.date)
                        }
                    }
                })
            }
            callback && callback({ created: true, entity })
            showAlert(`Transaction ${input.id ? 'updated' : 'created'}`)
        } catch (e) {
            showAlert(extractMessage(e), 'error')
        }
    }

    if (loadingAccounts || loadingCategories || errorAccounts || errorCategories) {
        return <CircularProgress />
    }

    return (
        <Dialog open onClose={handleCancel} aria-labelledby="form-dialog-title">
            <DialogTitle id="form-dialog-title">{title}</DialogTitle>
            <form autoComplete="off" onSubmit={handleSubmit(onSubmit)} >
                <DialogContent>
                    <Grid container>
                        <Grid item xs={12}>
                            <TextField
                                autoFocus
                                margin="dense"
                                id="description"
                                name="description"
                                label="Description"
                                inputRef={register({ required: true })}
                                error={errors.description}
                                fullWidth
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <FormControl>
                                <InputLabel id="accountIdLabel">Account</InputLabel>
                                <Controller
                                    as={<Select
                                        labelId="accountIdLabel"
                                        id="accountId"
                                        error={errors.accountId}
                                        style={{ minWidth: '500px' }}
                                    >
                                        {
                                            accountsData.accounts.map(e => <MenuItem value={e.id}>{e.name}</MenuItem>)
                                        }
                                    </Select>}
                                    name="accountId"
                                    rules={{ required: true }}
                                    control={control}
                                />
                            </FormControl>
                        </Grid>
                        <Grid item xs={12}>
                            <FormControl>
                                <InputLabel id="categoryIdLabel">Category</InputLabel>
                                <Controller
                                    as={<Select
                                        labelId="categoryIdLabel"
                                        id="categoryId"
                                        style={{ minWidth: '500px' }}
                                        error={errors.categoryId}
                                    >
                                        {
                                            categoriesData.categories.map(e => <MenuItem value={e.id}>{e.name}</MenuItem>)
                                        }
                                    </Select>}
                                    name="categoryId"
                                    rules={{ required: true }}
                                    control={control}
                                />
                            </FormControl>
                        </Grid>
                        <Grid item xs={12}>
                            <Controller
                                as={<KeyboardDatePicker
                                    disableToolbar
                                    variant="inline"
                                    format="dd/MM/yyyy"
                                    margin="normal"
                                    id="date-picker-inline"
                                    label="Date"
                                    error={errors.date}
                                    autoOk
                                    KeyboardButtonProps={{
                                        'aria-label': 'change date',
                                    }}
                                />}
                                name="date"
                                rules={{ required: true }}
                                control={control}
                                onChange={([selected]) => {
                                    return selected;
                                }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                margin="dense"
                                id="amount"
                                name="amount"
                                label="Amount"
                                inputRef={register({ required: true })}
                                error={errors.amount}
                                defaultValue={transaction.amount}
                                InputProps={{
                                    inputComponent: NumberFormatEx,
                                }}
                                fullWidth
                            />
                        </Grid>
                    </Grid>
                </DialogContent>
                <DialogActions>
                    <Button type='submit' color="primary">OK</Button>
                    <Button onClick={handleCancel} color="primary">Cancel</Button>
                </DialogActions>
            </form>
        </Dialog>
    )
}