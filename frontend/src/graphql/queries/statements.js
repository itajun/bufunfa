import { gql } from 'apollo-boost';

export const GET_STATEMENT = gql`
    query Statement($input: GetStatementInput) {
        statement(input: $input) {
            transactions {
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
                date
                amount
            }
            initialAccountTotals {
                accountId
                accountName
                amount
            }
            date
        }
    }
`