package test.app.everything.network

import okhttp3.Interceptor
import okhttp3.Response
import test.app.everything.utils.Constants.Network.AUTHRIZATION_TYPE
import test.app.everything.utils.Constants.Network.AUTHRIZATION_TYPE_VALUE



/**
 * Custom Interceptor for adding query parameters to the HTTP request.
 *
 * This class implements the Interceptor interface.
 *
 * @see Interceptor
 */

class QueryParameterInterceptor : Interceptor {

    /**
     * Intercepts the HTTP request and adds query parameters.
     *
     * @param chain The interceptor chain.
     * @return The intercepted response.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.newBuilder().addQueryParameter(AUTHRIZATION_TYPE, AUTHRIZATION_TYPE_VALUE).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}