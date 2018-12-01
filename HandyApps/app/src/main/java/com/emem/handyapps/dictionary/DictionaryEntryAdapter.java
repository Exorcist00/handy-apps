package com.emem.handyapps.dictionary;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.emem.handyapps.database.DictionaryEntry;

import java.util.List;

public class DictionaryEntryAdapter extends ArrayAdapter<DictionaryEntry> {
    private Activity context;
    private List<DictionaryEntry> entries;
    public DictionaryEntryAdapter(Activity context, List<DictionaryEntry> dictionaryEntries){
        super(context, android.R.layout.simple_list_item_1, dictionaryEntries);
        this.context = context;
        this.entries = dictionaryEntries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(android.R.layout.simple_list_item_1, null, true);
            viewHolder = new ViewHolder();
            viewHolder.textView = rowView.findViewById(android.R.id.text1);
            rowView.setTag(viewHolder);
        } else {
          viewHolder = (ViewHolder) rowView.getTag();
        }

        String text = entries.get(position).getWord() + " : " + entries.get(position).getMeaning();
        viewHolder.textView.setText(text);
        return rowView;
    }

    static class ViewHolder{
        public TextView textView;
    }
}
