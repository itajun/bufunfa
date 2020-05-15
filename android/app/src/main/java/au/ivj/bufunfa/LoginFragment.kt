package au.ivj.bufunfa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import au.ivj.bufunfa.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

const val SERVER_URL = "https://192.168.0.5:8443/graphql"

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loginBinding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        loginBinding.viewModel = viewModel
        loginBinding.lifecycleOwner = this

        return loginBinding.root
    }
}

class LoginViewModel : ViewModel() {
    var userName = ""
    var password = ""
    val loginError = MutableLiveData(false)

    fun login(view: View) {
        viewModelScope.launch {
            if (GraphQLService.instance.tryConnect(SERVER_URL, userName, password)) {
                val action =
                    LoginFragmentDirections
                        .loginSuccess()

                view.findNavController().navigate(action, navOptions {  })
                loginError.value = false
            } else {
                loginError.value = true
            }
        }
    }
}