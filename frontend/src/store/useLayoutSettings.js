import { useQuery, useApolloClient } from '@apollo/react-hooks';
import { gql } from 'apollo-boost';

const LAYOUT_SETTINGS = gql`
{
    layoutSettings @client {
        drawerOpen
        selectedMenu
    }
}
`

export const DEFAULT_LAYOUT_SETTINGS = {
    __typename: 'LayoutSettings',
    drawerOpen: false,
    selectedMenu: 'dashboard',
}

const useLayoutSettings = () => {
    const client = useApolloClient();
    const { error, data: { layoutSettings } } = useQuery(LAYOUT_SETTINGS);
    if (error) console.error(error)

    const updateLayoutSettings = settings => {
        client.writeQuery({query: LAYOUT_SETTINGS, data: {layoutSettings: {...layoutSettings, ...settings}}})
    }

    return {
        layoutSettings,
        updateLayoutSettings
    }
}

export default useLayoutSettings