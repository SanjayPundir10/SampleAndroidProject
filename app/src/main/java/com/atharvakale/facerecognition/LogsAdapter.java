package com.atharvakale.facerecognition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class LogsAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private List<Map<String, String>> listData;

    public LogsAdapter(Context context, List<Map<String, String>> list) {
        this.mContext = context;
        this.listData = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_logs, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, time;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            time = itemView.findViewById(R.id.time);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Map<String, String> map = listData.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.id.setText(map.get("Id")+" "+map.get("name"));
        viewHolder.time.setText(map.get("date"));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

}
