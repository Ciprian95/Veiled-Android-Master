package com.Veiled.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Veiled.Components.ScreenDetails.IScreenDetails;
import com.Veiled.Components.ScreenDetails.ScreenDetails;
import com.Veiled.R;
import com.Veiled.Utils.LeftSideElements;
import com.Veiled.Utils.LeftSidePanelElement;

import java.util.List;

public class LeftSidePanelAdapter  extends BaseAdapter implements View.OnClickListener {

    private Context m_context;
    private Activity m_activity;
    private List<LeftSidePanelElement> m_listElements;

    public LeftSidePanelAdapter(Context i_context, Activity i_activity) {
        m_context = i_context;
        m_activity = i_activity;
        m_listElements = LeftSideElements.Init();
    }

    public int getCount() {
        return m_listElements.size();
    }

    public Object getItem(int position) {
        return m_listElements.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        final LeftSidePanelElement entry = m_listElements.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) m_context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.elem_listview_leftpanel, null);
        }

        IScreenDetails screenDetails = ScreenDetails.getInstance(m_activity);
        Point details = screenDetails.getScreenDetails();

        TextView entry_name = (TextView) convertView.findViewById(R.id.currElemName);
        //entry_name.setTextSize(details.x / 50);
        entry_name.setText(entry.getName());

        ImageView entry_pic = (ImageView)convertView.findViewById(R.id.elemPic);
        entry_pic.setImageResource(entry.getPictureId());
        entry_pic.setScaleType(ImageView.ScaleType.FIT_XY);
        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}
