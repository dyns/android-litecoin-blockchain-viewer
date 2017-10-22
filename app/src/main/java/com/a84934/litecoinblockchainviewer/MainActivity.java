package com.a84934.litecoinblockchainviewer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.a84934.litecoinblockchainviewer.ChainFragment.BlockChainFragment;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(getApplicationContext());

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "Chain";
                    case 1:
                        return "Address";
                    default:
                        return null;
                }
            }

            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return new BlockChainFragment();
                    case 1:
                        return new AddressFragment();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });



    }
}
