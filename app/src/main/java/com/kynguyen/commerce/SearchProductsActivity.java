package com.kynguyen.commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kynguyen.commerce.Model.Products;
import com.kynguyen.commerce.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class SearchProductsActivity extends AppCompatActivity {
  private Button searchBtn;
  private EditText inputSearch;
  private RecyclerView seachList;
  private String searchInput;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_products);
    mapping();

    searchBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        searchInput = inputSearch.getText().toString();
        onStart();
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();

    final DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
    FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
        .setQuery(productRef.orderByChild("name").startAt(searchInput), Products.class)
        .build();

    FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
      @Override
      protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
        holder.txvProduct_name.setText(model.getname());
        holder.txvProduct_price.setText("Price: " + model.getPrice() + "$");
        holder.txvProduct_description.setText(model.getDescription());
        Picasso.get().load(model.getImages()).fit().into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
            intent.putExtra("pid", model.getPid());
            startActivity(intent);
          }
        });
      }

      @NonNull
      @Override
      public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
        ProductViewHolder holder = new  ProductViewHolder(view);
        return holder;
      }
    };

    seachList.setAdapter(adapter);
    adapter.startListening();

  }

  private void mapping() {
    searchBtn = (Button) findViewById(R.id.search_products_btn);
    inputSearch = (EditText) findViewById(R.id.search_product_name);
    seachList = findViewById(R.id.list_search_products);
    seachList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));
  }
}
