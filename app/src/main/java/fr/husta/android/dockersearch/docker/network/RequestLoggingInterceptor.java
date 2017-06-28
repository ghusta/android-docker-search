package fr.husta.android.dockersearch.docker.network;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestLoggingInterceptor
        implements Interceptor
{

    private static final String TAG = "REQ_LOG_INTERCEPTOR";

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
//        Log.d(TAG, String.format("Sending Request : %s", request.url()));
        System.out.println(String.format("%s - Sending Request : %s", TAG, request.url()));

        Response response = chain.proceed(request);
        // NOP

        return response;
    }

}
