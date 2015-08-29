package com.Veiled.Utils;

public class LeftSidePanelElement {
    private String name;
    private int pictureId;

    public LeftSidePanelElement(String i_name, int i_pictureId){
        name = i_name;
        pictureId = i_pictureId;
    }

    public void setName(String i_name){
        name = i_name;
    }
    public String getName(){
        return name;
    }

    public void setPictureId(int i_pictureId){
        pictureId = i_pictureId;
    }
    public int getPictureId(){
        return pictureId;
    }
}
