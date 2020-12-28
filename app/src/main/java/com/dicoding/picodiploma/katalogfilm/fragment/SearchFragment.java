package com.dicoding.picodiploma.katalogfilm.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dicoding.picodiploma.katalogfilm.BuildConfig;
import com.dicoding.picodiploma.katalogfilm.R;
import com.dicoding.picodiploma.katalogfilm.activity.DetailActivity;
import com.dicoding.picodiploma.katalogfilm.activity.MainActivity;
import com.dicoding.picodiploma.katalogfilm.adapter.MoviesAdapter;
import com.dicoding.picodiploma.katalogfilm.click.ItemClickSupport;
import com.dicoding.picodiploma.katalogfilm.model.Movie;
import com.dicoding.picodiploma.katalogfilm.model.MoviesResponse;
import com.dicoding.picodiploma.katalogfilm.rest.ApiClient;
import com.dicoding.picodiploma.katalogfilm.rest.ApiService;
import com.dicoding.picodiploma.katalogfilm.viewmodel.NowPlayingViewModel;
import com.dicoding.picodiploma.katalogfilm.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Movie> list = new ArrayList<>();

    boolean isSearched;

    RecyclerView tempRecyclerView;
    ProgressBar progressBar;
    SearchViewModel searchViewModel;
    EditText edtSearch;
    Button btnSearch;

    MoviesAdapter moviesAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        edtSearch = view.findViewById(R.id.edit_search);
        btnSearch = view.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);

        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        tempRecyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        moviesAdapter = new MoviesAdapter(list, R.layout.item_movies, getActivity());
        moviesAdapter.setDefaultView(true);
        recyclerView.setAdapter(moviesAdapter);

        searchViewModel.getMovies().observe(this, new SearchFragment.MovieObserver());

        if (savedInstanceState == null){
            searchViewModel.loadMovies("");
        }

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(list.get(position));
            }
        });
    }

    private void showSelectedMovie(Movie movie){
        movie.setTitle(movie.getTitle());
        Intent moveWithObjectIntent = new Intent(getActivity(), DetailActivity.class);
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        startActivity(moveWithObjectIntent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search) {
            progressBar.setVisibility(View.VISIBLE);

            String inputSearch = edtSearch.getText().toString().trim();
            searchViewModel.loadMovies(inputSearch);

            isSearched = true;
        }
    }

    private class MovieObserver implements Observer<List<Movie>> {

        @Override
        public void onChanged(@Nullable List<Movie> movies) {
            if (movies == null) return;
            list.clear();
            list.addAll(movies);
            moviesAdapter.setListMovie(list);

            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("isSearched", isSearched);
    }
}
