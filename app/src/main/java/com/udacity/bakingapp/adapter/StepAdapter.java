package com.udacity.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Step> mStepList;
    private ItemClickListener onClickListener;

    public interface ItemClickListener {
        void onListItemClick(List<Step> stepList, int itemIndex);
    }

    public void setData(List<Step> steps) {
        mStepList = steps;
        notifyDataSetChanged();
    }

    public StepAdapter(List<Step> steps, ItemClickListener listener) {
        mStepList = steps;
        onClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        StepViewHolder stepViewHolder = (StepViewHolder) viewHolder;

        Step step = mStepList.get(position);
        stepViewHolder.stepDescription.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mStepList != null) {
            return mStepList.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.step_list_item, parent, false);
        return new StepViewHolder(view);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable
        @BindView(R.id.tv_step_short_description)
        TextView stepDescription;

        public StepViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onListItemClick(mStepList, clickedPosition);
        }
    }
}
