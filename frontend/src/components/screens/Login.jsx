import { useApolloClient } from "@apollo/react-hooks";
import {
  Avatar,
  Button,
  Container,
  CssBaseline,
  makeStyles,
  Paper,
  TextField,
  Typography,
} from "@material-ui/core";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import React from "react";
import { useForm } from "react-hook-form";
import { ME } from "../../graphql/queries/basics";
import { showAlert } from "../../util/alert";
import useLayoutSettings from "../../store/useLayoutSettings";

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: theme.spacing(4),
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: "100%",
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

export default () => {
  const classes = useStyles();

  const { register, handleSubmit, errors, setError } = useForm({
    mode: "onBlur",
  });

  const { updateLayoutSettings } = useLayoutSettings();

  const client = useApolloClient();

  const onSubmit = async (input) => {
    try {
      localStorage.setItem("token", btoa(`${input.name}:${input.password}`));
      const {
        data: { me },
      } = await client.query({ query: ME });
      updateLayoutSettings({ user: me });
      showAlert(`Welcome ${me}`);
    } catch (e) {
      localStorage.removeItem("token");
      updateLayoutSettings({ user: false });
      console.error(e);
      showAlert("Invalid credentials", "error");
      setError("name");
      setError("password");
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <Paper className={classes.paper}>
        <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Sign in
        </Typography>
        <form
          autoComplete="off"
          onSubmit={handleSubmit(onSubmit)}
          className={classes.form}
        >
          <TextField
            autoFocus
            margin="dense"
            id="name"
            name="name"
            label="User"
            inputRef={register({ required: true })}
            error={!!errors.name}
            fullWidth
          />
          <TextField
            margin="dense"
            id="password"
            name="password"
            label="Password"
            inputRef={register({ required: true })}
            error={!!errors.password}
            type="password"
            fullWidth
          />
          <Typography>Tip: user/user</Typography>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            className={classes.submit}
          >
            Sign In
          </Button>
        </form>
      </Paper>
    </Container>
  );
};
