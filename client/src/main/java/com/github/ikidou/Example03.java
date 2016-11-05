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

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * [Retrofit註解詳解 之 FormUrlEncoded/Field/FieldMap/Multipart/Part/PartMap註解]源碼
 */
public class Example03 {
    public interface BlogService {

        /**
         * {@link FormUrlEncoded} 表明是一個表單格式的請求（Content-Type:application/x-www-form-urlencoded）
         * <code>Field("username")</code> 表示將後面的 <code>String name</code> 中name的取值作為 username 的值
         */
        @POST("/form")
        @FormUrlEncoded
        Call<ResponseBody> testFormUrlEncoded1(@Field("username") String name, @Field("age") int age);

        /**
         * Map的key作為表單的鍵
         */
        @POST("/form")
        @FormUrlEncoded
        Call<ResponseBody> testFormUrlEncoded2(@FieldMap Map<String, Object> map);


        /**
         * {@link Part} 後面支持三種類型，{@link RequestBody}、{@link okhttp3.MultipartBody.Part} 、任意類型
         * 除 {@link okhttp3.MultipartBody.Part} 以外，其它類型都必須帶上表單字段({@link okhttp3.MultipartBody.Part} 中已經包含了表單字段的信息)，
         */
        @POST("/form")
        @Multipart
        Call<ResponseBody> testFileUpload1(@Part("name") RequestBody name, @Part("age") RequestBody age, @Part MultipartBody.Part file);

        /**
         * PartMap 註解支持一個Map作為參數，支持 {@link RequestBody } 類型，
         * 如果有其它的類型，會被{@link retrofit2.Converter}轉換，如後面會介紹的 使用{@link com.google.gson.Gson} 的 {@link retrofit2.converter.gson.GsonRequestBodyConverter}
         * 所以{@link MultipartBody.Part} 就不適用了,所以文件只能用<b> @Part MultipartBody.Part </b>
         */
        @POST("/form")
        @Multipart
        Call<ResponseBody> testFileUpload2(@PartMap Map<String, RequestBody> args, @Part MultipartBody.Part file);
    }

    public static void main(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:4567/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BlogService service = retrofit.create(BlogService.class);


        // 演示 @FormUrlEncoded 和 @Field
        Call<ResponseBody> call1 = service.testFormUrlEncoded1("怪盜kidou", 24);
        ResponseBodyPrinter.printResponseBody(call1);


        //===================================================

        // 演示 @FormUrlEncoded 和 @FieldMap
        // 實現的效果與上面想同
        Map<String, Object> map = new HashMap<>();
        map.put("username", "怪盜kidou");
        map.put("age", 24);
        Call<ResponseBody> call2 = service.testFormUrlEncoded2(map);
        ResponseBodyPrinter.printResponseBody(call2);


        //===================================================


        MediaType textType = MediaType.parse("text/plain");
        RequestBody name = RequestBody.create(textType, "怪盜kidou");
        RequestBody age = RequestBody.create(textType, "24");
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), "這裡是模擬文件的內容");

        // 演示 @Multipart 和 @Part
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "test.txt", file);
        Call<ResponseBody> call3 = service.testFileUpload1(name, age, filePart);
        ResponseBodyPrinter.printResponseBody(call3);

        //===================================================
        // 演示 @Multipart 和 @PartMap
        // 實現和上面同樣的效果
        Map<String, RequestBody> fileUpload2Args = new HashMap<>();
        fileUpload2Args.put("name", name);
        fileUpload2Args.put("age", age);
        //這裡並不會被當成文件，因為沒有文件名(包含在Content-Disposition請求頭中)，但上面的 filePart 有
        //fileUpload2Args.put("file", file);
        Call<ResponseBody> call4 = service.testFileUpload2(fileUpload2Args, filePart); //單獨處理文件
        ResponseBodyPrinter.printResponseBody(call4);
    }
}
