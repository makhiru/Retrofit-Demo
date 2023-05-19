package com.example.retrofitdemo;

import com.example.retrofitdemo.model.AddProductData;
import com.example.retrofitdemo.model.DeleteProduct;
import com.example.retrofitdemo.model.ProductData;
import com.example.retrofitdemo.model.UserData;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class Retro {
    public String baseUrl = "https://hetsweb.000webhostapp.com";
    public static Retro instance;
    public RetroAPI retroAPI;

    public Retro() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroAPI = retrofit.create(RetroAPI.class);
    }

    public static Retro getInstance() {
        if (instance == null) {
            instance = new Retro();
        }
        return instance;
    }

    public interface RetroAPI {
        @FormUrlEncoded
        @POST("/myApp/register.php")
        Call<UserData> userRegister(@Field("name") String name, @Field("email") String email, @Field("password") String password);

        @FormUrlEncoded
        @POST("/myApp/login.php")
        Call<UserData> userLogin(@Field("email") String email, @Field("password") String password);

        @FormUrlEncoded
        @POST("/myApp/addProduct.php")
        Call<AddProductData> addProduct(@Field("userid") String userId, @Field("pname") String pro_name, @Field("pprize") String pro_price, @Field("pdes") String pro_dec, @Field("productimage") String pro_image);

        @FormUrlEncoded
        @POST("/myApp/viewData.php")
        Call<ProductData> getProducts(@Field("userid") String userId);

        @POST("/myApp/viewAllData.php")
        Call<ProductData> getAllProducts();

        @FormUrlEncoded
        @POST("/myApp/deleteproduct.php")
        Call<DeleteProduct> deleteProducts(@Field("id") String id);

        @FormUrlEncoded
        @POST("/myApp/updateproduct.php")
        Call<DeleteProduct> updateProducts(@Field("id") String id, @Field("name") String name, @Field("price") String price, @Field("description") String description, @Field("imagedata") String imagedata, @Field("imagename") String imagename);
    }
}