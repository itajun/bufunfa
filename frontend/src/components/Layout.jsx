import React, { Suspense } from 'react'
import CssBaseline from '@material-ui/core/CssBaseline'
import { ThemeProvider, Container } from '@material-ui/core';
import theme from '../util/layout/theme';
import AppBar from './AppBar'
import Drawer from './Drawer'
import Body from './Body';

export default () =>
    <ThemeProvider theme={theme}>
        <CssBaseline />
        <Suspense fallback='Loading...'>
            <AppBar />
            <Drawer />
            <Container fixed>
                 <Body />
            </Container>
        </Suspense>
    </ThemeProvider>
