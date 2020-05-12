import { gql } from "apollo-boost";

export const CREATE_ACCOUNT = gql`
  mutation createAccount($input: CreateAccountInput!) {
    createAccount(input: $input) {
      id
      name
      initialAmount
    }
  }
`;

export const CREATE_CATEGORY = gql`
  mutation createCategory($input: CreateCategoryInput!) {
    createCategory(input: $input) {
      id
      name
    }
  }
`;
