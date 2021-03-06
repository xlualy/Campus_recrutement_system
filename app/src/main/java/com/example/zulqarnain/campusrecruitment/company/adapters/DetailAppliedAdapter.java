package com.example.zulqarnain.campusrecruitment.company.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zulqarnain.campusrecruitment.R;
import com.example.zulqarnain.campusrecruitment.company.StudentDetailDialogFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Zul Qarnain on 11/16/2017.
 */

public class DetailAppliedAdapter extends RecyclerView.Adapter<DetailAppliedAdapter.ViewJobHolder> {
    private ArrayList<String> stuList;
    private Context mContext;

    public DetailAppliedAdapter(Context context, ArrayList<String> stuList) {
        this.stuList = stuList;
        this.mContext = context;
    }

    @Override
    public ViewJobHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.single_appled_student_details, parent, false);
        ViewJobHolder holder = new ViewJobHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewJobHolder holder, int position) {

        String stuKey = stuList.get(position);
        holder.bindView(stuKey);
    }

    @Override
    public int getItemCount() {
        return stuList.size();
    }

    public class ViewJobHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView stdName;
        private String argName;
        private Button bDetail;
        private String stuKey;
        public ViewJobHolder(View itemView) {
            super(itemView);
            stdName = itemView.findViewById(R.id.applied_stud_name);
            bDetail = itemView.findViewById(R.id.appplied_btn_detail);
            bDetail.setOnClickListener(this);
        }

        public void bindView(String stuKey) {
            this.stuKey = stuKey;
            setStdName();
        }

        public void setStdName() {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("student");
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        if (snap.getKey().equals("name")) {
                            String name = snap.getValue(String.class);
                            if (dataSnapshot.getKey().equals(stuKey)) {
                                argName = name;
                                stdName.setText(name);
                            }
                        }
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        if (snap.getKey().equals("name")) {
                            String name = snap.getValue(String.class);
                            if (dataSnapshot.getKey().equals(stuKey)) {
                                stdName.setText(name);
                                argName=name;
                            }
                        }
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        if (snap.getKey().equals("name")) {
                            int index = getIndex(dataSnapshot.getKey());
                            if (index != -1) {
                                stuList.remove(index);
                                notifyDataSetChanged();
                            }
                        }
                    }


                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public int getIndex(String key) {
            for (int i = 0; i < stuList.size(); i++) {
                if (stuList.get(i).toString().equals(key)) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public void onClick(View view) {
            DialogFragment dialog = StudentDetailDialogFragment.newInstance(stuKey.toString(), argName);
            dialog.show(((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction(), "mydialog");
        }
    }
}
