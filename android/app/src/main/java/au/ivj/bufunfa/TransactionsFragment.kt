package au.ivj.bufunfa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.ivj.bufunfa.databinding.FragmentTransactionsBinding
import au.ivj.bufunfa.databinding.ItemTransactionBinding
import com.apollographql.apollo.coroutines.toFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class TransactionsFragment : Fragment() {
    val viewModel: TransactionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTransactionsBinding.inflate(inflater, container, false)

        val itemRecyclerView = binding.itemRecyclerView
        viewModel.transactions.observe(viewLifecycleOwner, Observer { transactions ->
            itemRecyclerView.layoutManager = LinearLayoutManager(context)
            itemRecyclerView.adapter = TransactionAdapter(transactions)
        })
        viewModel.fetchTransactions()

        return binding.root
    }
}

class TransactionsViewModel : ViewModel() {
    val transactions = MutableLiveData<List<Transaction>>()

    @ExperimentalCoroutinesApi
    fun fetchTransactions() { // Should go to a Repository and the service be injected
        viewModelScope.launch {
            GraphQLService.instance.apolloClient
                ?.query(GetTransactionQuery())
                ?.toFlow()
                ?.collect { value ->
                    transactions.value = value
                        ?.data
                        ?.transactions
                        ?.map {
                            Transaction(
                                it?.description ?: "",
                                "${it?.amount ?: ""}",
                                "${it?.date ?: ""}",
                                it?.category?.name ?: ""
                            )
                        }
                        ?.toList()
                }
        }
    }
}

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    class TransactionViewHolder(private val itemTransactionBinding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(itemTransactionBinding.root) {
        fun bind(transaction: Transaction) {
            itemTransactionBinding.transaction = transaction
            itemTransactionBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTransactionBinding.inflate(inflater, parent, false)
        return TransactionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions.get(position))
    }
}

data class Transaction(
    val description: String,
    val amount: String,
    val date: String,
    val category: String
)