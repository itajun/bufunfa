import React from "react";
import {
  DialogContent,
  Dialog,
  TextField,
  DialogActions,
  Button,
  DialogTitle,
  makeStyles,
} from "@material-ui/core";
import { useForm } from "react-hook-form";
import { CREATE_ACCOUNT } from "../../graphql/mutations/basics";
import { useMutation } from "@apollo/react-hooks";
import { showAlert, extractMessage } from "../../util/alert";
import { currToCents } from "../../util/formatters";
import NumberFormatEx from "../extension/NumberFormatEx";
import { GET_ACCOUNTS } from "../../graphql/queries/basics";

const useStyles = makeStyles((theme) => ({
  textField: {
    marginLeft: theme.spacing(1),
    marginRight: theme.spacing(1),
    width: 200,
  },
  dense: {
    marginTop: 19,
  },
}));

const CLEAR_DATA = {
  id: null,
  name: "",
  initialAmount: "0.00",
};

export default ({ payload, callback }) => {
  const classes = useStyles();

  const [createAccountMutation] = useMutation(CREATE_ACCOUNT);

  const account = payload && payload.account ? payload.account : CLEAR_DATA;
  const title = account && account.id ? "Edit account" : "Create account";
  const { register, handleSubmit, errors } = useForm({
    mode: "onBlur",
    defaultValues: account,
  });

  const handleCancel = () => {
    callback && callback();
  };

  const onSubmit = async (input) => {
    try {
      let entity = input;
      if (input.id) {
        // TODO
      } else {
        entity = await createAccountMutation({
          variables: {
            input: {
              ...input,
              initialAmount: currToCents(input.initialAmount),
            },
            refetchQueries: { query: GET_ACCOUNTS },
          },
        });
      }
      callback && callback({ created: true, entity });
      showAlert(`Account ${input.id ? "updated" : "created"}`);
    } catch (e) {
      showAlert(extractMessage(e), "error");
    }
  };

  return (
    <Dialog open onClose={handleCancel} aria-labelledby="form-dialog-title">
      <DialogTitle id="form-dialog-title">{title}</DialogTitle>
      <form autoComplete="off" onSubmit={handleSubmit(onSubmit)}>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            id="name"
            name="name"
            label="Name"
            className={classes.textField}
            inputRef={register({ required: true })}
            error={!!errors.name}
          />
          <TextField
            margin="dense"
            id="initialAmount"
            name="initialAmount"
            label="Initial Amount"
            className={classes.textField}
            inputRef={register({ required: true })}
            error={!!errors.initialAmount}
            defaultValue={account.initialAmount}
            InputProps={{
              inputComponent: NumberFormatEx,
            }}
          />
        </DialogContent>
        <DialogActions>
          <Button type="submit" color="primary">
            OK
          </Button>
          <Button onClick={handleCancel} color="primary">
            Cancel
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
};
