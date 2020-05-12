import React from 'react'
import Dashboard from '../screens/dashboard/Dashboard'
import About from '../screens/About'
import useLayoutSettings from '../../store/useLayoutSettings'

export default () => {
    const { layoutSettings: { selectedMenu } } = useLayoutSettings()

    return (<>
        { selectedMenu === 'dashboard' && <Dashboard /> }
        { selectedMenu === 'about' && <About /> }
    </>)
}