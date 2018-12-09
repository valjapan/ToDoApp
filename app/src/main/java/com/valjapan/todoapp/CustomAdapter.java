package com.valjapan.todoapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<ToDoData> {
    private List<ToDoData> mCards;

    public CustomAdapter(Context context, int layoutResourceId, List<ToDoData> userData) {
        super(context, layoutResourceId, userData);

        this.mCards = userData;
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Nullable
    @Override
    public ToDoData getItem(int position) {
        return mCards.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();

        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_view, null);
            viewHolder = new ViewHolder();

            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
            viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.content_text_view);
            convertView.setTag(viewHolder);

        }

        ToDoData toDoData = mCards.get(position);

        viewHolder.titleTextView.setText(toDoData.getTitle());
        viewHolder.contentTextView.setText(toDoData.getContext());

        return convertView;
    }


    public ToDoData getUserDataKey(String key) {
        for (ToDoData toDoData : mCards) {
            if (toDoData.getFirebaseKey().equals(key)) {
                return toDoData;
            }
        }

        return null;
    }

    static class ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
    }

}