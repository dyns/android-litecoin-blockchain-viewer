package com.a84934.litecoinblockchainviewer;

import com.a84934.litecoinblockchainviewer.Models.BlockDisplayInfo;
import com.a84934.litecoinblockchainviewer.Models.ChainInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class Networking {

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

    public class CResponse<I> {
        public String status;
        public I data;
    }

    class AddressBalance {
        public String network;
        public String address;
        public String confirmed_balance;
        public String unconfirmed_balance;
    }

    interface ChainService {
        @GET("get_address_balance/{NETWORK}/{ADDRESS}")
        Call<CResponse<AddressBalance>> addressBalance(@Path("NETWORK") String network, @Path("ADDRESS") String address);

        @GET("get_info/{NETWORK}")
        Call<CResponse<ChainInfo>> networkInfo(@Path("NETWORK") String network);

        @GET("block/{NETWORK}/{BLOCK}") // BLOCK is BLOCKHASH or BLOCK_NO
        Call<CResponse<BlockDisplayInfo>> blockDisplayInfo(@Path("NETWORK") String network, @Path("BLOCK") int height);

        @GET("block/{NETWORK}/{BLOCK}") // BLOCK is BLOCKHASH or BLOCK_NO
        Call<ResponseBody> blockDisplayInfoRaw(@Path("NETWORK") String network, @Path("BLOCK") int height);

    }

    public void addressBalance(String address, Callback<CResponse<AddressBalance>> callback){
        service.addressBalance(LITE_COIN, address).enqueue(callback);
    }

    public void networkInfo(Callback<CResponse<ChainInfo>> callback){
        service.networkInfo(LITE_COIN).enqueue(callback);
    }

    public void blockInfo(int height, Callback<CResponse<BlockDisplayInfo>> callback){
        service.blockDisplayInfo(LITE_COIN, height).enqueue(callback);
    }

    public void blockInfoRaw(int height, Callback<ResponseBody> callback){
        service.blockDisplayInfoRaw(LITE_COIN, height).enqueue(callback);
    }

    public static Networking service(){
        if(networking == null){
            networking = new Networking();
        }

        return networking;
    }



}
