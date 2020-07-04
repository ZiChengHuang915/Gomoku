import java.awt.Color;
import java.io.*;

public class Gomoku {	
   GomokuGUI gui;
   final int NUMROW;  
   final int NUMCOL;
   final int INAROW = 5;
   
   final int EMPTY = -127;
   final int BLACK = 0;
   final int WHITE = 1;
   
   final int DRAW = -2;
   
   byte board[][];
   byte player = BLACK;
   int gameMode = 1; // 1 for human vs ai, 2 for human vs human
   int bestMoveRow, bestMoveCol, lastMoveRow, lastMoveCol;
   
   int aiDepth = 4;
   long timeOutMS = 1000000;
   
   public Gomoku (GomokuGUI gui) {
      this.gui = gui;
      NUMROW = gui.NUMROW;
      NUMCOL = gui.NUMCOL;
      board = new byte[NUMROW][NUMCOL];      
      resetGame();
      bestMoveRow = -1;
      bestMoveCol = -1;
      lastMoveRow = -1;
      lastMoveCol = -1;
   }
   
   public void resetGame () {
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            board[i][j] = EMPTY;
            gui.setPiece(i, j, 2);
         }
      }
      player = BLACK;
      placePiece(7, 7, (byte)BLACK);
      
      // placePiece(6, 6, player);
   //       placePiece(6, 7, player);
   //       placePiece(8, 7, player);
   //       placePiece(7, 6, player);
   //       placePiece(7, 8, player);
   //       placePiece(5, 8, player);
   //       placePiece(8, 5, player);
   //       placePiece(7, 5, player);
   //       placePiece(6, 9, player);
   //       placePiece(4, 9, player);
   //       placePiece(3, 10, player);
   //       placePiece(7, 4, player);
   //       placePiece(7, 3, player);
   //       placePiece(9, 6, player);
   //       placePiece(5, 10, player);
   //       placePiece(4, 11, player);
   //       placePiece(4, 10, player);
   //       placePiece(2, 10, player);
   //       placePiece(6, 10, player);
   //       placePiece(7, 10, player);
   //       placePiece(6, 12, player);
   //       placePiece(6, 11, player);
   //       placePiece(5, 11, player);
   //       placePiece(3, 9, player);
      //placePiece(8, 8, player);
   }
   
   public void takeTurn() {
      if (player == BLACK) {
         player = WHITE;
      } else {
         player = BLACK;
      }
   }
   
   public void placePiece(int row, int column, byte player) {
      System.out.println(player + " plays " + row + " " + column);
      board[row][column] = player;
      gui.setPiece(row, column, player);
      lastMoveRow = row;
      lastMoveCol = column;
      takeTurn();
   }
   
   public int getSumHorizontal(byte checkBoard[][], int sumsLeft, int row, int column) {
      if (sumsLeft == 1) {
         return checkBoard[row][column];
      }
      
      return checkBoard[row][column] + getSumHorizontal(checkBoard, sumsLeft - 1, row, column + 1);
   }
   
   public int getSumVertical(byte checkBoard[][], int sumsLeft, int row, int column) {
      if (sumsLeft == 1) {
         return checkBoard[row][column];
      }
      
      return checkBoard[row][column] + getSumVertical(checkBoard, sumsLeft - 1, row + 1, column);
   }
   
   public int getSumDownRight(byte checkBoard[][], int sumsLeft, int row, int column) {
      if (sumsLeft == 1) {
         return checkBoard[row][column];
      }
      
      return checkBoard[row][column] + getSumDownRight(checkBoard, sumsLeft - 1, row + 1, column + 1);
   }
   
   public int getSumUpRight(byte checkBoard[][], int sumsLeft, int row, int column) {
      if (sumsLeft == 1) {
         return checkBoard[row][column];
      }
      
      return checkBoard[row][column] + getSumUpRight(checkBoard, sumsLeft - 1, row - 1, column + 1);
   }
      
   // returns BLACK, WHITE, DRAW, EMPTY
   public int getWinner(byte checkBoard[][]) {
      int sum = 0;    
      
      for (int i = 0; i < NUMROW; i++) { // horizontal going right
         for (int j = 0; j < NUMCOL - INAROW + 1; j++) {
            sum = getSumHorizontal(checkBoard, INAROW, i, j);
            if (sum == BLACK * INAROW) { // black win
               return BLACK;
            } else if (sum == WHITE * INAROW) { // white win
               return WHITE;
            }
         }
      }
      
      for (int i = 0; i < NUMROW - INAROW + 1; i++) { // vertical going down
         for (int j = 0; j < NUMCOL; j++) {
            sum = getSumVertical(checkBoard, INAROW, i, j);
            if (sum == BLACK * INAROW) { // black win
               return BLACK;
            } else if (sum == WHITE * INAROW) { // white win
               return WHITE;
            }
         }
      } 
      
      for (int i = 0; i < NUMROW - INAROW + 1; i++) { // diagonal top left to bottom right
         for (int j = 0; j < NUMCOL - INAROW + 1; j++) {
            sum = getSumDownRight(checkBoard, INAROW, i, j);
            if (sum == BLACK * INAROW) { // black win
               return BLACK;
            } else if (sum == WHITE * INAROW) { // white win
               return WHITE;
            }
         }
      }
      
      for (int i = INAROW - 1; i < NUMROW; i++) { // diagonal bottom left to top right
         for (int j = 0; j < NUMCOL - INAROW + 1; j++) {
            sum = getSumUpRight(checkBoard, INAROW, i, j);
            if (sum == BLACK * INAROW) { // black win
               return BLACK;
            } else if (sum == WHITE * INAROW) { // white win
               return WHITE;
            }
         }
      }
      
      boolean hasPiece = true;
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            if (board[i][j] == EMPTY) {
               hasPiece = false;
            }
         }
      }
      if (hasPiece) {
         return DRAW; 
      }
      
      return EMPTY;
   }
   
   public boolean isValidMove(int row, int column) {
      if (board[row][column] == EMPTY) {
         return true;
      }
      
      return false;
   }
   
   public boolean besidesPlayedSquare(int row, int column, byte board[][]) { // perimeter check
      if (board[row][column] == EMPTY) {
         for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
               if (i >= 0 && i < NUMROW && j >= 0 && j < NUMCOL) {
                  if (board[i][j] != EMPTY) {
                     return true;
                  }
               }
            }
         }
      }
      
      return false;
   }
   
   public boolean adjacentLastPlayed(int row, int column, int playedRow, int playedCol) {
      if ((Math.abs(playedRow - row) <= 1) && (Math.abs(playedCol - column) <= 1)) {
         if ((playedRow == row) && (playedCol == column)) {
            return false;
         }
         
         return true;
      }
      
      return false;
   }
   
   /////////////////////////////////////////////////////////////////////////////
   public void play (int row, int column) throws InterruptedException {
      System.out.println();
      
      if (gameMode == 1) {
         if (isValidMove(row, column)) {
            placePiece(row, column, player);                
           
            if (getWinner(board) == BLACK) {
               gui.gameoverWinBlack();
               resetGame();
            } else if (getWinner(board) == WHITE) {
               gui.gameoverWinWhite();
               resetGame();
            } else if (getWinner(board) == DRAW) {
               gui.gameoverDraw();
               resetGame();
            } else {
               long startTime = System.nanoTime();
               System.out.println("\nThinking...");
               findBestMove(true, aiDepth); // ai/black is the maximizer
               placePiece(bestMoveRow, bestMoveCol, player);
               System.out.println("Time elapsed for thinking: " + ((System.nanoTime() - startTime)/1000000) + "ms");
               //System.out.println("board score: " + (getBoardScore(board, 4) + getBoardScore(board, 3) + getBoardScore(board, 2)));
            }      
         }    
      } else if (gameMode == 2) {
         if (isValidMove(row, column)) {
            placePiece(row, column, player);
         }
      } else if (gameMode == 3) {
         System.out.println("Thinking...");
         long startTime = System.nanoTime();
         if (player == BLACK) {
            findBestMove(true, aiDepth); // ai/black is the maximizer
         } else {
            findBestMove(false, aiDepth); // ai/white is the minimizer
         }
         placePiece(bestMoveRow, bestMoveCol, player);
         System.out.println("Time elapsed for thinking: " + ((System.nanoTime() - startTime)/1000000) + "ms");
         System.out.println("board score: " + (getBoardScore(board, 4) + getBoardScore(board, 3) + getBoardScore(board, 2)));
      }
      
      if (getWinner(board) == BLACK) {
         gui.gameoverWinBlack();
         resetGame();
      } else if (getWinner(board) == WHITE) {
         gui.gameoverWinWhite();
         resetGame();
      } else if (getWinner(board) == DRAW) {
         gui.gameoverDraw();
         resetGame();
      }  
   }
   ///////////////////////////////////////////////////////////////////////////// 
   
   public int getBoardScore(byte board[][], int inARow) {
      int sum;
      int totalSum = 0;
           
      for (int i = 0; i < NUMROW; i++) { // horizontal going right
         for (int j = 0; j < NUMCOL - inARow + 1; j++) {
            sum = getSumHorizontal(board, inARow, i, j);
            if (sum == BLACK * inARow) { // black win
               totalSum += Math.pow(10, inARow);
            } else if (sum == WHITE * inARow) { // white win
               totalSum -= Math.pow(10, inARow);
            }
         }
      }
      
      for (int i = 0; i < NUMROW - INAROW + 1; i++) { // vertical going down
         for (int j = 0; j < NUMCOL; j++) {
            sum = getSumVertical(board, inARow, i, j);
            if (sum == BLACK * inARow) { // black win
               totalSum += Math.pow(10, inARow);
            } else if (sum == WHITE * inARow) { // white win
               totalSum -= Math.pow(10, inARow);
            }
         
         }
      } 
      
      for (int i = 0; i < NUMROW - INAROW + 1; i++) { // diagonal top left to bottom right
         for (int j = 0; j < NUMCOL - INAROW + 1; j++) {
            sum = getSumDownRight(board, inARow, i, j);
            if (sum == BLACK * inARow) { // black win
               totalSum += Math.pow(10, inARow);
            } else if (sum == WHITE * inARow) { // white win
               totalSum -= Math.pow(10, inARow);
            }
         }
      }
      
      for (int i = INAROW - 1; i < NUMROW; i++) { // diagonal bottom left to top right
         for (int j = 0; j < NUMCOL - INAROW + 1; j++) {
            sum = getSumUpRight(board, inARow, i, j);
            if (sum == BLACK * inARow) { // black win
               totalSum += Math.pow(10, inARow);
            } else if (sum == WHITE * inARow) { // white win
               totalSum -= Math.pow(10, inARow);
            }
         
         }
      }
      
      return (int) totalSum;
      
       // if (sum == BLACK * inARow) { // black win
   //                totalSum += Math.pow(10, inARow + 0.5);
   //             } else if (sum == WHITE * inARow) { // white win
   //                if (inARow == 4) {
   //                   totalSum -= Math.pow(10, inARow + 2);
   //                } else if (inARow == 3) {
   //                   totalSum -= Math.pow(10, inARow + 1);
   //                } else {
   //                   totalSum -= Math.pow(10, inARow);
   //                }
   //             }
   }
      
   public int minimax(byte boardWithVirtualMove[][], boolean isMaximizer, int depth, int alpha, int beta, int movePlayedRow, int movePlayedCol) {
      if (getWinner(boardWithVirtualMove) == BLACK) {
         return 2000000000;
      } else if (getWinner(boardWithVirtualMove) == WHITE) {
         return -2000000000;
      } else if (getWinner(boardWithVirtualMove) == -1) {
         return 0;
      }
      
      if (depth == 0) {
         int score = 0; 
         score += getBoardScore(boardWithVirtualMove, 4);
         score += getBoardScore(boardWithVirtualMove, 3);
         //score += getBoardScore(boardWithVirtualMove, 2);
         return score;
      } else {
      
         // byte tempBoard[][] = new byte[NUMROW][NUMCOL];
      //          for (int i = 0; i < NUMROW; i++) {
      //             for (int j = 0; j < NUMCOL; j++) {
      //                tempBoard[i][j] = boardWithVirtualMove[i][j];
      //             }
      //          }
      
         if (isMaximizer) {
            int bestValue = -1000000000;
            
            adjacentLoop:
            for (int i = movePlayedRow - 1; i <= movePlayedRow + 1; i++) {
               for (int j = movePlayedCol - 1; j <= movePlayedCol + 1; j++) {
                  if (i >= 0 && i < NUMROW && j >= 0 && j < NUMCOL) {
                     if (boardWithVirtualMove[i][j] == EMPTY) {
                        boardWithVirtualMove[i][j] = BLACK;
                        bestValue = Math.max(bestValue, minimax(boardWithVirtualMove, !isMaximizer, depth - 1, alpha, beta, i, j));  
                        boardWithVirtualMove[i][j] = EMPTY;
                        alpha = Math.max(alpha, bestValue);
                        if (beta <= alpha) {
                           break adjacentLoop;
                        }
                     }
                  }
               }
            }
            
            perimeterLoop:
            for (int i = 0; i < NUMROW; i++) {
               for (int j = 0; j < NUMCOL; j++) {
                  if (besidesPlayedSquare(i, j, boardWithVirtualMove) && !adjacentLastPlayed(i, j, movePlayedRow, movePlayedCol)) {            
                     boardWithVirtualMove[i][j] = BLACK;
                     bestValue = Math.max(bestValue, minimax(boardWithVirtualMove, !isMaximizer, depth - 1, alpha, beta, i, j));  
                     boardWithVirtualMove[i][j] = EMPTY;
                     alpha = Math.max(alpha, bestValue);
                     if (beta <= alpha) {
                        break perimeterLoop;
                     }           
                  }
               }
            }
         
            return bestValue;
         } else {
            int bestValue = 1000000000;
            
            adjacentLoop:
            for (int i = movePlayedRow - 1; i <= movePlayedRow + 1; i++) {
               for (int j = movePlayedCol - 1; j <= movePlayedCol + 1; j++) {
                  if (i >= 0 && i < NUMROW && j >= 0 && j < NUMCOL) {
                     if (boardWithVirtualMove[i][j] == EMPTY) {
                        boardWithVirtualMove[i][j] = WHITE;
                        bestValue = Math.min(bestValue, minimax(boardWithVirtualMove, !isMaximizer, depth - 1, alpha, beta, i, j)); 
                        boardWithVirtualMove[i][j] = EMPTY;                 
                        beta = Math.min(beta, bestValue);
                        if (beta <= alpha) {
                           break adjacentLoop;
                        }
                     }
                  }
               }
            }
            
            perimeterLoop:
            for (int i = 0; i < NUMROW; i++) {
               for (int j = 0; j < NUMCOL; j++) {
                  if (besidesPlayedSquare(i, j, boardWithVirtualMove) && !adjacentLastPlayed(i, j, movePlayedRow, movePlayedCol)) {             
                     boardWithVirtualMove[i][j] = WHITE;
                     bestValue = Math.min(bestValue, minimax(boardWithVirtualMove, !isMaximizer, depth - 1, alpha, beta, i, j)); 
                     boardWithVirtualMove[i][j] = EMPTY;                 
                     beta = Math.min(beta, bestValue);
                     if (beta <= alpha) {
                        break perimeterLoop;
                     }
                  }
               }
            }
         
            return bestValue;
         }     
      }
   }
   
   public void findBestMove(boolean isMax, int depth) {
      int bestMoveValueSoFar;
      long startTime = System.nanoTime();
      
      if (isMax) {
         bestMoveValueSoFar = -1000000001;
      } else {
         bestMoveValueSoFar = 1000000001;
      }
      
      byte tempBoard[][] = new byte[NUMROW][NUMCOL];
      for (int i = 0; i < NUMROW; i++) {
         for (int j = 0; j < NUMCOL; j++) {
            tempBoard[i][j] = board[i][j];
         }
      }
      if (isMax) {
         for (int i = lastMoveRow - 1; i <= lastMoveRow + 1; i++) {  //adjacent loop
            for (int j = lastMoveCol - 1; j <= lastMoveCol + 1; j++) {
               if (i >= 0 && i < NUMROW && j >= 0 && j < NUMCOL) {
                  //if (besidesPlayedSquare(i, j, tempBoard)) {
                  if (tempBoard[i][j] == EMPTY) {
                     tempBoard[i][j] = BLACK; // virtual move
                     int testValue = minimax(tempBoard, false, depth, -1000000000, 1000000000, i, j);
                     //System.out.println("considers " + i + " " + j + " with score " + testValue);
                     if (testValue > bestMoveValueSoFar) {
                        bestMoveValueSoFar = testValue; // the false is for is minimizer, because the maximizer has just played a virtual move
                        bestMoveRow = i;
                        bestMoveCol = j;
                     }
                     tempBoard[i][j] = EMPTY; // reset virtual move    
                  }
               }
            }
         }
         
         perimeterLoop:
         for (int i = 0; i < NUMROW; i++) {
            for (int j = 0; j < NUMCOL; j++) {
               if (besidesPlayedSquare(i, j, tempBoard) && !adjacentLastPlayed(i, j, lastMoveRow, lastMoveCol)) {            
               
                  tempBoard[i][j] = BLACK; // virtual move
                  int testValue = minimax(tempBoard, false, depth, -1000000000, 1000000000, i, j);
                  //System.out.println("considers " + i + " " + j + " with score " + testValue);
                  if (testValue > bestMoveValueSoFar) {
                     bestMoveValueSoFar = testValue; // the false is for is minimizer, because the maximizer has just played a virtual move
                     bestMoveRow = i;
                     bestMoveCol = j;
                  }
                  tempBoard[i][j] = EMPTY; // reset virtual move
                  
                  if ((System.nanoTime() - startTime)/1000000 >= timeOutMS) {
                     break perimeterLoop;
                  }
               }
            }
         }
      } else { 
         for (int i = lastMoveRow - 1; i <= lastMoveRow + 1; i++) {  //adjacent loop
            for (int j = lastMoveCol - 1; j <= lastMoveCol + 1; j++) {
               if (i >= 0 && i < NUMROW && j >= 0 && j < NUMCOL) {
                  //if (besidesPlayedSquare(i, j, tempBoard)) {
                  if (tempBoard[i][j] == EMPTY) {
                     tempBoard[i][j] = WHITE; // virtual move
                     int testValue = minimax(tempBoard, true, depth, -1000000000, 1000000000, i, j);
                  
                     if (testValue < bestMoveValueSoFar) {
                        bestMoveValueSoFar = testValue; // the true is for is maximizer, because the minimizer has just played a virtual move
                        bestMoveRow = i;
                        bestMoveCol = j;
                     }
                     tempBoard[i][j] = EMPTY; // reset virtual move
                  }
               }
            }    
         }
             
         perimeterLoop:
         for (int i = 0; i < NUMROW; i++) {
            for (int j = 0; j < NUMCOL; j++) { 
               if (besidesPlayedSquare(i, j, tempBoard) && !adjacentLastPlayed(i, j, lastMoveRow, lastMoveCol)) {            
                  tempBoard[i][j] = WHITE; // virtual move
                  int testValue = minimax(tempBoard, true, depth, -1000000000, 1000000000, i, j);
                  
                  if (testValue < bestMoveValueSoFar) {
                     bestMoveValueSoFar = testValue; // the true is for is maximizer, because the minimizer has just played a virtual move
                     bestMoveRow = i;
                     bestMoveCol = j;
                  }
                  tempBoard[i][j] = EMPTY; // reset virtual move
                  
                  if ((System.nanoTime() - startTime)/1000000 >= timeOutMS) {
                     break perimeterLoop;
                  }
               }
            }
         }
      }
   }
}

// // calculating board score REMEMBER NEVER RETURN -1, it is taken by the methods
//          int tempChecker;
//          
//          // open 4
//          tempChecker = getBoardScoreOpen(boardWithVirtualMove, 4);
//          if (tempChecker != -1) {
//             return tempChecker; // 400 or -400
//          }  
//          // 4
//          // open 3
//          tempChecker = getBoardScoreOpen(boardWithVirtualMove, 3);
//          if (tempChecker != -1) {
//             return tempChecker; // 300 or -300
//          }  
//          // 3
//          // open 2
//          tempChecker = getBoardScoreOpen(boardWithVirtualMove, 3);
//          if (tempChecker != -1) {
//             return tempChecker; // 200 or -200
//          }  
//          return 0;