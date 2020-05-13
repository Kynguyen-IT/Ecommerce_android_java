package com.kynguyen.commerce.ui.logout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kynguyen.commerce.R;

public class LogoutFragment extends Fragment {
  private LogoutViewModel logoutViewModel;
  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_logout, container, false);
  }
}
