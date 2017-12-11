package com.reconova.faceid.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.String.valueOf;

public class OKHttpUploadUtils {

      public static void post_file(final String url, final Map<String, Object> map, File file) {
            OkHttpClient client = new OkHttpClient();
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            if (file != null) {
                  RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), file);
                  String filename = file.getName();
                  requestBody.addFormDataPart("faceimage", file.getName(), body);
            }
            /*if (map != null) {
                  for (Map.Entry entry : map.entrySet()) {
                        requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
                  }
            }*/
            requestBody.addFormDataPart(valueOf("uaccount"), valueOf("wsj"));

            //Log.i("lfq", "faceimage:"  + file.getName() + "body " + str);
            Request request = new Request.Builder().url(url).post(requestBody.build()).build();
            client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
                  @Override
                  public void onFailure(Call call, IOException e) {
                        Log.i("lfq", "onFailure");
                  }

                  @Override
                  public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                              String str = response.body().string();
                              Log.i("lfq", response.message() + " , body " + str);

                        } else {
                              Log.i("lfq", response.message() + " error : body " + response.body().string());
                        }
                  }
            });

      }

}