package com.example.reposteria_angeles.ui.cliente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.reposteria_angeles.databinding.FragmentClienteBinding;


public class ClienteFragment extends Fragment {

        private FragmentClienteBinding binding;

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            

            binding = FragmentClienteBinding.inflate(inflater, container, false);
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

