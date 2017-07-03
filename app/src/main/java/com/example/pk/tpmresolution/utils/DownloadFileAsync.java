package com.example.pk.tpmresolution.utils;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kien on 3/21/2017.
 */

public class DownloadFileAsync extends AsyncTask<String, Void, byte[]> {
    static OkHttpClient client = new OkHttpClient();
    public AsyncResponse delegate = null;

    public DownloadFileAsync(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    protected byte[] doInBackground(String... params) {
        byte[] result = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(params[0]).build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download file: " + response);
            }

            if(response.body()!=null) {
                result = response.body().bytes();
                Log.d("kien", "result: "+response.body().string());
            }else Log.d("kien", "result: null");
        }catch (Exception e){
            Log.d("kien", "error network: "+e.toString());
        }
        return result;
    }

    protected void onPostExecute(byte[] s) {
        delegate.processFinish(s);
    }

    public interface AsyncResponse {
        void processFinish(byte[] output);
    }
}

