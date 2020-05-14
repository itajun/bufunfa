GraphQL schema can be downloaded via:

`gradlew :app:downloadApolloSchema "-Pcom.apollographql.apollo.endpoint=http://localhost:8080/graphql" "-Pcom.apollographql.apollo.schema=schema.json" -Pcom.apollographql.apollo.headers="Authorization=Basic YWRtaW46YWRtaW4="`

Then, after creating the queries (`.graphql`)
`gradlew generateApolloSources`

We aren't enabling CLEAR_TEXT, so calls have to be made over HTTPS. See instructions in the backend `readme.md` file to set it up.