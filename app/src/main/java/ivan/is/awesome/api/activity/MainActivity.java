package ivan.is.awesome.api.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import ivan.is.awesome.api.object.Pokemon;
import ivan.is.awesome.api.R;
import ivan.is.awesome.api.util.ListAdapter;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    ProgressBar bar;
    ImageView connection;
    ListView mDrawerList;
    String api_url = "http://pokeapi.co/api/v2/pokemon/";
    SearchView searchView;
    ListAdapter adapter;
    int previous_pos =-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        connection = (ImageView)findViewById((R.id.connection)) ;
        mDrawerList = (ListView)findViewById(R.id.list_view);
        adapter = new ListAdapter(this, new ArrayList<Pokemon>());
        mDrawerList.setAdapter(adapter);
        if(isNetworkAvailable(this)) {
            RetrieveList task = new RetrieveList();
            task.execute(api_url);
        }else{
            connection.setVisibility(View.VISIBLE);
        }
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int x=0; x<adapter.getCount(); x++){
                    adapter.expand(x, false);
                }
                adapter.expand(position, true);
                searchView.setIconified(true);

            }
        });
    }
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public boolean onQueryTextSubmit(String query){
        return false;
    }
    public boolean onQueryTextChange(String newText){
        if (TextUtils.isEmpty(newText)) {
            adapter.getFilter().filter(null);
        }
        else {
            adapter.getFilter().filter(newText);
        }
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);


        return true;
    }

    public class RetrieveList extends AsyncTask<String, Integer, ArrayList<Pokemon>> {
        int progress=0;
        JSONArray pokemon;
        int total;
        ArrayList<Pokemon> pok;

        @Override
        protected ArrayList<Pokemon> doInBackground(String... params) {
            try {
                pok = new ArrayList<>();
                URL url = new URL(params[0]);
                URLConnection con = url.openConnection();
                String encoding = con.getContentEncoding();
                encoding = encoding == null ? "UTF-8" : encoding;
                total = 700;
                for(int x=0; x<(int)(total/20); x++){
                    URL url_iterate = new URL(params[0]+"?offset="+x*20);
                    URLConnection con_iterate = url_iterate.openConnection();
                    InputStream input = con_iterate.getInputStream();
                    String in_raw = IOUtils.toString(input, encoding);
                    JSONObject result_object_iterate = new JSONObject(in_raw);
                    pokemon = result_object_iterate.getJSONArray("results");
                    for(int i=0; i<pokemon.length(); i++){
                        String temp = pokemon.getJSONObject(i).getString("name");
                        temp = temp.substring(0, 1).toUpperCase() + temp.substring(1);
                        String image_url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+((x*20)+i+1)+".png";
                        Bitmap icon;
                        try {
                            InputStream input_pic = new java.net.URL(image_url).openStream();
                            icon = BitmapFactory.decodeStream(input_pic);
                            pok.add(new Pokemon(temp, icon));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        updateProgress();
                    }
                }

                return pok;
            } catch (Exception e) {
                return null;
            }
        }
        void updateProgress(){
            progress++;
            publishProgress(progress);
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            bar.setVisibility(View.VISIBLE);
            bar.setProgress(0);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try {
                bar.setMax(total + 3);
                adapter.updateData(pok);
                searchView.setVisibility(View.INVISIBLE);
            }catch (Exception e){
                bar.setMax(3);
            }
            bar.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(ArrayList<Pokemon> result) {
            super.onPostExecute(result);
            adapter.updateData(result);
            bar.setVisibility(View.INVISIBLE);
            searchView.setVisibility(View.VISIBLE);
        }
    }
}
