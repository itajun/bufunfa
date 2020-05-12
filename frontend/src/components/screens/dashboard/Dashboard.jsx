import React, { useEffect } from "react";
import TransactionsTable from "../../panels/TransactionsTable";
import { useQuery, useMutation } from "@apollo/react-hooks";
import { GET_STATEMENT } from "../../../graphql/queries/statements";
import CircularProgress from "@material-ui/core/CircularProgress";
import { DELETE_TRANSACTION } from "../../../graphql/mutations/transactions";
import {
  EVENT_ENTITY_CHANGED,
  subscribeTo,
  unsubscribeToken,
} from "../../../util/events";
import { graphqlDate } from "../../../util/formatters";
import useDashboardSettings from "../../../store/useDashboardSettings";
import { startOfMonth, lastDayOfMonth } from "date-fns";
import { Grid } from "@material-ui/core";
import FilterBar from "./FilterBar";
import AccountsTable from "../../panels/AccountsTable";

export default () => {
  const { dashboardSettings, updateDashboardSettings } = useDashboardSettings();
  const {
    data: statementData,
    loading: statementLoading,
    error: statementError,
    refetch,
  } = useQuery(GET_STATEMENT, {
    variables: {
      input: {
        from: graphqlDate(dashboardSettings.fromDate),
        to: graphqlDate(dashboardSettings.toDate),
        accounts: null, // TODO
      },
    },
  });
  const [deleteTransactionMutation] = useMutation(DELETE_TRANSACTION);

  const deleteTransaction = async (transactionId) => {
    await deleteTransactionMutation({ variables: { transactionId } });
    await refetch();
  };

  useEffect(() => {
    const token = subscribeTo(EVENT_ENTITY_CHANGED, () => {
      refetch(); // Since it is the dashboard, any entity change will require a refetch. These are comming from the 'create' dialogs
    });

    return () => {
      unsubscribeToken(token);
    };
  });

  const handleSetMonth = (date) => {
    updateDashboardSettings({
      fromDate: startOfMonth(date).getTime(),
      toDate: lastDayOfMonth(date).getTime(),
    });
  };

  if (statementLoading || statementError) {
    return <CircularProgress />;
  }

  const initialBalance = statementData.statement.initialAccountTotals.reduce(
    (p, c) => p + c.amount,
    0
  );

  return (
    <Grid container spacing={2}>
      <Grid item xs={12} style={{ marginTop: "8px" }}>
        <FilterBar
          setMonth={handleSetMonth}
          date={dashboardSettings.fromDate}
        />
      </Grid>
      <Grid item xs={12}>
        <Grid container spacing={2}>
          <Grid item sm={12} md>
            <TransactionsTable
              transactions={statementData.statement.transactions}
              initialBalance={initialBalance}
              onDelete={deleteTransaction}
              onConfirm={() => {}}
            />
          </Grid>
          <Grid item sm={12} md={3}>
            <AccountsTable statement={statementData.statement} />
          </Grid>
        </Grid>
      </Grid>
    </Grid>
  );
};
