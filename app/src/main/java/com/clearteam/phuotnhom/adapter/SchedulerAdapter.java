package com.clearteam.phuotnhom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.TourMe;

import java.util.List;

public class SchedulerAdapter extends RecyclerView.Adapter<SchedulerAdapter.ViewHolder> {

    private List<TourMe> mGroupResponseList;
    private Context mContext;
    private clickDetailTourGroup clickDetailTourGroup;
    private itemClick itemClick;

    public SchedulerAdapter(List<TourMe> mGroupResponseList, Context mContext) {
        this.mGroupResponseList = mGroupResponseList;
        this.mContext = mContext;
    }

    public void setClickDetailTourGroup(SchedulerAdapter.clickDetailTourGroup clickDetailTourGroup) {
        this.clickDetailTourGroup = clickDetailTourGroup;
    }

    public void setIClick(SchedulerAdapter.itemClick clickDetailTourGroup) {
        this.itemClick = clickDetailTourGroup;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tour_all, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mGroupResponseList.size() > 0) {
            holder.bindData(mGroupResponseList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mGroupResponseList == null ? 0 : mGroupResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameGroup;
        private TextView tvAddressStart;
        private TextView tvAddressEnd;
        private TextView tvDateStart;
        private TextView tvAdd;
//        private ImageView imgMoreItemTourGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameGroup = itemView.findViewById(R.id.tvNameGroup);
            tvAddressStart = itemView.findViewById(R.id.tv_address_start);
            tvAddressEnd = itemView.findViewById(R.id.tv_address_end);
            tvDateStart = itemView.findViewById(R.id.tv_date);
            tvAdd = itemView.findViewById(R.id.tv_add);
        }

        void bindData(final TourMe tourMe) {
            tvNameGroup.setText(tourMe.getName());
            tvAddressStart.setText(tourMe.getAddressStart());
            tvAddressEnd.setText(tourMe.getAddressEnd());
            tvDateStart.setText(tourMe.getDate());
            tvAdd.setText(tourMe.getTvAdd());
            tvAdd.setBackgroundColor(mContext.getResources().getColor(R.color.green));

            if (!tourMe.isMyTour()) {
                tvAdd.setBackgroundColor(mContext.getResources().getColor(R.color.bg_tab));
            }


//            tvAdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    tvAdd.setText("Đợi phê duyệt");
//                    clickDetailTourGroup.onClickDetail(getAdapterPosition(), tourMe);
//                }
//            });
//
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onIClick(getAdapterPosition(), tourMe);
                }
            });
        }
    }

    public void setData(List<TourMe> data) {
        this.mGroupResponseList = data;
        notifyDataSetChanged();
    }

    private void clearData() {
        this.mGroupResponseList.clear();
        notifyDataSetChanged();
    }

    public interface clickDetailTourGroup {
        void onClickDetail(int position, TourMe response);
    }

    public interface itemClick {
        void onIClick(int position, TourMe response);
    }

}
