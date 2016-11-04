package ivan.is.awesome.api.object;

import android.graphics.Bitmap;

public class Pokemon {
    private String name;
    private Bitmap pic_pos;
    public Pokemon(String n, Bitmap p){

        name = n;
        pic_pos = p;
    }
    public String getName(){
        return name;
    }
    public Bitmap getPic_pos(){
        return pic_pos;
    }

}
