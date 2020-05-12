import { gql } from "apollo-boost";

export const GET_CATEGORIES = gql`
  query Categories {
    categories {
      id
      name
    }
  }
`;

export const GET_ACCOUNTS = gql`
  query Accounts {
    accounts {
      id
      name
      initialAmount
    }
  }
`;
