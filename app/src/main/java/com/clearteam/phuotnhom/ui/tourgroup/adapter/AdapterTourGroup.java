package com.clearteam.phuotnhom.ui.tourgroup.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.ui.tourgroup.model.TourGroupResponse;

import java.util.ArrayList;
import java.util.List;

public class AdapterTourGroup extends RecyclerView.Adapter<AdapterTourGroup.ViewHolder> {

    private List<TourGroupResponse> mGroupResponseList;

    private clickDetailTourGroup clickDetailTourGroup;

    public void setClickDetailTourGroup(AdapterTourGroup.clickDetailTourGroup clickDetailTourGroup) {
        this.clickDetailTourGroup = clickDetailTourGroup;
    }

    public AdapterTourGroup() {
        this.mGroupResponseList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tour_group, parent, false));
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
        private TextView tvNameItemTourGroup;
        private ImageView imgMoreItemTourGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameItemTourGroup = (TextView) itemView.findViewById(R.id.tv_name_item_tour_group);
            imgMoreItemTourGroup = (ImageView) itemView.findViewById(R.id.img_more_item_tour_group);
        }

        void bindData(final TourGroupResponse response) {
            tvNameItemTourGroup.setText(response.getName() + "");
            imgMoreItemTourGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickDetailTourGroup.onMoreClick(getAdapterPosition(), response);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickDetailTourGroup.onClickDetail(getAdapterPosition(), response);
                }
            });
        }


    }

    private void setData(List<TourGroupResponse> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        this.mGroupResponseList.addAll(data);
        notifyDataSetChanged();
    }

    private void clearData() {
        this.mGroupResponseList.clear();
        notifyDataSetChanged();
    }

    public interface clickDetailTourGroup {
        void onClickDetail(int position, TourGroupResponse response);

        void onMoreClick(int position, TourGroupResponse response);
    }


}
