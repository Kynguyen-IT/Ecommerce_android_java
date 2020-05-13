package com.kynguyen.commerce.ViewHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kynguyen.commerce.Intetfave.itemClickLitsner;
import com.kynguyen.commerce.R;

public class backgroundViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
  public ImageView background_image;
  private itemClickLitsner listenr;

  public backgroundViewHolder(@NonNull View itemView) {
    super(itemView);
    background_image = itemView.findViewById(R.id.background_images);
  }

  public void setItemClickListen(itemClickLitsner listenr){
    this.listenr= listenr;
  }


  @Override
  public void onClick(View v) {
    listenr.onClick(v, getAdapterPosition(), false);
  }
}
