package au.ivj.bufunfa.graphql

import au.ivj.bufunfa.CategoryRepository
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class CategoryQueryResolver(val categoryRepository: CategoryRepository) : GraphQLQueryResolver {
    fun categories() = categoryRepository.findAll().toList()
}