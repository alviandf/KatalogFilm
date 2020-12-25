package com.dicoding.picodiploma.katalogfilm.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.dicoding.picodiploma.katalogfilm.BuildConfig;
import com.dicoding.picodiploma.katalogfilm.model.Movie;
import com.dicoding.picodiploma.katalogfilm.model.MoviesResponse;
import com.dicoding.picodiploma.katalogfilm.rest.ApiClient;
import com.dicoding.picodiploma.katalogfilm.rest.ApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NowPlayingViewModel extends ViewModel {

    ApiService apiService;

    private MutableLiveData<List<Movie>> movies;
    private MutableLiveData<Boolean> isLoading;

    private final static String API_KEY = BuildConfig.TMDB_API_KEY;;

    public NowPlayingViewModel() {
        movies = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
    }

    public MutableLiveData<List<Movie>> getMovies() {
        return movies;
    }


    public void loadMovies() {

        apiService = ApiClient.getClient().create(ApiService.class);
        Call<MoviesResponse> call = apiService.getNowPlayingMovies(API_KEY);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                MoviesResponse moviesResponse = response.body();

                if (moviesResponse != null) {
                    setMovies(moviesResponse.getResults());
                } else {
                    setMovies(Collections.<Movie>emptyList());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });
    }

    private void setMovies(List<Movie> movies) {
        this.movies.postValue(movies);
    }
}
