import { useQuery, useApolloClient } from "@apollo/react-hooks";
import { gql } from "apollo-boost";
import { startOfMonth, lastDayOfMonth } from "date-fns";

const DASHBOARD_SETTINGS = gql`
  {
    dashboardSettings @client {
      fromDate
      toDate
      accounts
    }
  }
`;

export const DEFAULT_DASHBOARD_SETTINGS = {
  __typename: "DashboardSettings",
  fromDate: startOfMonth(new Date()).getTime(),
  toDate: lastDayOfMonth(new Date()).getTime(),
  accounts: [],
};

const useDashboardSettings = () => {
  const client = useApolloClient();
  const {
    error,
    data: { dashboardSettings },
  } = useQuery(DASHBOARD_SETTINGS);
  if (error) console.error(error);

  const updateDashboardSettings = (settings) => {
    client.writeQuery({
      query: DASHBOARD_SETTINGS,
      data: { dashboardSettings: { ...dashboardSettings, ...settings } },
    });
  };

  return {
    dashboardSettings,
    updateDashboardSettings,
  };
};

export default useDashboardSettings;
