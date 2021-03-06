package com.example.zulqarnain.campusrecruitment.company.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zulqarnain.campusrecruitment.R;
import com.example.zulqarnain.campusrecruitment.models.Jobs;
import com.example.zulqarnain.campusrecruitment.company.CompanyJobsAppliedActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Zul Qarnain on 11/7/2017.
 */

public class JobAppliedAdapter extends RecyclerView.Adapter<JobAppliedAdapter.ViewJobHolder> {


    private ArrayList<Jobs> jobList;
    private Context mContext;
    private DatabaseReference ref;
    private int applicantNumber;

    public JobAppliedAdapter(Context context, ArrayList<Jobs> jobList, DatabaseReference ref) {
        this.jobList = jobList;
        this.mContext = context;
        this.ref = ref;
    }

    @Override
    public JobAppliedAdapter.ViewJobHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.single_view_applied_student, parent, false);
        ViewJobHolder holder = new ViewJobHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewJobHolder holder, int position) {

        Jobs jobs = jobList.get(position);
        holder.bindView(jobs);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class ViewJobHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tDes;
        private TextView tAppNum;
        private Jobs mjob;

        public ViewJobHolder(View itemView) {
            super(itemView);
            tDes = itemView.findViewById(R.id.t_com_applied_job_des);
            tAppNum = itemView.findViewById(R.id.t_com_applied_applicant_num);
            itemView.setEnabled(true);
            itemView.setOnClickListener(this);
        }

        public void bindView(Jobs mjob) {
            this.mjob = mjob;
            tDes.setText("Title:" + mjob.getJobDescription());
            getNumberOfApplicants();

        }

        public void getNumberOfApplicants() {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("jobs").child(mjob.getJobKey()).child("canidates");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    applicantNumber = (int) dataSnapshot.getChildrenCount();
                    tAppNum.setText("Number of applicants: " + applicantNumber);
                    if (applicantNumber > 0) {

                        itemView.setEnabled(true);
                    } else {
                        itemView.setEnabled(false);
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onClick(View view) {


            Intent intent = new Intent(mContext.getApplicationContext(), CompanyJobsAppliedActivity.class);
            intent.putExtra("jobKey", mjob.getJobKey());
            intent.putExtra("jobName", mjob.getJobDescription());
            mContext.startActivity(intent);

        }
    }
}
