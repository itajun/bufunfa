import { gql } from "apollo-boost";

export const GET_TRANSACTIONS = gql`
  {
    transactions {
      id
      description
      account {
        name
      }
      category {
        name
      }
      amount
      date
    }
  }
`;
