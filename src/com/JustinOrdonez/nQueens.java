package com.JustinOrdonez;

import java.util.*;


public class nQueens {
    static int queenAmt = 19;
    static double searchCost = 0;

    public static void main(String[] args) {
        BoardState.queenAmt = queenAmt;
        menu();
    }

    static void menu(){
        Scanner kb = new Scanner(System.in);

        System.out.println("Hello user, welcome to this N-Queens randomly generated problem solver." +
                "\nWhat would you like to do?" +
                "\n1) Solve a Randomly Generated N-Queens board with Steepest Hill-Climbing Algorithm." +
                "\n2) Solve a Randomly Generated N-Queens board with a Genetic Algorithm." +
                "\n3) 101 Cases Steepest Hill-Climbing Algorithm Analysis." +
                "\n4) 101 Cases Genetic Algorithm Analysis.");
        String input = kb.nextLine();

        switch(input){
            case "1":
                hillClimbHandler();
                break;
            case "2":
                geneticAlgorithmHandler();
                break;
            case "3":
                hillClimb101CaseHandler();
                break;
            case "4":
                geneticAlgorithm101CaseHandler();
                break;
            default:
                System.out.println("Invalid Input.");
                menu();
        }
    }

    static void hillClimb101CaseHandler(){
        double solvedCases = 0;
        long startTime, endTime;
        long avgExecTime = 0;

        for(int i=0; i < 101 ; i++){
            startTime = System.currentTimeMillis();
            if(hillClimbHandler()){
                solvedCases++;
            }
            endTime = System.currentTimeMillis();
            avgExecTime += endTime - startTime;
        }


        avgExecTime = avgExecTime/101;
        System.out.println("Solved Case Percentage: " + (solvedCases/101)*100 + "%" +
                "\nAverage Search Cost: " + searchCost/101 +
                "\nAverage Execution Time: " + avgExecTime + "ms");
    }

    static boolean hillClimbHandler(){
        BoardState randomBoard = new BoardState();
        randomBoard.printBoardStateArray();
        randomBoard.printBoard();
        System.out.println("Starting amount of vulnerable queens: " + randomBoard.attackingQueenAmt + "\n");
        return hillClimb(randomBoard);
    }

    static boolean hillClimb(BoardState currentState){
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

            searchCost++;
        }while(!Arrays.equals(candidateBoard.getStateArray(), currentState.getStateArray()));

        candidateBoard.printBoardStateArray();
        System.out.println("Resulting amount of vulnerable queens: " + candidateBoard.attackingQueenAmt);
        candidateBoard.printBoard();

        if(candidateBoard.attackingQueenAmt == 0){
            return true;
        } else{
            return false;
        }
    }

    static void geneticAlgorithm101CaseHandler(){
        double solvedCases = 0;
        long startTime, endTime;
        long avgExecTime = 0;

        for(int i=0; i < 101 ; i++){
            startTime = System.currentTimeMillis();
            if(geneticAlgorithm(16,16,1)){
                solvedCases++;
            }
            endTime = System.currentTimeMillis();
            avgExecTime += endTime - startTime;
        }

        avgExecTime = avgExecTime/101;
        System.out.println("Solved Case Percentage: " + (solvedCases/101)*100 + "%" +
                "\nAverage Search Cost: " + searchCost/101 +
                "\nAverage Execution Time: " + avgExecTime + "ms");
    }

    static boolean geneticAlgorithmHandler(){
        Scanner kb = new Scanner(System.in);

        System.out.println("How many randomly generated states should we start with?: ");
        int startingStateAmt = kb.nextInt();

        System.out.println("How many states should be selected from the initial group? (Only positive and even ints): ");
        int selectionAmt = kb.nextInt();

        System.out.println("How many digits should we crossover between each pair?: ");
        int crossOverAmt = kb.nextInt();

        if(startingStateAmt > 0
                && selectionAmt % 2 == 0
                && selectionAmt <= startingStateAmt
                && crossOverAmt < queenAmt){
            return geneticAlgorithm(startingStateAmt, selectionAmt, crossOverAmt);
        }else{
            invalidInput();
            return false;
        }
    }

    static boolean geneticAlgorithm(int startingStateAmt, int selectionAmt, int crossOverAmt){
        BoardState[] boardStates = new BoardState[selectionAmt];
        Comparator<BoardState> boardStateComparator = new BoardState();
        PriorityQueue<BoardState> pQ = new PriorityQueue<BoardState>(19, boardStateComparator);
        ArrayList<BoardState> aList = new ArrayList<BoardState>();
        Random rand = new Random();
        BoardState result;
        int rounds=0;
        int roundLimit = 100000;

        //Initial Population
        for(int i=0; i < startingStateAmt; i++){
            pQ.add(new BoardState());
        }

        while(pQ.peek().attackingQueenAmt != 0 && rounds < roundLimit){
            //Selection
            for(int i=0; i < selectionAmt; i++){
                boardStates[i] = pQ.poll();
                pQ.add(boardStates[i]);
            }

            //Cross-Over and Mutation
            for(int i=0; i < boardStates.length; i += 2){
                BoardState child1 = BoardState.stateCrossoverCreateChild(boardStates[i],boardStates[i+1], crossOverAmt);
                BoardState child2 = BoardState.stateCrossoverCreateChild(boardStates[i+1],boardStates[i], crossOverAmt);

                child1.setStateArrayElement(rand.nextInt(BoardState.queenAmt), rand.nextInt(BoardState.queenAmt));
                child2.setStateArrayElement(rand.nextInt(BoardState.queenAmt), rand.nextInt(BoardState.queenAmt));

                pQ.add(child1);
                pQ.add(child2);

                while(pQ.peek().attackingQueenAmt > boardStates[i].attackingQueenAmt){
                    pQ.remove();
                }
            }

            searchCost++;
            rounds++;
        }

        result = pQ.poll();

        System.out.println("Vulnerable Queens: " + result.attackingQueenAmt);
        result.printBoardStateArray();
        result.printBoard();

        if(result.attackingQueenAmt == 0){
            return true;
        } else{
            return false;
        }
    }

    static void invalidInput(){
        System.out.println("Invalid input. Come back when you're willing to follow directions.");
        System.exit(0);
    }
}
