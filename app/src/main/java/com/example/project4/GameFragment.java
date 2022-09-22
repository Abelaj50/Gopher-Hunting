package com.example.project4;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/* This class sets up the Game Fragment as well as defines the entire gameplay functionality. */
public class GameFragment extends Fragment {

    public static Boolean gopherFound = false;
    public static int gopherIndex;
    public static int gopherIndexRow;
    public static int gopherIndexCol;

    public GridView player1Grid;
    public GridView player2Grid;
    public ListView player1GuessList;
    public ListView player2GuessList;
    public static TextView winnerMessage;

    public static ArrayList<String> board1 = new ArrayList<>();
    public static ArrayList<String> board2 = new ArrayList<>();
    public static ArrayList<String> list1 = new ArrayList<>();
    public static ArrayList<String> list2 = new ArrayList<>();

    public static ArrayAdapter<String> p1GridAdapter;
    public static ArrayAdapter<String> p2GridAdapter;
    public static ArrayAdapter<String> p1ListAdapter;
    public static ArrayAdapter<String> p2ListAdapter;

    public static ArrayList<String> feedbackList = new ArrayList<>(Arrays.asList("Success", "Near miss", "Close guess", "Complete miss"));
    public static ArrayList<Integer> possibleClosePos = new ArrayList<>(Arrays.asList(22, 21, 20, 19, 18, 12, 8, 2, -2, -8, -12, -18, -19, -20, -21, -22));
    public static ArrayList<Integer> possibleNearPos = new ArrayList<>(Arrays.asList(11, 10, 9, 1, -1, -9, -10, -11));

    public void onAttach(@NonNull Context activity) {super.onAttach(activity); }
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState); }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_frag_layout, container, false);    }

    public void onViewCreated(@NonNull View view, Bundle savedState) {
        super.onViewCreated(view, savedState);

        /* Do work here to set up entire game. */
        player1Grid = getActivity().findViewById(R.id.gridView1);
        player2Grid = getActivity().findViewById(R.id.gridView2);
        player1GuessList = getActivity().findViewById(R.id.listView1);
        player2GuessList = getActivity().findViewById(R.id.listView2);
        winnerMessage = getActivity().findViewById(R.id.winnerText);

        p1GridAdapter = new ArrayAdapter<>(getActivity(), R.layout.textview_layout, R.id.textView, board1);
        p2GridAdapter = new ArrayAdapter<>(getActivity(), R.layout.textview_layout, R.id.textView, board2);
        p1ListAdapter = new ArrayAdapter<>(getActivity(), R.layout.textview_layout, R.id.textView, list1);
        p2ListAdapter = new ArrayAdapter<>(getActivity(), R.layout.textview_layout, R.id.textView, list2);

        player1Grid.setAdapter(p1GridAdapter);
        player2Grid.setAdapter(p2GridAdapter);
        player1GuessList.setAdapter(p1ListAdapter);
        player2GuessList.setAdapter(p2ListAdapter);

        /* Setting up default board. */
        for(int i = 0; i < 100; i++) {
            board1.add("-");
            board2.add("-");
        }

        /* Setting up gopher related info. */
        gopherIndex = new Random().nextInt(100);
        gopherIndexRow = gopherIndex / 10;
        gopherIndexCol = gopherIndex % 10;
        board1.set(gopherIndex, "G");
        board2.set(gopherIndex, "G");
    }

    /* This method cleans up the board for resetting. */
    public static void cleanUp() {

        gopherFound = false;

        board1.clear();
        board2.clear();
        list1.clear();
        list2.clear();

        /* Setting up default board. */
        for(int i = 0; i < 100; i++) {
            board1.add("-");
            board2.add("-");
        }

        /* Setting up gopher related info. */
        gopherIndex = new Random().nextInt(100);
        gopherIndexRow = gopherIndex / 10;
        gopherIndexCol = gopherIndex % 10;
        board1.set(gopherIndex, "G");
        board2.set(gopherIndex, "G");

        p1GridAdapter.notifyDataSetChanged();
        p2GridAdapter.notifyDataSetChanged();
        p1ListAdapter.notifyDataSetChanged();
        p2ListAdapter.notifyDataSetChanged();

    }


    /* This is our AsyncTask class which does the bulk of the program's work. */
    public static class GopherFinder extends AsyncTask<String, Integer, Boolean> {

        boolean player1 = false;
        boolean player2 = false;
        boolean threadFoundGopher = false;


        protected void onPreExecute() {
            synchronized (GameFragment.winnerMessage) {
                GameFragment.winnerMessage.setText("Status: Playing game...");
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            if(strings[0].equals("P1")) { player1 = true; }
            else { player2 = true; }

            boolean firstGuess = true;
            int guessToMake = -1;
            int indexIteration = 0;

            try {

                if(player1) {

                    /* This loop will iterate through until the gopher is found. */
                    while (!GameFragment.gopherFound) {

                        /* First guess should be random. */
                        if(firstGuess) {
                            guessToMake = new Random().nextInt(100);
                            firstGuess = false;
                        }

                        /* Following guesses should be smart guesses based on the last guess's feedback. */
                        else {

                            /* Index value for helping keep track of last guess's feedback, & other housekeeping. */
                            Integer lastIndexIteration = indexIteration - 1;
                            String lastGuessIndexString = lastIndexIteration.toString();
                            int i = 0;

                            /* Case of a prior complete miss, guess a random spot again. */
                            if(list1.get(lastIndexIteration).equals(lastIndexIteration + ". " + "Complete miss"))
                                guessToMake = new Random().nextInt(100);

                                /* Case of a prior close guess. */
                            else if (list1.get(lastIndexIteration).equals(lastIndexIteration + ". " + "Close guess")) {

                                /* We will iterate through the possible full range of motion based on the prev close guess until we find the gopher or a near miss. */
                                /* This will always result in either the gopher found or a near miss found for this case. */
                                while(!GameFragment.gopherFound && !list1.get(lastIndexIteration).equals(lastIndexIteration + ". " + "Near miss") && i < possibleClosePos.size()) {
                                    guessToMake = possibleClosePos.get(i) + board1.indexOf(lastGuessIndexString);

                                    /* If the guessed move is within bounds of the board. */
                                    if(guessToMake > -1 && guessToMake < 100) {
                                        publishProgress(guessToMake, indexIteration);
                                        Thread.sleep(2000);

                                        /* If we have successfully made a move on an empty space. */
                                        if(indexIteration < list1.size()) {
                                            indexIteration++;
                                            lastIndexIteration++;
                                        }
                                    }

                                    i++;
                                }
                            }

                            /* Case of a prior near miss. */
                            else if (list1.get(lastIndexIteration).equals(lastIndexIteration + ". " + "Near miss")) {

                                /* We will iterate through the possible full range of motion based on the prev close guess until we find the gopher. */
                                /* This will always result in the gopher found. */
                                while(!GameFragment.gopherFound && i < possibleNearPos.size()) {

                                    guessToMake = possibleNearPos.get(i) + board1.indexOf(lastGuessIndexString);

                                    /* If the guessed move is within bounds of the board. */
                                    if(guessToMake > -1 && guessToMake < 100) {
                                        publishProgress(guessToMake, indexIteration);
                                        Thread.sleep(2000);

                                        /* If we have successfully made a move on an empty space. */
                                        if(indexIteration < list1.size()) {
                                            indexIteration++;
                                            lastIndexIteration++;
                                        }
                                    }

                                    i++;
                                }
                            }

                            /* There will be an error if we have reached here. */
                            else { System.out.println(indexIteration + ". ERROR: Occurred in while loop."); }
                        }

                        /* At this point, we will for sure have a guess to make. */
                        /* This case deals with positions which have already been guessed. */
                        if (!GameFragment.board1.get(guessToMake).equals("G") && !GameFragment.board1.get(guessToMake).equals("-")) {
                            indexIteration--;
                        }

                        /* If we make it here, we have a guess to make. */
                        else {
                            if(!GameFragment.gopherFound) {
                                publishProgress(guessToMake, indexIteration);
                                Thread.sleep(2000);
                            }
                        }

                        indexIteration++;
                    }
                }

                else if(player2) {

                    /* This loop will iterate through until the gopher is found. */
                    while (!GameFragment.gopherFound) {

                        /* First guess should be random. */
                        if(firstGuess) {
                            guessToMake = new Random().nextInt(100);
                            firstGuess = false;
                        }

                        /* Following guesses should be smart guesses based on the last guess's feedback. */
                        else {

                            /* Index value for helping keep track of last guess's feedback, & other housekeeping. */
                            Integer lastIndexIteration = indexIteration - 1;
                            String lastGuessIndexString = lastIndexIteration.toString();
                            int i = 0;

                            /* Case of a prior complete miss, guess a random spot again. */
                            if(list2.get(lastIndexIteration).equals(lastIndexIteration + ". " + "Complete miss"))
                                guessToMake = new Random().nextInt(100);

                                /* Case of a prior close guess. */
                            else if (list2.get(lastIndexIteration).equals(lastIndexIteration + ". " + "Close guess")) {

                                /* We will iterate through the possible full range of motion based on the prev close guess until we find the gopher or a near miss. */
                                /* This will always result in either the gopher found or a near miss found for this case. */
                                while(!GameFragment.gopherFound && !list2.get(lastIndexIteration).equals(lastIndexIteration + ". " + "Near miss") && i < possibleClosePos.size()) {
                                    guessToMake = possibleClosePos.get(i) + board2.indexOf(lastGuessIndexString);

                                    /* If the guessed move is within bounds of the board. */
                                    if(guessToMake > -1 && guessToMake < 100) {
                                        publishProgress(guessToMake, indexIteration);
                                        Thread.sleep(2000);

                                        /* If we have successfully made a move on an empty space. */
                                        if(indexIteration < list2.size()) {
                                            indexIteration++;
                                            lastIndexIteration++;
                                        }
                                    }

                                    i++;
                                }
                            }

                            /* Case of a prior near miss. */
                            else if (list2.get(lastIndexIteration).equals(lastIndexIteration + ". " + "Near miss")) {

                                /* We will iterate through the possible full range of motion based on the prev close guess until we find the gopher. */
                                /* This will always result in the gopher found. */
                                while(!GameFragment.gopherFound && i < possibleNearPos.size()) {

                                    guessToMake = possibleNearPos.get(i) + board2.indexOf(lastGuessIndexString);

                                    /* If the guessed move is within bounds of the board. */
                                    if(guessToMake > -1 && guessToMake < 100) {
                                        publishProgress(guessToMake, indexIteration);
                                        Thread.sleep(2000);

                                        /* If we have successfully made a move on an empty space. */
                                        if(indexIteration < list2.size()) {
                                            indexIteration++;
                                            lastIndexIteration++;
                                        }
                                    }

                                    i++;
                                }
                            }

                            /* There will be an error if we have reached here. */
                            else { System.out.println(indexIteration + ". ERROR: Occurred in while loop."); }
                        }

                        /* At this point, we will for sure have a guess to make. */
                        /* This case deals with positions which have already been guessed. */
                        if (!GameFragment.board2.get(guessToMake).equals("G") && !GameFragment.board2.get(guessToMake).equals("-")) {
                            indexIteration--;
                        }

                        /* If we make it here, we have a guess to make. */
                        else {
                            if(!GameFragment.gopherFound) {
                                publishProgress(guessToMake, indexIteration);
                                Thread.sleep(2000);
                            }
                        }

                        indexIteration++;
                    }
                }



            } catch (Exception e) { System.out.println("Could not complete action."); }

            return GameFragment.gopherFound;
        }

        @Override
        protected void onProgressUpdate(Integer... integers) {
            int indexGuessed = integers[0];
            String iterationNumber = integers[1].toString();

            if(player1) {

                /* We guess and correctly find the gopher. */
                if (GameFragment.board1.get(indexGuessed).equals("G")) {
                    board1.set(indexGuessed, iterationNumber);
                    list1.add(iterationNumber + ". " + feedbackList.get(0));
                    synchronized (GameFragment.gopherFound) { GameFragment.gopherFound = true; threadFoundGopher = true; }
                }

                /* We guess on an empty game piece. */
                else if (GameFragment.board1.get(indexGuessed).equals("-")) {
                    board1.set(indexGuessed, iterationNumber);
                    Integer indexGuessedRow = indexGuessed / 10;
                    Integer indexGuessedCol = indexGuessed % 10;

                    /* Boundary checks for Close guess and Near miss cases. */
                    if(indexGuessedRow > gopherIndexRow - 3 && indexGuessedRow < gopherIndexRow + 3 && indexGuessedCol > gopherIndexCol - 3 && indexGuessedCol < gopherIndexCol + 3) {

                        /* Case for a Near miss. */
                        if(indexGuessedRow > gopherIndexRow - 2 && indexGuessedRow < gopherIndexRow + 2 && indexGuessedCol > gopherIndexCol - 2 && indexGuessedCol < gopherIndexCol + 2)
                            list1.add(iterationNumber + ". " + feedbackList.get(1));

                            /* Case for a Close guess. */
                        else { list1.add(iterationNumber + ". " + feedbackList.get(2)); }
                    }

                    /* Case for a Complete miss. */
                    else { list1.add(iterationNumber + ". " + feedbackList.get(3)); }
                }

                /* We guess on a game piece we've already guessed on. (May need taking care of). */
                else { System.out.println("Iteration " + iterationNumber + ". Ran into previous guess (" + board1.get(indexGuessed) + ") at index: " + indexGuessed); }

                /* Updating the ArrayAdapter for the ListView with new data from our feedback list. */
                p1GridAdapter.notifyDataSetChanged();
                p1ListAdapter.notifyDataSetChanged();
            }

            else if(player2) {

                /* We guess and correctly find the gopher. */
                if (GameFragment.board2.get(indexGuessed).equals("G")) {
                    board2.set(indexGuessed, iterationNumber);
                    list2.add(iterationNumber + ". " + feedbackList.get(0));
                    synchronized (GameFragment.gopherFound) { GameFragment.gopherFound = true; threadFoundGopher = true; }
                }

                /* We guess on an empty game piece. */
                else if (GameFragment.board2.get(indexGuessed).equals("-")) {
                    board2.set(indexGuessed, iterationNumber);
                    Integer indexGuessedRow = indexGuessed / 10;
                    Integer indexGuessedCol = indexGuessed % 10;

                    /* Boundary checks for Close guess and Near miss cases. */
                    if(indexGuessedRow > gopherIndexRow - 3 && indexGuessedRow < gopherIndexRow + 3 && indexGuessedCol > gopherIndexCol - 3 && indexGuessedCol < gopherIndexCol + 3) {

                        /* Case for a Near miss. */
                        if(indexGuessedRow > gopherIndexRow - 2 && indexGuessedRow < gopherIndexRow + 2 && indexGuessedCol > gopherIndexCol - 2 && indexGuessedCol < gopherIndexCol + 2)
                            list2.add(iterationNumber + ". " + feedbackList.get(1));

                            /* Case for a Close guess. */
                        else { list2.add(iterationNumber + ". " + feedbackList.get(2)); }
                    }

                    /* Case for a Complete miss. */
                    else { list2.add(iterationNumber + ". " + feedbackList.get(3)); }
                }

                /* We guess on a game piece we've already guessed on. (May need taking care of). */
                else { System.out.println("Iteration " + iterationNumber + ". Ran into previous guess (" + board2.get(indexGuessed) + ") at index: " + indexGuessed); }

                /* Updating the ArrayAdapter for the ListView with new data from our feedback list. */
                p2GridAdapter.notifyDataSetChanged();
                p2ListAdapter.notifyDataSetChanged();
            }
        }

        protected void onPostExecute(Boolean isGopherFound) {
            if(GameFragment.gopherFound) {
                synchronized (GameFragment.winnerMessage) {
                    if(player1 && threadFoundGopher) {
                        GameFragment.winnerMessage.setText("Winner: Player 1!");
                        GameFragment.winnerMessage.setTextColor(Color.rgb(61, 220, 132));
                        MainFragment.theStopButton.setEnabled(false);
                        MainFragment.theStartButton.setEnabled(false);
                        MainFragment.theResetButton.setEnabled(true);
                    }
                    else if(player2 && threadFoundGopher){
                        GameFragment.winnerMessage.setText("Winner: Player 2!");
                        GameFragment.winnerMessage.setTextColor(Color.rgb(61, 220, 132));
                        MainFragment.theStopButton.setEnabled(false);
                        MainFragment.theStartButton.setEnabled(false);
                        MainFragment.theResetButton.setEnabled(true);
                    }
                }

            }
        }

    }



}
