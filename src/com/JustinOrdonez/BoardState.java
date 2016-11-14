package com.JustinOrdonez;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Justin Ordonez on 11/13/2016.
 */
public class BoardState implements Comparator<BoardState> {
    static int queenAmt = 19;
    private int[] stateArray = new int[queenAmt];
    protected int attackingQueenAmt;

    BoardState(){
        generateBoard();
        generateAttackingQueenAmt();
    }

    BoardState(BoardState boardState){
        cloneBoard(boardState);
    }

    private void generateBoard(){
        Random rand = new Random();

        for(int i = 0; i < queenAmt; i++) {
            stateArray[i] = rand.nextInt(queenAmt);
        }
    }

    private void generateAttackingQueenAmt(){
        attackingQueenAmt = 0;

        for(int i=0; i < stateArray.length - 1; i++){
            for(int j = i + 1; j < stateArray.length; j++){
                if(stateArray[j] == stateArray[i]
                        || stateArray[j] == stateArray[i] - (j-i)
                        || stateArray[j] == stateArray[i] + (j-i)){
                    attackingQueenAmt++;
                }
            }
        }
    }

    public void printBoardStateArray(){
        System.out.println("Board State Array: ");

        for(int x: stateArray){
            System.out.print(x + " ");
        }
        System.out.println();
    }

    public void printBoard(){
        System.out.println("Game Board: ");

        for(int i = 0; i < stateArray.length; i++){
            for(int j = 0; j < stateArray.length; j++){
                if(stateArray[i] != j && j % 2 == 1){
                    System.out.print("|-| ");
                } else if(stateArray[i] != j && j % 2 == 0){
                    System.out.print(" -  ");
                } else {
                    System.out.print(" Q  ");
                }
            }
            System.out.println();
        }
    }

    public void cloneBoard(BoardState boardState){
        for(int i=0; i < stateArray.length; i++){
            stateArray[i] = boardState.getStateArrayElement(i);
        }

        attackingQueenAmt = boardState.attackingQueenAmt;
    }

//    static int[] cloneArray(int[] array){
//        int[] clone = new int[array.length];
//
//        for(int i=0; i < array.length; i++){
//            clone[i] = array[i];
//        }
//
//        return clone;
//    }

    public void setStateArrayElement(int index, int value){
        stateArray[index] = value;
        generateAttackingQueenAmt();
    }

    public int getStateArrayElement(int index){
        return stateArray[index];
    }

    public int[] getStateArray(){
        return stateArray;
    }

    public static BoardState stateCrossoverCreateChild(BoardState state1, BoardState state2, int crossOverAmt){
        BoardState child = new BoardState(state1);

        for(int i=0; i < crossOverAmt; i++){
            child.setStateArrayElement(i,state2.getStateArray()[i]);
        }

        return child;
    }

    @Override
    public int compare(BoardState o1, BoardState o2) {
        return o1.attackingQueenAmt - o2.attackingQueenAmt;
    }
}
