package ivan.is.awesome.api.object;

import android.graphics.Bitmap;

public class Pokemon {
    private String name;
    private Bitmap pic_pos;
    private boolean expanded;
    private boolean loaded;
    private int position;
    public Pokemon(String n, Bitmap p, boolean b, int pos){
        position = pos;
        name = n;
        pic_pos = p;
        loaded = b;
    }
    public int getPosition(){
        return position;
    }
    public void setBitmap(Bitmap p){
        pic_pos = p;
    }
    public void setExpansion(boolean set){
        expanded = set;
    }
    public boolean isLoaded(){
        return loaded;
    }
    public void setLoaded(boolean b){
        loaded = b;
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
