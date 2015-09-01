package hu.bba.snews.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.bba.snews.R;
import hu.bba.snews.activities.AddActivity;
import hu.bba.snews.activities.DetailsActivity;
import hu.bba.snews.adapters.CustomLayoutAdapter;
import hu.bba.snews.adapters.CustomMainLayoutAdapter;
import hu.bba.snews.models.Content;
import hu.bba.snews.models.ContentDataResponse;
import hu.bba.snews.services.ApiServices;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class MainFragment extends Fragment {

    private static String TAG = MainFragment.class.getSimpleName();
    private static ArrayList<Content> listContent;
    private static CustomMainLayoutAdapter adapter;
    private static int duration = Toast.LENGTH_LONG;

    @Bind(R.id.fragment_main_listView)
    ListView listView;
    @Bind(R.id.fragment_main_fab)
    FloatingActionButton floatingActionButton;

    public static MainFragment newInstance() {

        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View createView = inflater.inflate(R.layout.fragment_main_layout, container, false);
        ButterKnife.bind(this, createView);

        ArrayList<Content> content = new ArrayList<>();

        Gson gson = new GsonBuilder().create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.api_endpoint))
                .setConverter(new GsonConverter(gson))
                .build();

        ApiServices apiService = restAdapter.create(ApiServices.class);

        Callback<ContentDataResponse> callback = new Callback<ContentDataResponse>() {
            @Override
            public void success(ContentDataResponse contentImageDataResponse, Response response) {
                listContent = contentImageDataResponse.getResultList().getImageList();
                adapter.initList(listContent);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast toast = Toast.makeText(getActivity(), R.string.main_feed_unavailable, duration);
                toast.show();
            }
        };

        apiService.getContentImageDataResponse(callback);

        floatingActionButton.attachToListView(listView);

        adapter = new CustomMainLayoutAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, pos, id) -> {
            Intent toDetailsIntent = new Intent(getActivity(), DetailsActivity.class);
            toDetailsIntent.putExtra("Content", content);
            toDetailsIntent.putExtra("Position", pos);
            startActivity(toDetailsIntent);
        });

        return createView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @OnClick(R.id.fragment_main_fab)
    public void onClick(View view) {
        Intent toAddIntent = new Intent(getActivity(), AddActivity.class);
        startActivity(toAddIntent);
    }
}
