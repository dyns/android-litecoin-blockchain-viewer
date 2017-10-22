package com.a84934.litecoinblockchainviewer.Models;

import com.google.gson.JsonObject;

import io.realm.RealmObject;

public class BlockDisplayInfo extends RealmObject{
    public String network;
    public Integer block_no;
    public Integer confirmations;
    public Integer time;
    public String sent_value;
    public String fee;
    public String mining_difficulty;
    public Integer size;
    public String blockhash;
    public String merkleroot;
    public String previous_blockhash;
    public String next_blockhash;
    //public JsonObject txs;
    //public List<String> txs;
    //public RealmList<String> children = new RealmList<>();
}