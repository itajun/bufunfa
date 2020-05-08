import React from 'react';
import Layout from './components/Layout';
import ApolloClient from 'apollo-boost';
import { ApolloProvider } from '@apollo/react-hooks';
import { InMemoryCache } from 'apollo-cache-inmemory';
import { DEFAULT_DASHBOARD_SETTINGS } from './store/useDashboardSettings'
import { DEFAULT_LAYOUT_SETTINGS } from './store/useLayoutSettings';

const cache = new InMemoryCache();
cache.writeData({
  data: {
    layoutSettings: DEFAULT_LAYOUT_SETTINGS,
    dashboardSettings: DEFAULT_DASHBOARD_SETTINGS
  },
});

const client = new ApolloClient({
  uri: 'http://localhost:8080/graphql',
  cache,
  resolvers: {}
});

function App() {
  return (
    <ApolloProvider client={client}>
      <Layout />
    </ApolloProvider>
  );
}

export default App;
