package com.example.airpnp.Resources;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.google.android.material.card.MaterialCardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airpnp.R;

import java.util.ArrayList;

public class CardRecyclerViewAdapter extends RecyclerView.Adapter<CardRecyclerViewAdapter.ExampleViewHolder> {
    private ArrayList<PaymentDetails> mExampleList;
    private ItemClickListener itemClickListener;

    public class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_titleName, tv_subText;
        MaterialCardView cardView;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            tv_titleName = itemView.findViewById(R.id.card_hours);
            tv_subText = itemView.findViewById(R.id.card_price);
            cardView = itemView.findViewById(R.id.paymentCard);
            cardView.setBackgroundResource(R.drawable.card_state_regular);
            cardView.setCheckedIconResource(R.drawable.card_state_checked);
            cardView.setCheckedIconTint(null);
            cardView.setOnClickListener(this);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            cardView.setChecked(!cardView.isChecked());
            if (cardView.isChecked()){
                cardView.setBackgroundResource(R.drawable.card_state_checked);
                tv_titleName.setTextColor(Color.WHITE);
                tv_subText.setTextColor(Color.WHITE);
            }
            else{
                cardView.setBackgroundResource(R.drawable.card_state_regular);
                tv_titleName.setTextColor(Color.BLACK);
                tv_subText.setTextColor(Color.BLACK);
            }
            if (itemClickListener != null) itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public CardRecyclerViewAdapter(ArrayList<PaymentDetails> exampleList) {
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_card_view, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ExampleViewHolder holder, final int position) {
        PaymentDetails currentItem = mExampleList.get(position);
        holder.tv_titleName.setText(currentItem.getTitleName());
        holder.tv_subText.setText(currentItem.getSubText());
//        holder.cardView.setOnClickListener(onClickListener);
//        String name = currentItem.getName ();
//        String description = currentItem.getDescription ();
//        holder.tv_name.setText ( name );
//        holder.tv_description.setText ( description );

    }

    @Override
    public int getItemCount() {
        if(mExampleList.isEmpty())
            return 0;
        return mExampleList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
