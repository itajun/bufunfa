import { useQuery, useApolloClient } from '@apollo/react-hooks';
import { gql } from 'apollo-boost';

const DASHBOARD_SETTINGS = gql`
{
    dashboardSettings @client {
        fromDate
        toDate
        accounts
    }
}
`

export const DEFAULT_DASHBOARD_SETTINGS = {
    __typename: 'DashboardSettings',
    fromDate: new Date().getTime(),
    toDate: new Date().setMonth(new Date().getMonth() + 1),
    accounts: [],
}

const useDashboardSettings = () => {
    const client = useApolloClient();
    const { error, data: {dashboardSettings} } = useQuery(DASHBOARD_SETTINGS);
    if (error) console.error(error)

    const updateDashboardSettings = settings => {
        client.writeQuery({query: DASHBOARD_SETTINGS, data: {dashboardSettings: {...dashboardSettings, ...settings}}})
    }

    return {
        dashboardSettings,
        updateDashboardSettings
    }
}

export default useDashboardSettings