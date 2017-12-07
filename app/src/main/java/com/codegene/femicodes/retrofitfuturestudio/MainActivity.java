package com.codegene.femicodes.retrofitfuturestudio;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ListView mList;
    EditText mUsername;
    ProgressBar mProgress;
    View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mList = findViewById(R.id.list);
        mUsername = findViewById(R.id.username);
        mProgress = findViewById(R.id.progressBar);
        parentView = findViewById(R.id.parentLayout);

        FloatingActionButton fab =  findViewById(R.id.fab_search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setVisibility(View.VISIBLE);
                  String uname = String.valueOf(mUsername.getText());
                  getRepos(uname);
            }
        });
    }

    public void getRepos(String username){
        String API_BASE_URL = "https://api.github.com/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit = builder
                        .client(
                                httpClient.build()
                        )
                        .build();


        // Create a very simple REST adapter which points the GitHub API endpoint.
        GithubClient client =  retrofit.create(GithubClient.class);

// Fetch a list of the Github repositories.
        Call<List<GitHubRepo>> call =
                client.reposForUser(username);

// Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                // The network call was a success and we got a response
                // TODO: use the repository list and display it

                if(response.isSuccessful()) {
                    List<GitHubRepo> repoList = response.body();

                    //Creating a String array for the Listview
                    String[] repos = new String[repoList.size()];

                    //looping through all the repos
                    for (int i = 0; i < repoList.size(); i++) {
                        repos[i] = repoList.get(i).getName();
                    }

                    //displaying the string array into listview
                    mList.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, repos));
                }else{
                    Snackbar.make(parentView, R.string.username_not_found, Snackbar.LENGTH_LONG).show();
                }

                mProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
                Snackbar.make(parentView, R.string.error_message, Snackbar.LENGTH_LONG).show();
                mProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
