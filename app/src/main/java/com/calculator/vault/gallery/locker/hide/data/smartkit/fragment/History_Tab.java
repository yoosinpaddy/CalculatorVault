package com.calculator.vault.gallery.locker.hide.data.smartkit.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.BMIMainActivity;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.BMIListAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.BMIShareData;

import java.util.Collections;

public class History_Tab extends Fragment {
    private View view;
    LinearLayout llshow,ln2;
    BMIListAdapter adapter;
    ListView lv1;
    TextView empty;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.s_activity_history_tab, container, false);

        llshow = (LinearLayout)view.findViewById(R.id.hidelinear);

        lv1 =(ListView)view.findViewById(R.id.list_view1);
        ln2 = (LinearLayout)view.findViewById(R.id.text1);
        TextView emptyText = (TextView)view.findViewById(R.id.empty);
        lv1.setEmptyView(emptyText);
        Collections.reverse(BMIMainActivity.arraydata);
        adapter =new BMIListAdapter(getContext(), BMIMainActivity.arraydata);
        lv1.setAdapter(adapter);
        lv1.setDivider(null);
        lv1.setDividerHeight(0);


        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {

                AlertDialog.Builder adb=new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
                adb.setTitle("Delete History");
                //(Html.fromHtml("<font color='#FF7F27'>Delete?</font>"));
                adb.setMessage("Are you sure you want to delete ? ");
                final int positionToRemove = position;


                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        BMIMainActivity.arraydata.remove(positionToRemove);
                        BMIMainActivity.tinyDB.putListObject(BMIShareData.model,BMIMainActivity.arraydata);
                        adapter.notifyDataSetChanged();
                        if (BMIMainActivity.arraydata.isEmpty()){
                            llshow.setVisibility(View.GONE);
                            ln2.setVisibility(View.VISIBLE);
                        }else
                        {
                            ln2.setVisibility(View.GONE);
                            llshow.setVisibility(View.VISIBLE);
                        }
                    }});
                adb.show();


                return false;
            }
        });

        return view;

    }

    @Override
    public void onResume() {
        if (BMIMainActivity.arraydata.isEmpty()){
            llshow.setVisibility(View.GONE);
            ln2.setVisibility(View.VISIBLE);
        }else
        {
            ln2.setVisibility(View.GONE);
            llshow.setVisibility(View.VISIBLE);
        }

        super.onResume();
    }
}