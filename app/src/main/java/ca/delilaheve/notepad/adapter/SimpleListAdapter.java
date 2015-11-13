package ca.delilaheve.notepad.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ca.delilaheve.notepad.R;

public class SimpleListAdapter extends BaseAdapter {

    private Context context;

    private String[] items;

    public SimpleListAdapter(Context context, int stringArray) {
        this.context = context;
        items = context.getResources().getStringArray(stringArray);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.list_simple_item, null);

        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(getItem(position));

        if(position == items.length-1)
            view.findViewById(R.id.divider).setVisibility(View.GONE);

        return view;
    }

    @Override
    public String getItem(int position) {
        if(position < items.length)
            return items[position];

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return items.length;
    }
}
