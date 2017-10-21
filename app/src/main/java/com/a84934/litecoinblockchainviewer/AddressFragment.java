package com.a84934.litecoinblockchainviewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    TextView addressAmountTextView;
    ProgressBar progressBar;
    SearchView searchView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchView = (SearchView)getView().findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);

        addressAmountTextView = (TextView)getView().findViewById(R.id.addressAmountTextView);
        progressBar = (ProgressBar)getView().findViewById(R.id.addressProgressBar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        clearContent();
    }

    private void search(String query){
        query = query.trim();
        searchView.setQuery(query, false);

        if(query.isEmpty()){
            Toast.makeText(getContext(), "Address is blank.", Toast.LENGTH_SHORT).show();
            return;
        }

        setSearchView();

        Networking.service().addressBalance(query, new Callback<Networking.CResponse<Networking.AddressBalance>>() {
            @Override
            public void onResponse(Call<Networking.CResponse<Networking.AddressBalance>> call, Response<Networking.CResponse<Networking.AddressBalance>> response) {
                searchDone(response.body());
            }

            @Override
            public void onFailure(Call<Networking.CResponse<Networking.AddressBalance>> call, Throwable t) {
                searchDone(null);
            }
        });
    }

    private void setSearchView(){
        addressAmountTextView.setText(null);
        addressAmountTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void searchDone(Networking.CResponse<Networking.AddressBalance> data){
        Networking.AddressBalance balance = data.data;
        progressBar.setVisibility(View.GONE);
        addressAmountTextView.setVisibility(View.VISIBLE);
        if(balance == null || balance.confirmed_balance == null){
            addressAmountTextView.setText("Invalid Address");
        } else {
            addressAmountTextView.setText(balance.confirmed_balance + " LTC");
        }
    }

    private void clearContent(){
        progressBar.setVisibility(View.GONE);
        addressAmountTextView.setText(null);
        addressAmountTextView.setVisibility(View.GONE);
        searchView.setQuery("", false);
    }

}
