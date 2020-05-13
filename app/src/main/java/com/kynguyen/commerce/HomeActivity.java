package com.kynguyen.commerce;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kynguyen.commerce.Admin.AdminMainProductsActivity;
import com.kynguyen.commerce.Model.Products;
import com.kynguyen.commerce.Prevalent.Prevalent;
import com.kynguyen.commerce.ViewHolder.ProductViewHolder;
import com.kynguyen.commerce.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
  private AppBarConfiguration mAppBarConfiguration;
  private DatabaseReference ProductRef;
  private RecyclerView recyclerView;
  private FragmentManager fragmentManager;
  private String type = "";
  RecyclerView.LayoutManager layoutManager;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

//     check admin
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    if (bundle != null){
      type = getIntent().getStringExtra("Admin");
    }

    ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
    Paper.init(this);

    Toolbar toolbar = findViewById(R.id.toolbar);

    toolbar.setTitle("Home");
    setSupportActionBar(toolbar);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!type.equals("Admin")){
          Intent intent = new Intent(HomeActivity.this, CartActivity.class);
          startActivity(intent);
        }
      }
    });
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.nav_view);
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    mAppBarConfiguration = new AppBarConfiguration.Builder(
        R.id.nav_home, R.id.nav_cart, R.id.nav_orders, R.id.nav_category, R.id.nav_setting, R.id.nav_logout_user)
        .setDrawerLayout(drawer)
        .build();
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    NavigationUI.setupWithNavController(navigationView, navController);

    View headerView = navigationView.getHeaderView(0);
    TextView usernameView = headerView.findViewById(R.id.user_profile_name);
    CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

    if (!type.equals("Admin")){
      usernameView.setText(Prevalent.currentOnLineUsers.getName());
      Picasso.get().load(Prevalent.currentOnLineUsers.getImage()).placeholder(R.drawable.profile).into(profileImageView);
    }

    recyclerView = findViewById(R.id.recyclerview_name);
    recyclerView.setHasFixedSize(true);
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), item.getTitle() + " clicked", Snackbar.LENGTH_SHORT);
         Fragment   fragment = null;
         switch (id){
           case R.id.nav_home:
            Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
            fragment = HomeFragment.getFragInstance();
            break;
           case R.id.nav_cart:
             if (!type.equals("Admin")){
               Intent intentCart = new Intent(HomeActivity.this, CartActivity.class);
               startActivity(intentCart);
             }
             break;
           case R.id.nav_setting:
             if (!type.equals("Admin")){
               Intent intentSetting = new Intent(HomeActivity.this, SettingActivity.class);
               startActivity(intentSetting);
               Toast.makeText(HomeActivity.this, "Settings", Toast.LENGTH_SHORT).show();
             }
              break;
           case R.id.nav_orders:
             if (!type.equals("Admin")){
               Intent intentSearch = new Intent(HomeActivity.this, SearchProductsActivity.class);
               startActivity(intentSearch);
             }
             break;
           case  R.id.nav_logout_user:
             if (!type.equals("Admin")){
               Paper.book().destroy();
               Intent logout  = new Intent(HomeActivity.this,MainActivity.class);
               logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(logout);
               Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
               finish();
             }
            break;
         }

          DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
      }
    });

//    navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//      @Override
//      public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//
//
//        if (destination.getId() == R.id.nav_cart){
//          Toast.makeText(HomeActivity.this, "Cart", Toast.LENGTH_SHORT).show();
//        }
//
//
//
//        if (destination.getId() == R.id.nav_category){
//          Toast.makeText(HomeActivity.this, "Category", Toast.LENGTH_SHORT).show();
////
//        }
//
//        if (destination.getId() == R.id.nav_setting){
//
//        }
//
//
//      }
//    });


  }


  @Override
  protected void onStart() {
    super.onStart();
    FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
        .setQuery(ProductRef, Products.class)
        .build();
   FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new  FirebaseRecyclerAdapter<Products, ProductViewHolder>(options){
     @Override
     public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
      ProductViewHolder holder = new  ProductViewHolder(view2);
       return holder;
     }

     @NonNull
     @Override
     protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
      holder.txvProduct_name.setText(model.getname());
      holder.txvProduct_price.setText("Price: " + model.getPrice() + "$");
      holder.txvProduct_description.setText(model.getDescription());
      Picasso.get().load(model.getImages()).fit().into(holder.imageView);

      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (type.equals("Admin")){
            Intent intent = new Intent(HomeActivity.this, AdminMainProductsActivity.class);
            intent.putExtra("pid", model.getPid());
            startActivity(intent);
          }else {
            Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
            intent.putExtra("pid", model.getPid());
            startActivity(intent);
          }
        }
      });
     }
   };
   recyclerView.setAdapter(adapter);
   adapter.startListening();
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.home, menu);
    return true;
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    return NavigationUI.navigateUp(navController, mAppBarConfiguration)
        || super.onSupportNavigateUp();
  }
}
