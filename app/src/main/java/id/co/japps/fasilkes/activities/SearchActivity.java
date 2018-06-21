package id.co.japps.fasilkes.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.adapter.FaskesAdapter;
import id.co.japps.fasilkes.adapter.SearchAdapter;
import id.co.japps.fasilkes.api.Client;
import id.co.japps.fasilkes.api.Service;
import id.co.japps.fasilkes.gps.GPSTracker;
import id.co.japps.fasilkes.objek.Faskes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by OiX on 23/11/2017.
 */

public class SearchActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Faskes> mArrayList;
    private SearchAdapter mAdapter;
    private MenuItem search;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.search_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        loadJSON(0);
    }

    private void loadJSON(int index){
        GPSTracker gps = new GPSTracker(SearchActivity.this);

        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadSearch = serviceAPI.readTeamArray("all",gps.getLatitude(),gps.getLongitude());
        loadSearch.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                String faskesString = response.body().toString();

                Type listType = new TypeToken<ArrayList<Faskes>>() {}.getType();
                mArrayList = getTeamListFromJson(faskesString, listType);

                //Collections.sort(mArrayList,Faskes.BY_JARAK);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new SearchAdapter(mArrayList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    public static <T> ArrayList<T> getTeamListFromJson(String jsonString, Type type) {
        if (!isValid(jsonString)) {
            return null;
        }
        return new Gson().fromJson(jsonString, type);
    }

    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem cari = menu.findItem(R.id.btnSearch);
        cari.setVisible(false);

        search = menu.findItem(R.id.search);
        searchView  = (SearchView) search.getActionView();
        search(searchView);
        search.expandActionView();
        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                View view = SearchActivity.this.getCurrentFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(SearchActivity.this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (mAdapter != null) {
                    mAdapter.getFilter().filter(newText);

                }
                return true;
            }
        });
    }

}
