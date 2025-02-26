package com.candy.gumdrops;

import android.graphics.Bitmap;

public class ImageContainer {

    private Bitmap myImage;
    int index;
    String fileDirectory;

    public ImageContainer(int index,Bitmap image){
        this.index = index;
        myImage = image;
    }

    public Bitmap getMyImage() {
        return myImage;
    }

    public int getIndex() {
        return index;
    }
}
