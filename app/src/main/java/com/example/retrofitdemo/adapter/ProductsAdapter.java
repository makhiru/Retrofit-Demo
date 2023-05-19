package com.example.retrofitdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofitdemo.R;
import com.example.retrofitdemo.RecyclerClickListener;
import com.example.retrofitdemo.model.ProductData;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    Context context;
    List<ProductData.Productdatas> productDataList;
    RecyclerClickListener listener;
    boolean all;

    public ProductsAdapter(Context context, List<ProductData.Productdatas> productDataList, boolean all) {
        this.context = context;
        this.productDataList = productDataList;
        this.all = all;
    }

    public ProductsAdapter(Context context, List<ProductData.Productdatas> productDataList, RecyclerClickListener listener, boolean all) {
        this.context = context;
        this.productDataList = productDataList;
        this.listener = listener;
        this.all = all;
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_products, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
        String url = "https://hetsweb.000webhostapp.com/myApp/" + productDataList.get(position).getProImage();
        Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.image_placeholder).into(holder.item_img);
        holder.item_userId.setText("" + productDataList.get(position).getUid());
        holder.item_proName.setText("" + productDataList.get(position).getProName());
        holder.item_proPrice.setText("" + productDataList.get(position).getProPrice());
        holder.item_prodec.setText("" + productDataList.get(position).getProDes());

        if (!all) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(holder.itemView, productDataList.get(holder.getAdapterPosition()));
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_img;
        TextView item_userId, item_proName, item_proPrice, item_prodec;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_img = itemView.findViewById(R.id.item_img);
            item_userId = itemView.findViewById(R.id.item_userId);
            item_proName = itemView.findViewById(R.id.item_proName);
            item_proPrice = itemView.findViewById(R.id.item_proPrice);
            item_prodec = itemView.findViewById(R.id.item_prodec);
        }
    }
}
