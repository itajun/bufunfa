import React from 'react'
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import useLayoutSettings from '../store/useLayoutSettings';

export default () => {
    const { layoutSettings, updateLayoutSettings } = useLayoutSettings()

    const handleDrawerOpen = () => {
        console.log(layoutSettings)
        updateLayoutSettings({ drawerOpen: true })
    };

    return (<AppBar position="static" color="default">
        <Toolbar>
            <IconButton edge="start" aria-label="menu" onClick={handleDrawerOpen}>
                <MenuIcon />
            </IconButton>
            <Typography variant="h6">Bufunfa</Typography>
        </Toolbar>
    </AppBar>)
}