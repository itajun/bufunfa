package au.ivj.bufunfa

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
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

        viewModel.authenticated.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(LoginFragmentDirections.loginSuccess())
            }
        }

        viewModel.tryFromSaved()

        return loginBinding.root
    }
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var userName = ""
    var password = ""
    val authenticated = MutableLiveData(false)
    val loginError = MutableLiveData(false)

    fun tryFromSaved() {
        try {
            getApplication<Application>().getSharedPreferences("credentials", Context.MODE_PRIVATE)
                .let {
                    val userName = it.getString("userName", null)
                    val password = it.getString("password", null)

                    userName?.let {
                        password?.let {
                            viewModelScope.launch {
                                if (GraphQLService.instance.tryConnect(
                                        SERVER_URL,
                                        userName,
                                        password
                                    )
                                ) {
                                    authenticated.value = true
                                }
                            }
                        }
                    }
                }
        } catch (e: Throwable) {
            Log.e(null, e.message, e)
            authenticated.value = false
        }
    }

    fun login() {
        viewModelScope.launch {
            if (GraphQLService.instance.tryConnect(SERVER_URL, userName, password)) {
                getApplication<Application>().getSharedPreferences(
                    "credentials",
                    Context.MODE_PRIVATE
                ).edit(true) {
                    putString("userName", userName)
                    putString("password", password)
                }
                authenticated.value = true
                loginError.value = false
            } else {
                authenticated.value = false
                loginError.value = true
            }
        }
    }
}