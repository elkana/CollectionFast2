package id.co.ppu.collectionfast2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.ppu.collectionfast2.pojo.master.MstLDVClassifications;

/**
 * Created by Eric on 16-Oct-17.
 */

public class AdapterKlasifikasi extends ArrayAdapter<MstLDVClassifications> {
    private Context ctx;
    private List<MstLDVClassifications> list;


    public AdapterKlasifikasi(Context context, int resource, List<MstLDVClassifications> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.list = objects;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MstLDVClassifications getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(this.ctx);
//            TextView tv = (TextView) convertView.findViewById(R.id.nama);
        tv.setPadding(10, 10, 10, 10);
        tv.setTextColor(Color.BLACK);
        tv.setText(list.get(position).getDescription());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

        return tv;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(this.ctx);
        label.setPadding(10,10,10,10);
//            label.setTextColor(Color.BLACK);
        label.setText(list.get(position).getDescription());
        label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

        return label;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
