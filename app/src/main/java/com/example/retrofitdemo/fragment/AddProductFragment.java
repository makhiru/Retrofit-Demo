package com.example.retrofitdemo.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.retrofitdemo.R;
import com.example.retrofitdemo.Retro;
import com.example.retrofitdemo.model.AddProductData;
import com.example.retrofitdemo.model.DeleteProduct;
import com.example.retrofitdemo.model.ProductData;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductFragment extends Fragment {

    ImageView add_product_img;
    EditText pro_name_edt, pro_price_edt, pro_dec_edt;
    Button add_product_btn;

    Uri product_uri = Uri.parse("");
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    List<ProductData.Productdatas> productdatasList = new ArrayList<>();

    public AddProductFragment(List<ProductData.Productdatas> productdatasList) {
        this.productdatasList = productdatasList;
    }

    public AddProductFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        add_product_img = view.findViewById(R.id.add_product_img);
        add_product_btn = view.findViewById(R.id.add_product_btn);
        pro_name_edt = view.findViewById(R.id.pro_name_edt);
        pro_price_edt = view.findViewById(R.id.pro_price_edt);
        pro_dec_edt = view.findViewById(R.id.pro_dec_edt);

        preferences = getContext().getSharedPreferences("Login_pref", MODE_PRIVATE);
        editor = preferences.edit();

        if (!productdatasList.isEmpty()) {
            add_product_btn.setText("Update");
            pro_name_edt.setText("" + productdatasList.get(0).getProName());
            pro_price_edt.setText("" + productdatasList.get(0).getProPrice());
            pro_dec_edt.setText("" + productdatasList.get(0).getProDes());
            product_uri = Uri.parse("1");
            String url = "https://hetsweb.000webhostapp.com/myApp/" + productdatasList.get(0).getProImage();
            Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.image_placeholder).into(add_product_img);
        }

        add_product_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 303);
                }
            }
        });

        add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pro_name_edt.getText().toString().isEmpty() && !pro_price_edt.getText().toString().isEmpty() && !pro_dec_edt.getText().toString().isEmpty() && !product_uri.toString().isEmpty()) {
                    String product_name = pro_name_edt.getText().toString();
                    String product_price = pro_price_edt.getText().toString();
                    String product_description = pro_dec_edt.getText().toString();
                    String userId = preferences.getString("user_id", "h");
                    String product_img = getImageFromView(add_product_img);

                    if (!productdatasList.isEmpty()) {
                        ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("Update Product");
                        progressDialog.setMessage("Updating Product...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        Retro.getInstance().retroAPI.updateProducts(productdatasList.get(0).getId(), product_name, product_price, product_description, product_img, productdatasList.get(0).getProImage()).enqueue(new Callback<DeleteProduct>() {
                            @Override
                            public void onResponse(Call<DeleteProduct> call, Response   <DeleteProduct> response) {
                                if (response.body().getConnection() == 1) {
                                    if (response.body().getResult() == 1) {
                                        progressDialog.dismiss();

                                        Toast.makeText(getContext(), "Product updated", Toast.LENGTH_SHORT).show();
                                        setFragment(new ShowProductFragment());
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<DeleteProduct> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("Add Product");
                        progressDialog.setMessage("Adding Product...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        Retro.getInstance().retroAPI.addProduct(userId, product_name, product_price, product_description, product_img).enqueue(new Callback<AddProductData>() {
                            @Override
                            public void onResponse(Call<AddProductData> call, Response<AddProductData> response) {
                                if (response.body().getConnection() == 1) {
                                    if (response.body().getProductaddd() == 1) {

                                        progressDialog.dismiss();

                                        add_product_img.setImageResource(R.drawable.add_image);
                                        product_uri = Uri.parse("");
                                        pro_name_edt.setText("");
                                        pro_price_edt.setText("");
                                        pro_dec_edt.setText("");
                                        Toast.makeText(getContext(), "Product Added", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<AddProductData> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    if (pro_name_edt.getText().toString().isEmpty()) {
                        pro_name_edt.setError("Enter Product Name");
                    }
                    if (pro_price_edt.getText().toString().isEmpty()) {
                        pro_price_edt.setError("Enter Product Price");
                    }
                    if (pro_dec_edt.getText().toString().isEmpty()) {
                        pro_dec_edt.setError("Enter Product description");
                    }
                    if (product_uri.toString().isEmpty()) {
                        Toast.makeText(getContext(), "Pick Image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private String getImageFromView(ImageView view) {
        Bitmap bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] bytes = baos.toByteArray();
        String imageData = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            imageData = Base64.getEncoder().encodeToString(bytes);
        }
        return imageData;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 303) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 101);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == AppCompatActivity.RESULT_OK) {
                product_uri = result.getUri();
                add_product_img.setImageURI(product_uri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == 101) {
                Uri uri = data.getData();
                if (uri != null) {
                    CropImage.activity(uri)
                            .start(getContext(), this);
                }
            }
        }
    }

    public void setFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }
}
