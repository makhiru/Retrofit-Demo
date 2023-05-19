package com.example.retrofitdemo.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.retrofitdemo.R;
import com.example.retrofitdemo.RecyclerClickListener;
import com.example.retrofitdemo.Retro;
import com.example.retrofitdemo.adapter.ProductsAdapter;
import com.example.retrofitdemo.model.DeleteProduct;
import com.example.retrofitdemo.model.ProductData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProductFragment extends Fragment {

    RecyclerView show_product_recycler;
    ProgressBar progress_bar;

    SharedPreferences preferences;
    List<ProductData.Productdatas> productDataList;
    ProductsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_product, container, false);

        show_product_recycler = view.findViewById(R.id.show_product_recycler);
        progress_bar = view.findViewById(R.id.progress_bar);

        preferences = getContext().getSharedPreferences("Login_pref", MODE_PRIVATE);

        String userId = preferences.getString("user_id", "h");

        if (!userId.contains("h")) {
            Retro.getInstance().retroAPI.getProducts(userId).enqueue(new Callback<ProductData>() {
                @Override
                public void onResponse(Call<ProductData> call, Response<ProductData> response) {
                    if (response.body().getConnection() == 1) {
                        if (response.body().getResult() == 1) {
                            progress_bar.setVisibility(View.GONE);

                            productDataList = response.body().getProductdata();

                            show_product_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                            adapter = new ProductsAdapter(getContext(), productDataList, listener, false);
                            show_product_recycler.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductData> call, Throwable t) {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    RecyclerClickListener listener = new RecyclerClickListener() {
        @Override
        public void onLongClick(View view, ProductData.Productdatas data) {
            PopupMenu popupMenu = new PopupMenu(getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.popup_edit) {
                        List<ProductData.Productdatas> productdatasList = new ArrayList<>();
                        productdatasList.add(data);
                        setFragment(new AddProductFragment(productdatasList));
                    } else if (item.getItemId() == R.id.popup_delete) {
                        progress_bar.setVisibility(View.VISIBLE);
                        Retro.getInstance().retroAPI.deleteProducts(data.getId()).enqueue(new Callback<DeleteProduct>() {
                            @Override
                            public void onResponse(Call<DeleteProduct> call, Response<DeleteProduct> response) {
                                if (response.body().getConnection() == 1) {
                                    if (response.body().getResult() == 1) {
                                        progress_bar.setVisibility(View.GONE);
                                        for (int i = 0; i < productDataList.size(); i++) {
                                            if (productDataList.get(i).getId().equals(data.getId())) {
                                                productDataList.remove(i);
                                                adapter.notifyDataSetChanged();
                                                Toast.makeText(getContext(), "Product Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DeleteProduct> call, Throwable t) {
                                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    return true;
                }
            });
            popupMenu.show();
        }

    };

    public void setFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }
}