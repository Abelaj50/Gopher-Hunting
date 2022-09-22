package com.example.project4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;

public class GameFragmentActivity extends FragmentActivity {
    private FragmentManager theFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = theFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.gameFrag, new GameFragment());
        fragmentTransaction.commit();
        theFragmentManager.executePendingTransactions();
    }
}