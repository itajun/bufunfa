import React from 'react'
import Dashboard from './Dashboard'
import About from './About'
import useLayoutSettings from '../store/useLayoutSettings'

export default () => {
    const { layoutSettings: { selectedMenu } } = useLayoutSettings()

    return (<>
        { selectedMenu === 'dashboard' && <Dashboard /> }
        { selectedMenu === 'about' && <About /> }
    </>)
}