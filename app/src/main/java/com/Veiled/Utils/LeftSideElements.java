package com.Veiled.Utils;

import com.Veiled.R;

import java.util.ArrayList;
import java.util.List;

public class LeftSideElements {
    public static List<LeftSidePanelElement> Init(){
        List<LeftSidePanelElement> elems =  new ArrayList<LeftSidePanelElement>();

        String[] elemNames = new String[]{"Collected", "Archived", "Invite a friend", "Settings", "Help"};
        int[] elemPicRes = new int[]{R.drawable.collected, R.drawable.archived,  R.drawable.friends, R.drawable.settings, R.drawable.help };

        for(int i = 0; i < 5 ; i++ ){
            elems.add(new LeftSidePanelElement(elemNames[i],elemPicRes[i]));
        }

        return elems;
    }
}
