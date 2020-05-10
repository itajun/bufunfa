import React, { Suspense } from 'react'
import CssBaseline from '@material-ui/core/CssBaseline'
import { ThemeProvider, Container } from '@material-ui/core';
import theme from '../util/layout/theme';
import AppBar from './layout/AppBar'
import Drawer from './layout/Drawer'
import Body from './layout/Body';
import Dialogs from './layout/Dialogs';

export default () => {
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Suspense fallback='Loading...'>
                <AppBar />
                <Drawer />
                <Container fixed>
                    <Body />
                    <Dialogs />
                </Container>
            </Suspense>
        </ThemeProvider>
    )
}
