package nyc.patrickhill.fuzzchallenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG="MainActivity";
    private ListView myListView=null;
    private ListAdapter customAdapter=null;
    private TextView tvNoInet=null;
    private Button btnRetry=null;
    private final static String MENU_SELECTED = "selected";
    private int selected = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvNoInet = (TextView) findViewById(android.R.id.empty);
        btnRetry=(Button)findViewById(R.id.btnRetry);
        setSupportActionBar(toolbar);

        myListView = (ListView) findViewById(R.id.listViewID);
        customAdapter = new ListAdapter(savedInstanceState);
        if (savedInstanceState!=null){
            selected = savedInstanceState.getInt(MENU_SELECTED);
            myListView.setAdapter(customAdapter);
        }
        else {
            if (isNetworkAvailable()) {
                getData theJsonData = new getData();
                theJsonData.execute();
                tvNoInet.setVisibility(View.GONE);
                btnRetry.setVisibility(View.GONE);
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                tvNoInet.setVisibility(View.VISIBLE);
                btnRetry.setVisibility(View.VISIBLE);
            }
        }

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (customAdapter.getItem(position).getmType().equals("text")){
                    if (isNetworkAvailable()) {
                        Intent in = new Intent(MainActivity.this, MyWebView.class);
                        startActivity(in);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No internet connection, try again later", Toast.LENGTH_SHORT).show();
                    }
                }
                if (customAdapter.getItem(position).getmType().equals("image")){
                    Toast.makeText(getApplicationContext(), "Loading Image", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(MainActivity.this,FullImage.class);
                    in.putExtra("imageURL",customAdapter.getItem(position).getmData());
                    startActivity(in);
                }
            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    getData theJsonData = new getData();
                    theJsonData.execute();
                    tvNoInet.setVisibility(View.GONE);
                    btnRetry.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    tvNoInet.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.all).setChecked(true);
        MenuItem mnuAll = (MenuItem) menu.findItem(R.id.all);
        MenuItem mnuImages = (MenuItem) menu.findItem(R.id.images);
        MenuItem mnuText = (MenuItem) menu.findItem(R.id.text);
        if (selected == -1){
            return true;
        }
        switch (selected){
            case R.id.all:
                mnuAll.setChecked(true);
                break;
            case R.id.images:
                mnuImages.setChecked(true);
                break;
            case R.id.text:
                mnuText.setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.all) {
            item.setChecked(true);
            selected = id;
            customAdapter.setListMode(ListAdapter.ListMode.IMAGES_AND_TEXT);
            myListView.setSelectionAfterHeaderView();
            return true;

        }
        if (id == R.id.images) {
            item.setChecked(true);
            selected = id;
            customAdapter.setListMode(ListAdapter.ListMode.IMAGES_ONLY);
            myListView.setSelectionAfterHeaderView();
            return true;

        }
        if (id == R.id.text) {
            item.setChecked(true);
            selected = id;
            customAdapter.setListMode(ListAdapter.ListMode.TEXT_ONLY);
            myListView.setSelectionAfterHeaderView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class getData extends AsyncTask<String, Void, String> {
        String jsonStr = null;
        ProgressDialog progress = new ProgressDialog(MainActivity.this);
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("http://quizzes.fuzzstaging.com/quizzes/mobile/1/data.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
            try {

                JSONArray itemsArray = new JSONArray(jsonStr);
                customAdapter.setItems(itemsArray);
                String itemID=null;
                String itemType=null;
                String itemDate=null;
                String itemData=null;
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonItem=itemsArray.getJSONObject(i);
                    Item myItem=new Item(jsonItem);
                }
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                Log.e(LOG_TAG, "Error processing Json Data");
            }
            return jsonStr;
        }
        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            myListView.setAdapter(customAdapter);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {

            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.show();

        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        customAdapter.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(MENU_SELECTED, selected);
    }

}



