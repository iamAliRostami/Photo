package com.leon.photo;

import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leon.photo.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final long READ_TIMEOUT = 120;
    private static final long WRITE_TIMEOUT = 60;
    private static final long CONNECT_TIMEOUT = 10;
    private static final boolean RETRY_ENABLED = true;
    ArrayList<photoFeedBack> photosFeedBack;
    ActivityMainBinding binding;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Retrofit retrofit = getInstance();
        photo photo = retrofit.create(MainActivity.photo.class);
        Call<ArrayList<photoFeedBack>> call = photo.getPhoto();
        if (isOnline(getApplicationContext()))
            call.enqueue(new Callback<ArrayList<photoFeedBack>>() {
                @Override
                public void onResponse(@NotNull Call<ArrayList<photoFeedBack>> call,
                                       @NotNull Response<ArrayList<photoFeedBack>> response) {
                    customAdapter = new CustomAdapter(response.body(), getApplicationContext());
                    customAdapter.notifyDataSetChanged();
                    binding.recyclerView.setAdapter(customAdapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
                        @Override
                        public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent,
                                                                     @NonNull View child,
                                                                     @NonNull Rect rect, boolean immediate) {
                            return false;
                        }
                    });

//                    Picasso.get().load(response.body().get(1).url).into(binding.imageView);
                }

                @Override
                public void onFailure(@NotNull Call<ArrayList<photoFeedBack>> call,
                                      @NotNull Throwable t) {
                    Log.e("Error", t.getLocalizedMessage());

                }
            });
        else
            Toast.makeText(getApplicationContext(), "اینترنت متصل نیست.", Toast.LENGTH_LONG).show();
    }

    private Uri buildURI(String url, Map<String, String> params) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        return builder.build();
    }

    public static class photoFeedBack {
        int albumId;
        int id;
        String title;
        String url;
        String thumbnailUrl;
    }

    public interface photo {
        @GET("photos")
        Call<ArrayList<photoFeedBack>> getPhoto();
    }

    Retrofit getInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    OkHttpClient getHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient
                .Builder()
                .readTimeout(READ_TIMEOUT, TIME_UNIT)
                .writeTimeout(WRITE_TIMEOUT, TIME_UNIT)
                .connectTimeout(CONNECT_TIMEOUT, TIME_UNIT)
                .retryOnConnectionFailure(RETRY_ENABLED)
                .addInterceptor(interceptor).build();
    }


    boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null &&
                Objects.requireNonNull(cm.getActiveNetworkInfo()).isConnectedOrConnecting();
    }
}