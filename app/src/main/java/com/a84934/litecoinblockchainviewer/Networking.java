package com.a84934.litecoinblockchainviewer;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

class Networking {

    private static Networking networking;
    private ChainService service;
    private final String LITE_COIN = "LTC";

    private Networking(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://chain.so/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ChainService.class);
    }

    class CResponse<I> {
        public String status;
        public I data;
    }

    class AddressBalance {
        public String network;
        public String address;
        public String confirmed_balance;
        public String unconfirmed_balance;
    }

    class NetworkInfo {
        public String name;
        public String acronym;
        public String network;
        public String symbol_htmlcode;
        public String url;
        public String mining_difficulty;
        public Integer unconfirmed_txs;
        public Integer blocks;
        public String price;
        public String price_base;
        public Integer price_update_time;
        public String hashrate;
    }

    class BlockDisplayInfo {
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
        public List<String> txs;
    }

    interface ChainService {
        @GET("get_address_balance/{NETWORK}/{ADDRESS}")
        Call<CResponse<AddressBalance>> addressBalance(@Path("NETWORK") String network, @Path("ADDRESS") String address);

        @GET("get_info/{NETWORK}")
        Call<CResponse<NetworkInfo>> networkInfo(@Path("NETWORK") String network);

        @GET("block/{NETWORK}/{BLOCK}") // BLOCK is BLOCKHASH or BLOCK_NO
        Call<ResponseBody> blockDisplayInfo(@Path("NETWORK") String network, @Path("BLOCK") String blockhashOrId);
    }

    void addressBalance(String address, Callback<CResponse<AddressBalance>> callback){
        service.addressBalance(LITE_COIN, address).enqueue(callback);
    }

    void networkInfo(String address, Callback<CResponse<NetworkInfo>> callback){
        service.networkInfo(LITE_COIN).enqueue(callback);
    }

    void blockInfo(String id, Callback<ResponseBody> callback){
        service.blockDisplayInfo(LITE_COIN, id).enqueue(callback);
    }

    static Networking service(){
        if(networking == null){
            networking = new Networking();
        }

        return networking;
    }



}
