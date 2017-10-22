package com.a84934.litecoinblockchainviewer.ChainFragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.a84934.litecoinblockchainviewer.BlockAdapter;
import com.a84934.litecoinblockchainviewer.BlockDetailActivity;
import com.a84934.litecoinblockchainviewer.Models.BlockDisplayInfo;
import com.a84934.litecoinblockchainviewer.Models.ChainInfo;
import com.a84934.litecoinblockchainviewer.Networking;
import com.a84934.litecoinblockchainviewer.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockChainFragment extends Fragment {


    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView (
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_block_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = (RecyclerView)getView().findViewById(R.id.blockList);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RealmResults<BlockDisplayInfo> res = realm.where(BlockDisplayInfo.class).findAllSorted("block_no", Sort.DESCENDING);
        final BlockAdapter adapter = new BlockAdapter(res, new BlockAdapter.OnBlockClickListener() {
            @Override
            public void blockClicked(BlockDisplayInfo block) {
                startActivity(new Intent(getContext(), BlockDetailActivity.class));
            }
        });

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                RealmResults<BlockDisplayInfo> res = realm.where(BlockDisplayInfo.class).findAllSorted("block_no", Sort.DESCENDING);
                adapter.setBlocks(res);
            }
        });

        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.pullRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshHeight();
            }
        });
    }

    private void refreshHeight(){
        Networking.service().networkInfo(new Callback<Networking.CResponse<ChainInfo>>() {
            @Override
            public void onResponse(Call<Networking.CResponse<ChainInfo>> call, Response<Networking.CResponse<ChainInfo>> response) {
                swipeRefreshLayout.setRefreshing(false);
                Networking.CResponse<ChainInfo> respBod = response.body();
                if( respBod != null){
                    updateNetworkStatus(respBod.data);
                } else {
                    Toast.makeText(getContext(), getString(R.string.network_status_call_fail), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Networking.CResponse<ChainInfo>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), getString(R.string.network_status_call_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    Realm realm = Realm.getDefaultInstance();

    private final int MAX_PAGINATION = 10;

    private void updateNetworkStatus(@NonNull final ChainInfo newInfo){

        ChainInfo oldInfo = realm.where(ChainInfo.class).findFirst();
        int oldHeight;
        int newHeight = newInfo.blocks;
        if(oldInfo == null){
            oldHeight = Math.max(newHeight - MAX_PAGINATION, 0);
        } else {
            oldHeight = oldInfo.blocks;
        }

        int nextBlock = oldHeight + 1;

        if(oldInfo == null || newInfo.price_update_time != oldInfo.price_update_time){
            realm.beginTransaction();
            realm.delete(ChainInfo.class);
            realm.copyToRealm(newInfo);
            realm.commitTransaction();
        }

        if(oldHeight < newHeight){
            int delta = newHeight - oldHeight;
            if(delta <= MAX_PAGINATION){
                handleDownloadAllNewBlocks(new Point(nextBlock, newHeight));
            } else {
                handleDownloadMaxNewBlocks(nextBlock);
            }
        }
    }

    interface OnBlocksDownloadedListener {
        void blocksFetched();
    }

    private void handleDownloadAllNewBlocks(Point range){
        downloadBlocks(range, new OnBlocksDownloadedListener() {
            @Override
            public void blocksFetched() {
                // on success callback, hide load more new
                // hide download spinner
            }
        });
    }

    private void handleDownloadMaxNewBlocks(int start){
        // show load more new
        downloadBlocks(new Point(start, start + (MAX_PAGINATION - 1)), new OnBlocksDownloadedListener() {
            @Override
            public void blocksFetched() {
                // hide download spinner
            }
        });
    }

    private void downloadBlocks(Point range, final OnBlocksDownloadedListener listener){
        swipeRefreshLayout.setRefreshing(true);
        realm.beginTransaction();

        final AtomicInteger completed = new AtomicInteger(0);
        final int total = (range.y - range.x) + 1;
        for(int i = range.x; i <= range.y; i++){
            final int height = i;

            Networking.service().blockInfo(i, new Callback<Networking.CResponse<BlockDisplayInfo>>() {
                @Override
                public void onResponse(Call<Networking.CResponse<BlockDisplayInfo>> call, Response<Networking.CResponse<BlockDisplayInfo>> response) {

                    Networking.CResponse<BlockDisplayInfo> respBody = response.body();
                    if( respBody != null){
                        realm.copyToRealm(respBody.data);
                    } else {
                        Log.d("block", "null block");
                    }

                    if(completed.incrementAndGet() == total){
                        realm.commitTransaction();
                        swipeRefreshLayout.setRefreshing(false);
                        listener.blocksFetched();
                    }
                }

                @Override
                public void onFailure(Call<Networking.CResponse<BlockDisplayInfo>> call, Throwable t) {
                    Toast.makeText(getContext(), "Unable to get block at height: " + height, Toast.LENGTH_SHORT).show();
                    if(completed.incrementAndGet() == total){
                        realm.commitTransaction();
                        swipeRefreshLayout.setRefreshing(false);
                        listener.blocksFetched();
                    }
                }
            });

        }

    }

}
