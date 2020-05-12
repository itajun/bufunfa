import React from 'react'
import { DatePicker } from '@material-ui/pickers';
import { addMonths, subMonths } from 'date-fns';
import { IconButton } from '@material-ui/core';
import SkipPreviousIcon from '@material-ui/icons/SkipPrevious';
import SkipNextIcon from '@material-ui/icons/SkipNext';

export default ({ setMonth, date }) => {
    return (<>
        <IconButton onClick={() => setMonth(subMonths(date, 1))}><SkipPreviousIcon fontSize='small' /></IconButton>
        <DatePicker
            views={["year", "month"]}
            openTo="month"
            value={date}
            onChange={setMonth}
            autoOk
        />
        <IconButton onClick={() => setMonth(addMonths(date, 1))}><SkipNextIcon fontSize='small' /></IconButton>
    </>)
}