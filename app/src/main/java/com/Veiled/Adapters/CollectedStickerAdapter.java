package com.Veiled.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Veiled.Activities.Barcode;
import com.Veiled.Activities.CollectedActivity;
import com.Veiled.R;
import com.Veiled.Utils.CollectedSticker;

import java.io.File;
import java.util.ArrayList;

public class CollectedStickerAdapter extends BaseAdapter {
    ArrayList<CollectedSticker> collectedStickers;

    Context context;
    private static LayoutInflater inflater=null;

    int width;
    int height;

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FORESLASH = "/";

    public CollectedStickerAdapter(){
        collectedStickers  = new ArrayList<>();
    }
    public CollectedStickerAdapter(CollectedActivity collectedActivity, ArrayList<CollectedSticker> i_collectedStickers,
                                   int i_width, int i_height) {

        context=collectedActivity;
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
        TextView available_until;
        TextView created_by;

        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.sticker_collected, null);
        holder.name=(TextView) rowView.findViewById(R.id.stickerNameTV);
        holder.img=(ImageView) rowView.findViewById(R.id.stickerIV);
        holder.details = (TextView) rowView.findViewById(R.id.stickerDetailsTV);
        holder.picked_from = (TextView) rowView.findViewById(R.id.pickedfromInfoTV);
        holder.available_until = (TextView)rowView.findViewById(R.id.availableuntilInfoTV);
        holder.created_by = (TextView)rowView.findViewById(R.id.createdbyInfoTV);

        holder.name.getLayoutParams().width = width;
        holder.name.getLayoutParams().height = height/10;
        //holder.name.setTextSize(height/40);

        holder.details.getLayoutParams().width =  width/3;
        holder.details.getLayoutParams().height = height/10;
        //holder.details.setTextSize(height/80);

        holder.img.getLayoutParams().width = width/3;
        holder.img.getLayoutParams().height = width/3;

        TextView pickedfromTV = (TextView)rowView.findViewById(R.id.pickedfromTV);
        pickedfromTV.getLayoutParams().width = width/3;
        pickedfromTV.getLayoutParams().height = height/16;
        //pickedfromTV.setTextSize(height/70);

        TextView pickedfromInfoTV = (TextView)rowView.findViewById(R.id.pickedfromInfoTV);
        pickedfromInfoTV.getLayoutParams().width = width/3;
        pickedfromInfoTV.getLayoutParams().height = height/10;
        //pickedfromInfoTV.setTextSize(height/70);

        TextView availableuntilTV = (TextView)rowView.findViewById(R.id.availableuntilTV);
        availableuntilTV.getLayoutParams().width = width/3;
        availableuntilTV.getLayoutParams().height = height/16;
        //availableuntilTV.setTextSize(height/70);

        TextView availableuntilInfoTV = (TextView)rowView.findViewById(R.id.availableuntilInfoTV);
        availableuntilInfoTV.getLayoutParams().width = width/3;
        availableuntilInfoTV.getLayoutParams().height = height/10;
        //availableuntilInfoTV.setTextSize(height/70);

        TextView createdbyTV = (TextView)rowView.findViewById(R.id.createdbyTV);
        createdbyTV.getLayoutParams().width = width/3;
        createdbyTV.getLayoutParams().height = height/16;
        //createdbyTV.setTextSize(height/70);

        TextView createdbyInfoTV = (TextView)rowView.findViewById(R.id.createdbyInfoTV);
        createdbyInfoTV.getLayoutParams().width = width/3;
        createdbyInfoTV.getLayoutParams().height = height/10;
        //createdbyInfoTV.setTextSize(height/70);

        Button barcodeButton = (Button)rowView.findViewById(R.id.barcodeButton);
        barcodeButton.getLayoutParams().width = width/8;
        barcodeButton.getLayoutParams().height = height/12;
        // barcodeButton.setTextSize(height/70);
        barcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //Create the bundle
                Intent i = new Intent(context, Barcode.class);
                Bundle bundle = new Bundle();
                //Add your data from getFactualResults method to bundle
                bundle.putString("campaignId", collectedStickers.get(position).getCampaign_id());
                //Add the bundle to the intent
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });

        holder.picked_from.setText(collectedStickers.get(position).getPicked_from());
        holder.available_until.setText(collectedStickers.get(position).getAvailable_until());
        holder.created_by.setText(collectedStickers.get(position).getCreated_by());
        holder.name.setText(collectedStickers.get(position).getName());
        holder.details.setText(collectedStickers.get(position).getDetails());
        Uri uri = collectedStickers.get(position).getImg_src();
        File file = new File(uri.getPath());
        if(file.exists())
            holder.img.setImageURI(collectedStickers.get(position).getImg_src());
        else {
            Uri image_uri = Uri.parse(ANDROID_RESOURCE + context.getPackageName()
                    + FORESLASH + R.drawable.default_image);
            holder.img.setImageURI(image_uri);
        }
        //holder.img.setImageURI(ImageData.getUriFromFile(collectedStickers.get(position).getCampaign_id()+"_promo"));

        return rowView;
    }

}