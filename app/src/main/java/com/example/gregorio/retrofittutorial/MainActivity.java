package com.example.gregorio.retrofittutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String API_BASE_URL = "https://api.github.com/";
    ListView listView;
    Button button;
    EditText user;

    GitHubClient gitHubClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lv_github_repos);
        button =  findViewById(R.id.button);
        user =  findViewById(R.id.ed_user);


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        // Create a very simple REST adapter which points the GitHub API endpoint.
        gitHubClient = retrofit.create(GitHubClient.class);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userRepo = user.getText().toString();

                // Fetch a list of the Github repositories.
                Call<List<GitHubRepo>> call = gitHubClient.reposForUser(userRepo);

                // Execute the call asynchronously. Get a positive or negative callback.
                call.enqueue(new Callback<List<GitHubRepo>>() {

                    @Override
                    public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                        // The network call was a success and we got a response
                        // TODO: use the repository list and display it
                        if (response !=null){
                            List<GitHubRepo> repos = response.body();
                            listView.setAdapter(new GitHubRepoAdapter(getApplicationContext(), repos));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                        // the network call was a failure
                        // TODO: handle error
                        t.getStackTrace();
                        Toast.makeText(MainActivity.this, "Error Connecting", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }


}
