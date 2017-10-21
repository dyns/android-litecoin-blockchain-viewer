package com.a84934.litecoinblockchainviewer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockVH> {

    interface OnBlockClickListener {
        void blockClicked(Block block);
    }

    final OnBlockClickListener listener;
    BlockAdapter(OnBlockClickListener listener){
        super();
        this.listener = listener;
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
        return 10;
    }

    class BlockVH extends RecyclerView.ViewHolder {
        View root;
        BlockVH(View itemView) {
            super(itemView);
            root = itemView;
        }
    }

}
