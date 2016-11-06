package ivan.is.awesome.api.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ivan.is.awesome.api.object.Pokemon;
import ivan.is.awesome.api.R;

public class ListAdapter extends BaseAdapter implements Filterable {

    private static LayoutInflater inflater=null;
    private ArrayList<Pokemon> unfiltered = new ArrayList<>();
    private ArrayList<Pokemon> filteredPokemon = new ArrayList<>();


    public ListAdapter(Activity a, ArrayList<Pokemon> p){
        filteredPokemon = new ArrayList<>(p);
        inflater = (LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void expand(int position, boolean set){
            Pokemon temp = filteredPokemon.get(position);
            temp.setExpansion(set);
            filteredPokemon.set(position, temp);
        notifyDataSetChanged();
    }


    public int getCount() {
        return filteredPokemon.size();
    }

    public Object getItem(int position) {
        return filteredPokemon.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    public void updateData(ArrayList<Pokemon> data){
        unfiltered = new ArrayList<>(data);
        filteredPokemon = new ArrayList<>();
        filteredPokemon = (ArrayList)unfiltered.clone();
        notifyDataSetChanged();
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {
        if(!filteredPokemon.get(position).isExpanded()) {
            convertView = inflater.inflate(R.layout.list_layout, null);
        }
        else{
            convertView = inflater.inflate(R.layout.expanded_list_layout, null);
        }
            TextView title = (TextView)convertView.findViewById(R.id.title); // title
            ImageView imageView = (ImageView)convertView.findViewById(R.id.list_image);
            imageView.setImageBitmap(filteredPokemon.get(position).getPic_pos());
            title.setText(filteredPokemon.get(position).getName());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredPokemon = (ArrayList<Pokemon>)results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                if(constraint!=null && constraint.length()>0){
                    filteredPokemon.clear();
                    for(int i=0;i<unfiltered.size();i++){
                        if((unfiltered.get(i).getName().toUpperCase()).startsWith(constraint.toString().toUpperCase())) {
                            filteredPokemon.add(unfiltered.get(i));
                        }
                    }
                    results.count=filteredPokemon.size();
                    results.values=filteredPokemon.clone();
                }else{
                    results.count=unfiltered.size();
                    results.values=unfiltered.clone();
                }
                return results;
            }
        };
    }
}
