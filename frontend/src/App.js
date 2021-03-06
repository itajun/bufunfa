import React from "react";
import Layout from "./components/layout/Layout";
import ApolloClient from "apollo-boost";
import { ApolloProvider } from "@apollo/react-hooks";
import { InMemoryCache } from "apollo-cache-inmemory";
import { DEFAULT_DASHBOARD_SETTINGS } from "./store/useDashboardSettings";
import { DEFAULT_LAYOUT_SETTINGS } from "./store/useLayoutSettings";

const cache = new InMemoryCache();
cache.writeData({
  data: {
    layoutSettings: DEFAULT_LAYOUT_SETTINGS,
    dashboardSettings: DEFAULT_DASHBOARD_SETTINGS,
  },
});

const serverUrl =
  window.location.port === "3000" ? "http://localhost:8080" : "";
const client = new ApolloClient({
  uri: `${serverUrl}/graphql`,
  cache,
  resolvers: {},
  request: (operation) => {
    const token = localStorage.getItem("token");
    operation.setContext({
      headers: {
        authorization: token ? `Basic ${token}` : "",
      },
    });
  },
  onError: ({ graphQLErrors, networkError }) => {
    if (graphQLErrors)
      console.error(`[GraphQL errors]: ${JSON.stringify(graphQLErrors)}`);
    if (networkError) console.error(`[Network error]: ${networkError}`);
  },
});

function App() {
  return (
    <ApolloProvider client={client}>
      <Layout />
    </ApolloProvider>
  );
}

export default App;
