package com.clearteam.phuotnhom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clearteam.phuotnhom.R;
import com.clearteam.phuotnhom.model.TourMe;

import java.util.List;

public class TourMeAdapter extends RecyclerView.Adapter<TourMeAdapter.ViewHolder> {

    private List<TourMe> mGroupResponseList;
    private Context mContext;

    private clickDetailTourGroup clickDetailTourGroup;

    public TourMeAdapter(List<TourMe> mGroupResponseList, Context mContext) {
        this.mGroupResponseList = mGroupResponseList;
        this.mContext = mContext;
    }

    public void setClickDetailTourGroup(TourMeAdapter.clickDetailTourGroup clickDetailTourGroup) {
        this.clickDetailTourGroup = clickDetailTourGroup;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tour_me, parent, false));
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
        private ImageView imgAvataGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameGroup = itemView.findViewById(R.id.tvNameGroup);
            tvAddressStart = itemView.findViewById(R.id.tv_address_start);
            tvAddressEnd = itemView.findViewById(R.id.tv_address_end);
            tvDateStart = itemView.findViewById(R.id.tv_date);
           // imgAvataGroup = itemView.findViewById(R.id.imgAvataGroup);

        }

        void bindData(final TourMe response) {
            tvNameGroup.setText(response.getName());
            tvAddressStart.setText(response.getAddressStart());
            tvAddressEnd.setText(response.getAddressEnd());
            tvDateStart.setText(response.getDate());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickDetailTourGroup.onClickDetail(getAdapterPosition(), response);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickDetailTourGroup.onLongClick(getAdapterPosition(), response);
                    return true;
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

        void onLongClick(int adapterPosition, TourMe response);
    }


}
