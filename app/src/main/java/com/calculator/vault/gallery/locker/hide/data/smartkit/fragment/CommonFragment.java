package com.calculator.vault.gallery.locker.hide.data.smartkit.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.MyAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;

/**
 * Created by Bansi on 06-10-2017.
 */

public class CommonFragment extends Fragment {

    private RecyclerView ll_main;
    public String id;
    private String title;

    public CommonFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("id","id"+ Share.selected_tab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.s_fragment_one, container, false);
        Bundle bundle=getArguments();
        id = bundle.getString("id");
        findView(v);
        initAction();
        getData();

        return v;
    }

    private void findView(View v) {
        ll_main = (RecyclerView) v.findViewById(R.id.ll_main);
    }

    private void initAction() {
    }

    public void getData() {

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        ll_main.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        MyAdapter mAdapter = new MyAdapter(getActivity().getApplicationContext(), id);
        ll_main.setAdapter(mAdapter);
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
