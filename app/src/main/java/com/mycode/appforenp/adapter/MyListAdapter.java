package com.mycode.appforenp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mycode.appforenp.R;
import com.mycode.appforenp.databinding.RvItemBinding;
import com.mycode.appforenp.models.MyModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.CustomViewHolder> {

    private ArrayList<MyModel> myModel;
    private Context mContext;

    public MyListAdapter(ArrayList<MyModel> myModel, Context mContext) {
        this.myModel = myModel;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate( R.layout.rv_item,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        MyModel model = myModel.get(position);
        holder.header.setText(model.getHeadder());
        holder.dec.setText(model.getDis());
        holder.iv_pic.setImageBitmap(model.getBitmap());

    }

    @Override
    public int getItemCount() {
        return myModel.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView header,dec;
        ImageView iv_pic;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            header = (TextView)itemView.findViewById(R.id.tv_header);
            dec = (TextView)itemView.findViewById(R.id.tv_dec);
            iv_pic = (ImageView) itemView.findViewById(R.id.iv_pic);


        }
    }
}
