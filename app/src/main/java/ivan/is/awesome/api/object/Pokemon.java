package ivan.is.awesome.api.object;

import android.graphics.Bitmap;

public class Pokemon {
    private String name;
    private Bitmap pic_pos;
    private boolean expanded;
    public Pokemon(String n, Bitmap p){

        name = n;
        pic_pos = p;
    }
    public void setExpansion(boolean set){
        expanded = set;
    }
    public boolean isExpanded(){
        return expanded;
    }
    public String getName(){
        return name;
    }
    public Bitmap getPic_pos(){
        return pic_pos;
    }

}
