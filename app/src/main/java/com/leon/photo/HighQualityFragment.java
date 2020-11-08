package com.leon.photo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import com.leon.photo.databinding.HighQualityFragmentBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HighQualityFragment extends DialogFragment {
    HighQualityFragmentBinding binding;
    private String url;
    static final String KEY = "URL";

    public HighQualityFragment() {
    }

    public static HighQualityFragment newInstance(String url) {
        HighQualityFragment fragment = new HighQualityFragment();
        Bundle args = new Bundle();
        args.putString(KEY, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString((KEY));
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = HighQualityFragmentBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    void initialize() {
        Picasso.get().load(url).into(binding.imageViewHighQuality);
    }

    @Override
    public void onResume() {
        WindowManager.LayoutParams params = Objects.requireNonNull(
                Objects.requireNonNull(getDialog()).getWindow()).getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(getDialog().getWindow()).setAttributes(params);
        super.onResume();
    }
}