package com.Veiled.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Veiled.Activities.ArchivedActivity;
import com.Veiled.R;
import com.Veiled.Utils.CollectedSticker;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Laur on 5/14/2015.
 */
public class ArchivedStickerAdapter extends BaseAdapter {
    ArrayList<CollectedSticker> collectedStickers;
    Context context;
    private static LayoutInflater inflater=null;

    int width;
    int height;

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FORESLASH = "/";

    public ArchivedStickerAdapter(ArchivedActivity archivedActivity, ArrayList<CollectedSticker> i_collectedStickers,
                                   int i_width, int i_height) {

        context=archivedActivity;
        collectedStickers = i_collectedStickers;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        width = i_width;
        height = i_height;
    }

    @Override
    public int getCount() {
        return collectedStickers.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView name;
        TextView details;
        TextView picked_from;
        TextView created_by;

        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.sticker_archived, null);

        holder.name=(TextView) rowView.findViewById(R.id.stickerArchivedNameTV);
        holder.img=(ImageView) rowView.findViewById(R.id.stickerArchivedIV);
        holder.details = (TextView) rowView.findViewById(R.id.stickerArchivedDetailsTV);
        holder.picked_from = (TextView) rowView.findViewById(R.id.pickedfromInfoArchivedTV);
        holder.created_by = (TextView)rowView.findViewById(R.id.createdbyArchivedInfoTV);

        holder.name.getLayoutParams().width = width;
        holder.name.getLayoutParams().height = height/10;
        holder.name.setTextSize(height/40);

        holder.details.getLayoutParams().width =  width/3;
        holder.details.getLayoutParams().height = height/10;
        holder.details.setTextSize(height/80);

        holder.img.getLayoutParams().width = width/3;
        holder.img.getLayoutParams().height = width/3;

        TextView pickedfromTV = (TextView)rowView.findViewById(R.id.pickedfromArchivedTV);
        pickedfromTV.getLayoutParams().width = width/3;
        pickedfromTV.getLayoutParams().height = height/16;
        pickedfromTV.setTextSize(height/70);

        TextView pickedfromInfoTV = (TextView)rowView.findViewById(R.id.pickedfromInfoArchivedTV);
        pickedfromInfoTV.getLayoutParams().width = width/3;
        pickedfromInfoTV.getLayoutParams().height = height/10;
        pickedfromInfoTV.setTextSize(height/70);

        TextView createdbyTV = (TextView)rowView.findViewById(R.id.createdbyArchivedTV);
        createdbyTV.getLayoutParams().width = width/3;
        createdbyTV.getLayoutParams().height = height/16;
        createdbyTV.setTextSize(height/70);

        TextView createdbyInfoTV = (TextView)rowView.findViewById(R.id.createdbyArchivedInfoTV);
        createdbyInfoTV.getLayoutParams().width = width/3;
        createdbyInfoTV.getLayoutParams().height = height/10;
        createdbyInfoTV.setTextSize(height/70);

        holder.picked_from.setText(collectedStickers.get(position).getPicked_from());
        holder.created_by.setText(collectedStickers.get(position).getCreated_by());
        holder.name.setText(collectedStickers.get(position).getName());
        holder.details.setText(collectedStickers.get(position).getDetails());

        Uri uri = collectedStickers.get(position).getImg_src();
        File file = new File(uri.getPath());
        if(file.exists()){
            holder.img.setImageURI(collectedStickers.get(position).getImg_src());
        }
        else{
            Uri image_uri = Uri.parse(ANDROID_RESOURCE + context.getPackageName()
                    + FORESLASH + R.drawable.default_image);
            holder.img.setImageURI(image_uri);
        }
        return rowView;
    }
}
