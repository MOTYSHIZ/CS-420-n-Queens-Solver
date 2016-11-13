package com.JustinOrdonez;

import java.util.Arrays;
import java.util.Random;


public class nQueens {
    static final int QUEENAMT = 19;
    static boolean[][] currentBoard = new boolean[QUEENAMT][QUEENAMT];


    public static void main(String[] args) {
	    printBoardStateArray(getStateArray(generateBoard()));
        printBoard(currentBoard);
        getAttackingQueenAmt(getStateArray(currentBoard));
        hillClimb();

    }

    static boolean[][] generateBoard(){
        Random rand = new Random();

        for(int i = 0; i < QUEENAMT; i++) {
            currentBoard[i][rand.nextInt(QUEENAMT)] = true;
        }

        return currentBoard;
    }

    static int getAttackingQueenAmt(int[] stateArray){
        int attackingQueenAmt = 0;

        for(int i=0; i < stateArray.length - 1; i++){
            for(int j = i + 1; j < stateArray.length; j++){
                if(stateArray[j] == stateArray[i]
                        || stateArray[j] == stateArray[i] - (j-i)
                        || stateArray[j] == stateArray[i] + (j-i)){
                    attackingQueenAmt++;
                }
            }
        }
        return attackingQueenAmt;
    }

    static void hillClimb(){
        int[] currentStateArray = getStateArray(currentBoard);
        int maxNetDecrease = 0;
        int[] candidateArray = currentStateArray;

        do {
            currentStateArray = cloneArray(candidateArray);
            maxNetDecrease = 0;
            for(int i=0;i < currentStateArray.length;i++){
                int[] exploringArray = cloneArray(currentStateArray);

                for(int j = 0; j < currentStateArray.length; j++){
                    int currentAttackingQueenAmt = getAttackingQueenAmt(currentStateArray);

                    if(j != currentStateArray[i]){
                        exploringArray[i] = j;
                        int exploringAttackingQueenAmt = getAttackingQueenAmt(exploringArray);
                        int netDecrease = currentAttackingQueenAmt - exploringAttackingQueenAmt;

                        if(netDecrease > maxNetDecrease){
                            maxNetDecrease = netDecrease;
                            candidateArray = cloneArray(exploringArray);
                        }

                    }
                }
            }
        }while(!Arrays.equals(candidateArray, currentStateArray));

        printBoardStateArray(candidateArray);
        System.out.println("Vulnerable Queens: " + getAttackingQueenAmt(candidateArray));
        printBoard(candidateArray);
    }

    static void printBoard(boolean[][] boardState){
        for(int i = 0; i < boardState.length; i++){
            for(int j = 0; j < boardState[i].length; j++){
                if(boardState[i][j] != true && j % 2 == 1){
                    System.out.print("|-| ");
                } else if(boardState[i][j] != true && j % 2 == 0){
                    System.out.print(" -  ");
                } else {
                    System.out.print(" Q  ");
                }
            }
            System.out.println();
        }
    }

    static void printBoard(int[] boardState){
        for(int i = 0; i < boardState.length; i++){
            for(int j = 0; j < boardState.length; j++){
                if(boardState[i] != j && j % 2 == 1){
                    System.out.print("|-| ");
                } else if(boardState[i] != j && j % 2 == 0){
                    System.out.print(" -  ");
                } else {
                    System.out.print(" Q  ");
                }
            }
            System.out.println();
        }
    }

    static void printBoardStateArray(int[] boardState){
        for(int x: boardState){
            System.out.print(x + " ");
        }
        System.out.println();
    }

    static int[] getStateArray(boolean[][] boardState){
        int[] stateArray = new int[boardState.length];

        for(int i = 0; i < boardState.length; i++){
            for(int j = 0; j < boardState[i].length; j++){
                if(boardState[i][j] == true){
                    stateArray[i] = j;
                }
            }
        }
        return stateArray;
    }

    static int[] cloneArray(int[] array){
        int[] clone = new int[array.length];

        for(int i=0; i < array.length; i++){
            clone[i] = array[i];
        }

        return clone;
    }
}
