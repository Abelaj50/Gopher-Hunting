package com.example.project4;
/* Credits to sulai on StackOverFlow (https://stackoverflow.com/a/13800208) for the help on threading in parallel. */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Random;

public class MainFragment extends Fragment {
    public static Button theStartButton;
    public static Button theStopButton;
    public static Button theResetButton;
    GameFragment.GopherFinder Player1;
    GameFragment.GopherFinder Player2;

    public void onAttach(@NonNull Context activity) {super.onAttach(activity); }
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState); }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_frag_layout, container, false);    }

    /* Do work here to set up view. */
    public void onViewCreated(@NonNull View view, Bundle savedState) {
        super.onViewCreated(view, savedState);

        /* Do work here to set start and stopped states (set in ModelView, and observe in 2nd fragment.) */
        theStartButton = (Button) getActivity().findViewById(R.id.startButton);
        theStopButton = (Button) getActivity().findViewById(R.id.stopButton);
        theResetButton = (Button) getActivity().findViewById(R.id.resetButton);
        theStopButton.setEnabled(false);
        theResetButton.setEnabled(false);

        //This start button should execute both threads!
        theStartButton.setOnClickListener(e->{
            Toast.makeText(getActivity(), "Starting...", Toast.LENGTH_SHORT).show();
            Player1 = new GameFragment.GopherFinder();
            Player2  = new GameFragment.GopherFinder();
            startMyTask(Player1, "P1");
            startMyTask(Player2, "P2");
            theStopButton.setEnabled(true);
            theStartButton.setEnabled(false);
        });

        //This stop button should end both threads!
        theStopButton.setOnClickListener(e->{
            Toast.makeText(getActivity(), "Stopping...", Toast.LENGTH_SHORT).show();
            if(Player1.getStatus().equals(AsyncTask.Status.RUNNING))
                Player1.cancel(true);

            if(Player2.getStatus().equals(AsyncTask.Status.RUNNING))
                Player2.cancel(true);

            theStopButton.setEnabled(false);
            theResetButton.setEnabled(true);
            GameFragment.winnerMessage.setText("Status: STOPPED");
            GameFragment.winnerMessage.setTextColor(Color.rgb(139,0,0));
        });

        //This restart button will restart the game!
        theResetButton.setOnClickListener(e->{
            Toast.makeText(getActivity(), "Resetting...", Toast.LENGTH_SHORT).show();
            theStartButton.setEnabled(true);
            theStopButton.setEnabled(false);
            theResetButton.setEnabled(false);
            GameFragment.winnerMessage.setText("");
            GameFragment.winnerMessage.setTextColor(Color.rgb(128, 128, 128));
            GameFragment.cleanUp();
        });

    }

    /* This method helps execute threads in parallel. */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    void startMyTask(GameFragment.GopherFinder asyncTask, String... strings) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strings[0]);
        else
            asyncTask.execute(strings[0]);
    }



}
