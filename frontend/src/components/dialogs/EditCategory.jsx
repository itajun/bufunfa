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
import { CREATE_CATEGORY } from "../../graphql/mutations/basics";
import { useMutation } from "@apollo/react-hooks";
import { showAlert, extractMessage } from "../../util/alert";

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
};

export default ({ payload, callback }) => {
  const classes = useStyles();

  const [createCategoryMutation] = useMutation(CREATE_CATEGORY);

  const category = payload && payload.category ? payload.category : CLEAR_DATA;
  const title = category && category.id ? "Edit category" : "Create category";
  const { register, handleSubmit, errors } = useForm({
    mode: "onBlur",
    defaultValues: category,
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
        entity = await createCategoryMutation({
          variables: { input },
        });
      }
      callback && callback({ created: true, entity });
      showAlert(`Category ${input.id ? "updated" : "created"}`);
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
