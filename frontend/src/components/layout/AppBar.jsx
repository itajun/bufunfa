import React from 'react'
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import useLayoutSettings from '../../store/useLayoutSettings';
import CreateButton from './CreateButton';
import { Box, makeStyles } from '@material-ui/core';

const useStyles = makeStyles((theme) => ({
    bar: {
        padding: '0 8px'
    },
    actions: {
        display: 'flex',
        justifyContent: 'flex-end',
        flex: 1,
    }
}))

export default () => {
    const { updateLayoutSettings } = useLayoutSettings()
    const classes = useStyles()

    const handleDrawerOpen = () => {
        updateLayoutSettings({ drawerOpen: true })
    };

    return (<AppBar position="static" className={classes.bar}>
        <Toolbar>
            <IconButton edge="start" aria-label="menu" onClick={handleDrawerOpen}>
                <MenuIcon />
            </IconButton>
            <Typography variant="h6">Bufunfa</Typography>
            <Box className={classes.actions}>
                <CreateButton />
            </Box>
        </Toolbar>
    </AppBar>)
}