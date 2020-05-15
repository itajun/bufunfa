package au.ivj.bufunfa

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import au.ivj.bufunfa.databinding.ActivityMainBinding
import au.ivj.bufunfa.type.CreateCategoryInput
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.*

class MainActivity : AppCompatActivity() {
    private val model: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.model = model
        mainBinding.lifecycleOwner = this
    }
}

class CategoryViewModel : ViewModel() {
    val categoryName = MutableLiveData<String>("Initial")

    val apolloClient: ApolloClient by lazy {
        ApolloClient.builder()
            .serverUrl("https://192.168.0.5:8080/graphql")
            .okHttpClient(
                OkHttpClient().newBuilder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        chain.proceed(
                            original.newBuilder()
                                .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                                .build()
                        )
                    }
                    .sslSocketFactory(buildBlindSocketFactory(), BlindTrustManager())
                    .hostnameVerifier(BlindHostnameVerifier())
                    .build()
            )
            .build();
    }

    fun createCategory() {
        GlobalScope.launch {
            try {
                apolloClient.mutate(
                    CreateCategoryMutation(
                        CreateCategoryInput(
                            categoryName.value ?: ""
                        )
                    )
                )
                    .toDeferred()
                    .await()

                categoryName.postValue("Done!")
            } catch (e: Throwable) {
                Log.e("XPTO", e.message, e)
            }
        }
    }

    // Don't do this in the real world :). Do this instead: https://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https/6378872#6378872
    private class BlindTrustManager : X509TrustManager {
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) = Unit
        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) = Unit
        override fun getAcceptedIssuers() = emptyArray<X509Certificate>()
    }

    private fun buildBlindSocketFactory(): SSLSocketFactory {
        val ctx: SSLContext = SSLContext.getInstance("TLS")
        ctx.init(null, arrayOf(BlindTrustManager()), null)
        return ctx.getSocketFactory()
    }

    private class BlindHostnameVerifier : HostnameVerifier {
        override fun verify(p0: String?, p1: SSLSession?) = true
    }
}

data class Category(
    val name: String,
    val id: Long? = null
)

