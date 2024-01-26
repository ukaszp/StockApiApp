package com.example.stockapiapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<Symbol> stocksList;

    //db
    DatabaseHelper db;

    public CustomAdapter(Context context, ArrayList<Symbol> stocksList) {
        this.context = context;
        this.stocksList = stocksList;
        db = new DatabaseHelper(context);
    }


    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        //get data, set data, handle view click in this method

        //get data
        Symbol symbolRecord = stocksList.get(position);
        String id = symbolRecord.getId();
        String symbol = symbolRecord.getSymbol();

        //set data
        holder.idTv.setText(id);
        holder.symbolTv.setText(symbol);

        //handle item clicks (go to detail record activity)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("RECORD_ID", id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return stocksList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView idTv;
        TextView symbolTv;
        //LinearLayout mainLayout;
        //ImageButton moreButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            idTv = itemView.findViewById(R.id.idTv);
            symbolTv = itemView.findViewById(R.id.symbolTv);


            //Animate Recyclerview
            //translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            //mainLayout.setAnimation(translate_anim);
        }
    }
}
