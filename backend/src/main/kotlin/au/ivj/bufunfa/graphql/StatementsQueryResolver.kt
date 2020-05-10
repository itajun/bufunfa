package au.ivj.bufunfa.graphql

import au.ivj.bufunfa.GetStatementInput
import au.ivj.bufunfa.service.StatementService
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class StatementsQueryResolver(val statementService: StatementService) : GraphQLQueryResolver {
    fun getStatement(getStatementInput: GetStatementInput) = statementService.getStatement(getStatementInput)
}