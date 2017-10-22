package com.a84934.litecoinblockchainviewer.Models;

import io.realm.RealmObject;

public class ChainInfo extends RealmObject {
    public String name;
    public String acronym;
    public String network;
    public String symbol_htmlcode;
    public String url;
    public String mining_difficulty;
    public int unconfirmed_txs;
    public int blocks;
    public String price;
    public String price_base;
    public int price_update_time;
    public String hashrate;
}