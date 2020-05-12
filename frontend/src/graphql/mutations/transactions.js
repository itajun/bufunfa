import { gql } from 'apollo-boost';

export const CREATE_TRANSACTION = gql`
  mutation CreateTransaction($input: CreateTransactionInput!) {
    createTransaction(input: $input) {
      id
      description
      account {
        id
        name
      }
      category {
        id
        name
      }
      amount
      date
    }  
  }  
`;

export const DELETE_TRANSACTION = gql`
    mutation deleteTransaction($transactionId: Long!) {
        deleteTransaction(transactionId: $transactionId)
    }
`;
