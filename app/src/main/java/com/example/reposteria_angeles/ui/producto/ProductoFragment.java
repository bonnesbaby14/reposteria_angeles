package com.example.reposteria_angeles.ui.producto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

//import com.example.reposteria_angeles.databinding.FragmentHomeBinding;
import com.example.reposteria_angeles.databinding.FragmentProductoBinding;

public class ProductoFragment extends Fragment {

    private FragmentProductoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProductoViewModel productoViewModel =
                new ViewModelProvider(this).get(ProductoViewModel.class);

        binding = FragmentProductoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       // final TextView textView = binding.textHome;
        //productoViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}