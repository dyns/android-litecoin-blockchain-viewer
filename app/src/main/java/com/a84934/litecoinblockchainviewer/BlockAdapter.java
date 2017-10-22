package com.a84934.litecoinblockchainviewer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a84934.litecoinblockchainviewer.Models.BlockDisplayInfo;

import java.util.List;

import io.realm.RealmResults;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockVH> {

    RealmResults<BlockDisplayInfo> blocks;

    public interface OnBlockClickListener {
        void blockClicked(BlockDisplayInfo block);
    }

    final OnBlockClickListener listener;

    public BlockAdapter(RealmResults<BlockDisplayInfo> blocks, OnBlockClickListener listener){
        super();
        this.listener = listener;
        this.blocks = blocks;
    }

    public void setBlocks(RealmResults<BlockDisplayInfo> newBlocks){
        this.blocks = newBlocks;
        this.notifyDataSetChanged();
    }

    @Override
    public BlockVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new BlockVH(layoutInflater.inflate(R.layout.block_card, parent, false));
    }

    @Override
    public void onBindViewHolder(BlockVH holder, int position) {
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.blockClicked(null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blocks.size();
    }

    public int maxHeight(){
        if(blocks.isEmpty()){
            return -1;
        }
        else {
            return blocks.get(0).block_no;
        }
    }

    class BlockVH extends RecyclerView.ViewHolder {
        View root;
        BlockVH(View itemView) {
            super(itemView);
            root = itemView;
        }
    }

}
