import React from 'react'
import { ButtonGroup, IconButton } from '@material-ui/core';
import ArrowDropDownIcon from '@material-ui/icons/ArrowDropDown';
import ClickAwayListener from '@material-ui/core/ClickAwayListener';
import Grow from '@material-ui/core/Grow';
import Paper from '@material-ui/core/Paper';
import Popper from '@material-ui/core/Popper';
import MenuItem from '@material-ui/core/MenuItem';
import MenuList from '@material-ui/core/MenuList';
import { EVENT_ENTITY_CHANGED, sendEvent } from '../../util/events';
import { closeAnyOpen, showDialog, EDIT_TRANSACTION, EDIT_CATEGORY, EDIT_ACCOUNT } from '../../util/dialogs';
import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';

export default () => {
    const anchorRef = React.useRef(null)
    const [open, setOpen] = React.useState(false)

    const handleToggle = () => {
        setOpen((prevOpen) => !prevOpen);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const dialogCallback = payload => {
        if (payload && payload.created) {
            sendEvent(EVENT_ENTITY_CHANGED, payload)
        }
        closeAnyOpen()
    }

    const handleOpenDialog = dialog => () => {
        showDialog(dialog, dialogCallback)
        setOpen(false)
    }

    return (
        <>
            <ButtonGroup variant="contained" ref={anchorRef} size='small'>
                <IconButton onClick={handleOpenDialog(EDIT_TRANSACTION)}>
                    <AddCircleOutlineIcon />
                </IconButton>
                <IconButton onClick={handleToggle}>
                    <ArrowDropDownIcon />
                </IconButton>
            </ButtonGroup>
            <Popper open={open} anchorEl={anchorRef.current} role={undefined} transition disablePortal>
                {({ TransitionProps, placement }) => (
                    <Grow
                        {...TransitionProps}
                        style={{
                            transformOrigin: placement === 'bottom' ? 'center top' : 'center bottom',
                        }}
                    >
                        <Paper>
                            <ClickAwayListener onClickAway={handleClose}>
                                <MenuList id="split-button-menu">
                                    <MenuItem
                                        onClick={handleOpenDialog(EDIT_TRANSACTION)}
                                    >
                                        Transaction
                                    </MenuItem>
                                    <MenuItem
                                        onClick={handleOpenDialog(EDIT_ACCOUNT)}
                                    >
                                        Account
                                    </MenuItem>
                                    <MenuItem
                                        onClick={handleOpenDialog(EDIT_CATEGORY)}
                                    >
                                        Category
                                    </MenuItem>
                                </MenuList>
                            </ClickAwayListener>
                        </Paper>
                    </Grow>
                )}
            </Popper>
        </>
    )
}