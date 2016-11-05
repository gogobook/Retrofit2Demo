/*
 * Copyright 2016 ikidou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ikidou;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * [Retrofit註解詳解 之 FormUrlEncoded/Field/FieldMap註解]源碼
 */
public class Example05 {
    public interface BlogService {
        /**
         * 當GET、POST...HTTP等方法中沒有設置Url時，則必須使用 {@link Url}提供
         * 對於Query和QueryMap，如果不是String（或Map的第個泛型參數不是String）時
         * 會被調用toString
         * Url支持的類型有 okhttp3.HttpUrl, String, java.net.URI, android.net.Uri
         * {@link retrofit2.http.QueryMap} 用法和{@link retrofit2.http.FieldMap} 用法一樣，不再說明
         */
        @GET //當有URL註解時，這裡的URL就省略了
        Call<ResponseBody> testUrlAndQuery(@Url String url, @Query("showAll") boolean showAll);

    }

    public static void main(String[] args) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:4567/")
                .build();

        BlogService service = retrofit.create(BlogService.class);

        //演示 @Headers 和 @Header
        Call<ResponseBody> call1 = service.testUrlAndQuery("headers",false);
        ResponseBodyPrinter.printResponseBody(call1);

    }


}
