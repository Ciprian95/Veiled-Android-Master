package com.Veiled.Utils;

import com.Veiled.R;

import java.util.ArrayList;
import java.util.List;

public class LeftSideElements {
    public static List<LeftSidePanelElement> Init(){
        List<LeftSidePanelElement> elems =  new ArrayList<LeftSidePanelElement>();

        String[] elemNames = new String[]{"Colectate", "Arhiva", "Invita un prieten","Preferinte", "Setari", "Help"};
        int[] elemPicRes = new int[]{R.drawable.collected, R.drawable.archived,  R.drawable.friends,  R.drawable.iconwhite ,R.drawable.settings, R.drawable.help };

        for(int i = 0; i < 6 ; i++ ){
            elems.add(new LeftSidePanelElement(elemNames[i],elemPicRes[i]));
        }

        return elems;
    }
}
//am adaugat Preferences - Ciprian