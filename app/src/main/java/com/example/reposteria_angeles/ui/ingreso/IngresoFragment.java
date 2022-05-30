package com.example.reposteria_angeles.ui.ingreso;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.reposteria_angeles.databinding.FragmentIngresoBinding;

public class IngresoFragment extends Fragment {

    //private FragmentIngresoBinding binding;
    private FragmentIngresoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        IngresoViewModel ingresoViewModel =
                new ViewModelProvider(this).get(IngresoViewModel.class);

        binding = FragmentIngresoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       // final TextView textView = binding.textGallery;
       // gastoViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}