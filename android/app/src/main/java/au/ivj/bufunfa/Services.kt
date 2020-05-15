package au.ivj.bufunfa

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

class GraphQLService {
    var apolloClient: ApolloClient? = null

    suspend fun tryConnect(url: String, user: String, password: String): Boolean {
        val credentials =
            Base64.getEncoder().encodeToString("$user:$password".toByteArray(Charsets.UTF_8))

        return try {
            val client = buildApolloClient(url, credentials)
            val response = client.query(MeQuery()).toDeferred().await()

            return response.data?.let {
                Log.i(null, "${response.data} logged in")
                apolloClient = client
                true
            } ?: false
        } catch (e: Throwable) {
            Log.e(null, e.message, e)
            false
        }
    }

    private fun buildApolloClient(url: String, credentials: String): ApolloClient {
        return ApolloClient.builder()
            .serverUrl(url)
            .okHttpClient(
                OkHttpClient().newBuilder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        chain.proceed(
                            original.newBuilder()
                                .header("Authorization", "Basic ${credentials}")
                                .build()
                        )
                    }
                    .sslSocketFactory(
                        buildBlindSocketFactory(),
                        BlindTrustManager()
                    )
                    .hostnameVerifier(BlindHostnameVerifier())
                    .build()
            )
            .build();
    }

    companion object {
        val instance = GraphQLService()
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