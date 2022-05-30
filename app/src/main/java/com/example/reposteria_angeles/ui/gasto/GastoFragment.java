package com.example.reposteria_angeles.ui.gasto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.reposteria_angeles.databinding.FragmentGastoBinding;

public class GastoFragment extends Fragment {

    private FragmentGastoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GastoViewModel gastoViewModel =
                new ViewModelProvider(this).get(GastoViewModel.class);

        binding = FragmentGastoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       // final TextView textView = binding.textSlideshow;
        //gastoViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}