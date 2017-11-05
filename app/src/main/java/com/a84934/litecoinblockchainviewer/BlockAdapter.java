package com.a84934.litecoinblockchainviewer;

import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.TextView;

import com.a84934.litecoinblockchainviewer.Models.BlockDisplayInfo;

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
        BlockDisplayInfo block = blocks.get(position);

        holder.hashTV.setText("Hash: " + block.blockhash);
        holder.heightTV.setText("Height:" + Integer.toString(block.block_no));
        holder.totalCoinTV.setText("Coins sent: " + block.sent_value);
        holder.timeTV.setText("Executed at: " + Integer.toString(block.time));
        holder.transactionCountTV.setText("Transaction count: " + 1);

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
        } else {
            return blocks.get(0).block_no;
        }
    }

    class BlockVH extends RecyclerView.ViewHolder {
        View root;
        TextView hashTV, heightTV, totalCoinTV, timeTV, transactionCountTV;
        BlockVH(View itemView) {
            super(itemView);
            root = itemView;
            hashTV = (TextView) root.findViewById(R.id.hashTV);
            heightTV = (TextView) root.findViewById(R.id.heightTV);
            totalCoinTV = (TextView) root.findViewById(R.id.totalCoinMovedTV);
            timeTV = (TextView) root.findViewById(R.id.timeTV);
            transactionCountTV = (TextView) root.findViewById(R.id.transactionCountTV);
        }
    }


}






