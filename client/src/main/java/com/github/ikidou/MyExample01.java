package com.github.ikidou;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by lee on 2016/10/25.
 */

public class MyExample01 {
    public interface BlogServer {
        @GET("task/{id}")
        Call<ResponseBody> getBlogFirst(@Path("id") int id);
    }

    public static void main(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()             //Retrofit實例化。直接使用Retrofit.Builder()
                .baseUrl("http://localhost:8000/")
                .build();

        BlogServer server = retrofit.create(BlogServer.class); //介面類別直接實例化，成為代理物件

        Call<ResponseBody> call = server.getBlogFirst(4);      //代理物件方法，介面的方法，返回介面方法中的返回類別。

        call.enqueue(new Callback<ResponseBody>() {            //代理物件方法，用enqueue執行Callback<ResponseBody>, 後面會自動帶入方法名稱。
                                                               //try - catch 要自已帶入。
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println(response.body().string());

                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
