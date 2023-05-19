package com.example.retrofitdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofitdemo.R;
import com.example.retrofitdemo.Retro;
import com.example.retrofitdemo.adapter.ProductsAdapter;
import com.example.retrofitdemo.model.ProductData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAllProductFragment extends Fragment {

    RecyclerView all_product_recycler;
    ProgressBar progress_bar;

    List<ProductData.Productdatas> productDataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_all_product, container, false);

        all_product_recycler = view.findViewById(R.id.all_product_recycler);
        progress_bar = view.findViewById(R.id.progress_bar);

        Retro.getInstance().retroAPI.getAllProducts().enqueue(new Callback<ProductData>() {
            @Override
            public void onResponse(Call<ProductData> call, Response<ProductData> response) {
                if (response.body().getConnection() == 1) {
                    if (response.body().getResult() == 1) {
                        progress_bar.setVisibility(View.GONE);

                        productDataList = response.body().getProductdata();

                        all_product_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                        ProductsAdapter adapter = new ProductsAdapter(getContext(), productDataList, true);
                        all_product_recycler.setAdapter(adapter);
                    } else {
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductData> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}