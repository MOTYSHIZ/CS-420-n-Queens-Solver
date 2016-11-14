package com.JustinOrdonez;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Comparator;


public class nQueens {
    static int queenAmt = 19;

    public static void main(String[] args) {
        menu();
    }

    static void menu(){
        Scanner kb = new Scanner(System.in);

        System.out.println("Hello user, welcome to this N-Queens randomly generated problem solver." +
                "\nWhat would you like to do?" +
                "\n1) Solve a Randomly Generated N-Queens board with Steepest Hill-Climbing Algorithm. " +
                "\n2) Solve a Randomly Generated N-Queens board with a genetic algorithm.");
        String input = kb.nextLine();

        switch(input){
            case "1":
                hillClimbHandler();
                break;
            case "2":
                geneticAlgorithmHandler();
                break;
            default:
                System.out.println("Invalid Input.");
                menu();
        }
    }

    static int[] generateBoard(){
        Random rand = new Random();
        int[] board = new int[queenAmt];

        for(int i = 0; i < queenAmt; i++) {
            board[i] = rand.nextInt(queenAmt);
        }

        return board;
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

    static void hillClimbHandler(){
        BoardState randomBoard = new BoardState();
        randomBoard.printBoardStateArray();
        randomBoard.printBoard();
        System.out.println("Starting amount of vulnerable queens: " + randomBoard.attackingQueenAmt + "\n");
        hillClimb(randomBoard);
    }

    static void hillClimb(BoardState currentState){
        int maxNetDecrease;
        BoardState candidateBoard = new BoardState(currentState);

        do {
            currentState.cloneBoard(candidateBoard);
            maxNetDecrease = 0;

            for(int i=0;i < currentState.getStateArray().length;i++){
                BoardState exploringBoard = new BoardState(currentState);

                for(int j = 0; j < currentState.getStateArray().length; j++){
                    if(j != currentState.getStateArrayElement(i)){
                        exploringBoard.setStateArrayElement(i,j);
                        int netDecrease = currentState.attackingQueenAmt - exploringBoard.attackingQueenAmt;

                        if(netDecrease > maxNetDecrease){
                            maxNetDecrease = netDecrease;
                            candidateBoard.cloneBoard(exploringBoard);
                        }
                    }
                }
            }

        }while(!Arrays.equals(candidateBoard.getStateArray(), currentState.getStateArray()));

        candidateBoard.printBoardStateArray();
        System.out.println("Resulting amount of vulnerable queens: " + candidateBoard.attackingQueenAmt);
        //candidateBoard.printBoard();
    }

    static void geneticAlgorithmHandler(){
        Scanner kb = new Scanner(System.in);

        System.out.println("How many randomly generated states should we start with? (Only positive, even ints): ");
        int startingStateAmt = kb.nextInt();

        System.out.println("How many states should be selected from the initial group?: ");
        int selectionAmt = kb.nextInt();

        System.out.println("How many digits should we crossover between each pair?: ");
        int crossOverAmt = kb.nextInt();

        if(startingStateAmt % 2 == 0 && startingStateAmt > 0
                && crossOverAmt < queenAmt
                && selectionAmt < startingStateAmt){
            geneticAlgorithm(startingStateAmt, selectionAmt, crossOverAmt);
        }else{
            invalidInput();
        }


    }

    static void geneticAlgorithm(int startingStateAmt, int selectionAmt, int crossOverAmt){
        BoardState[] boardStates = new BoardState[startingStateAmt];
        Comparator<BoardState> boardStateComparator = new BoardState();
        PriorityQueue pQ = new PriorityQueue(19, boardStateComparator);

        for(int i=0; i < boardStates.length; i++){
            boardStates[i] = new BoardState();
            boardStates[i].printBoardStateArray();
            pQ.add(boardStates[i]);
        }

//        for(int i=0; i < boardStates.length; i += 2){
//            stateCrossover(boardStates[i], boardStates[i+1], crossOverAmt);
//        }

    }

    static void stateCrossover(int[] state1, int[] state2, int crossOverAmt){
        int[] temp = new int[state1.length];

        for(int i=0; i < crossOverAmt; i++){
            temp[i] = state1[i];
            state1[i] = state2[i];
            state2[i] = temp[i];
        }
    }

//    static void printBoard(boolean[][] boardState){
//        for(int i = 0; i < boardState.length; i++){
//            for(int j = 0; j < boardState[i].length; j++){
//                if(boardState[i][j] != true && j % 2 == 1){
//                    System.out.print("|-| ");
//                } else if(boardState[i][j] != true && j % 2 == 0){
//                    System.out.print(" -  ");
//                } else {
//                    System.out.print(" Q  ");
//                }
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }






    static void invalidInput(){
        System.out.println("Invalid input. Come back when you're willing to follow directions.");
        System.exit(0);
    }
}
