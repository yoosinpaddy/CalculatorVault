package com.calculator.vault.gallery.locker.hide.data.smartkit.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.MyAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;

/**
 * Created by soham on 11/mandala2/2017.
 */

public class HomeFragment extends Fragment {

    private RecyclerView ll_main;
    private int position;
    String title;

    public HomeFragment() {
    }

    public static Fragment getInstance(int position) {
        HomeFragment f = new HomeFragment();
        Bundle args = new Bundle();
        Share.position = position;
        args.putInt("position", position);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.s_fragment_one, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ll_main = (RecyclerView) view.findViewById(R.id.ll_main);
        getData();
        setContentView();
    }

    private void setContentView() {

        ll_main.setVisibility(View.VISIBLE);
    }

    public void getData() {
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        ll_main.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        MyAdapter mAdapter = new MyAdapter(getActivity().getApplicationContext(),"Home");
        ll_main.setAdapter(mAdapter);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}