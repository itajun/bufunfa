import React, { Suspense } from "react";
import CssBaseline from "@material-ui/core/CssBaseline";
import { ThemeProvider, Container } from "@material-ui/core";
import theme from "../../util/layout/theme";
import AppBar from "./AppBar";
import Drawer from "./Drawer";
import Body from "./Body";
import Dialogs from "./Dialogs";
import Alert from "../dialogs/Alert";
import DateFnsUtils from "@date-io/date-fns";
import { MuiPickersUtilsProvider } from "@material-ui/pickers";

export default () => {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Suspense fallback="Loading...">
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
          <AppBar />
          <Drawer />
          <Container maxWidth="xl">
            <Body />
            <Dialogs />
            <Alert />
          </Container>
        </MuiPickersUtilsProvider>
      </Suspense>
    </ThemeProvider>
  );
};
