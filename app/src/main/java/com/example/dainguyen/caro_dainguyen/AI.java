package com.example.dainguyen.caro_dainguyen;



class AiMove{
    int row;
    int col;
    int score;


    public AiMove(){

    }
    public AiMove(int score){
        this.score = score;
    }

    public AiMove(int row, int col, int score) {
        this.col = col;
        this.row = row;
        this.score = score;
    }
}


public class AI {
    ChessBoard chessBoard;
    STATE      prvPlayer;

    public static final int SCORE = 100;
    public static final int MAX = 100000;
    public static final int MIN = -100000;



    public AI(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    /* Minimax Alpha Beta Pruning */
    public AiMove getBestMoveAlphaBeta(int prvRow, int prvCol, STATE player, int depth, int alpha, int beta) {
        AiMove bestMove = new AiMove();

        /* Kiem tra nguoi choi luot danh truoc do */
        if (player == STATE.PLAYER) prvPlayer = STATE.COMPUTER;
        else prvPlayer = STATE.PLAYER;


        /* Trang thai ket thuc ban co?! */
         if (chessBoard.checkIfVictory(prvRow, prvCol, prvPlayer)) {
            if (chessBoard.ST_ENDGAME == STATE.COMPUTER) {
                bestMove.score = SCORE - depth;
            } else if (chessBoard.ST_ENDGAME == STATE.PLAYER) {
                bestMove.score = -SCORE + depth;
            } else  {
                bestMove.score = -depth;
            }
        } else  if (player == STATE.COMPUTER) {
            bestMove.score = alpha;

            forLoop:
            for (int stRow = 0; stRow < ChessBoard.numRow; stRow++) {
                for (int stCol = 0; stCol < ChessBoard.numCol; stCol++) {
                    if (chessBoard.ST_CHESSBOARD[stRow][stCol] == STATE.NULL) {

                        chessBoard.ST_CHESSBOARD[stRow][stCol] = player;
                        chessBoard.numTotalChecked++;

                        int score = getBestMoveAlphaBeta(stRow, stCol, STATE.PLAYER, depth + 1, bestMove.score, beta).score;

                       /* tra de quy */
                        chessBoard.numTotalChecked--;
                        chessBoard.ST_CHESSBOARD[stRow][stCol] = STATE.NULL;


                        if ( score > bestMove.score) {
                            bestMove.row = stRow;
                            bestMove.col = stCol;
                            bestMove.score = score;
                        }
                        if(beta <= bestMove.score ) break forLoop;
                    }
                }
            }
        } else {
            bestMove.score = beta;

            forLoop:
            for (int stRow = 0; stRow < ChessBoard.numRow; stRow++) {
                for (int stCol = 0; stCol < ChessBoard.numCol; stCol++) {
                    if (chessBoard.ST_CHESSBOARD[stRow][stCol] == STATE.NULL) {

                        chessBoard.ST_CHESSBOARD[stRow][stCol] = player;
                        chessBoard.numTotalChecked++;

                        int score = getBestMoveAlphaBeta(stRow, stCol, STATE.COMPUTER, depth + 1, alpha, bestMove.score).score;

                       /* tra de quy */
                        chessBoard.numTotalChecked--;
                        chessBoard.ST_CHESSBOARD[stRow][stCol] = STATE.NULL;


                        if ( score < bestMove.score) {
                            bestMove.row = stRow;
                            bestMove.col = stCol;
                            bestMove.score = score;
                        }
                        if(bestMove.score <= alpha) break forLoop;
                    }
                }
            }
        }
        return bestMove;
    }


    public AiMove getBestMove(int prvRow, int prvCol, STATE player, int depth) {
        AiMove aiBestMove = new AiMove();

        /* Kiem tra nguoi danh truoc do */
        if (player == STATE.PLAYER) prvPlayer = STATE.COMPUTER;
        else prvPlayer = STATE.PLAYER;

        if (chessBoard.checkIfVictory(prvRow, prvCol, prvPlayer)) {
            if (chessBoard.ST_ENDGAME == STATE.COMPUTER) {
                aiBestMove.score = SCORE - depth;
            } else if (chessBoard.ST_ENDGAME == STATE.PLAYER) {
                aiBestMove.score = -SCORE + depth;
            } else if (chessBoard.ST_ENDGAME == STATE.TIE_VAL) {
                aiBestMove.score = -depth;
            }
        } else if (player == STATE.COMPUTER) {
            aiBestMove.score = MIN;

            for (int stRow = 0; stRow < ChessBoard.numRow; stRow++) {
                for (int stCol = 0; stCol < ChessBoard.numCol; stCol++) {
                    if (chessBoard.ST_CHESSBOARD[stRow][stCol] == STATE.NULL) {

                        chessBoard.ST_CHESSBOARD[stRow][stCol] = player;
                        chessBoard.numTotalChecked++;

                        int score = getBestMove(stRow, stCol, STATE.PLAYER, depth + 1).score;
                        if ( score > aiBestMove.score) {
                            aiBestMove.row = stRow;
                            aiBestMove.col = stCol;
                            aiBestMove.score = score;
                        }

                       /* tra de quy */
                        chessBoard.numTotalChecked--;
                        chessBoard.ST_CHESSBOARD[stRow][stCol] = STATE.NULL;
                    }
                }
            }
        } else {
            aiBestMove.score = MAX;

            for (int stRow = 0; stRow < ChessBoard.numRow; stRow++) {
                for (int stCol = 0; stCol < ChessBoard.numCol; stCol++) {
                    if (chessBoard.ST_CHESSBOARD[stRow][stCol] == STATE.NULL) {

                        chessBoard.ST_CHESSBOARD[stRow][stCol] = player;
                        chessBoard.numTotalChecked++;

                        int score = getBestMove(stRow, stCol, STATE.COMPUTER, depth + 1).score;

                        if ( score < aiBestMove.score) {
                            aiBestMove.row = stRow;
                            aiBestMove.col = stCol;
                            aiBestMove.score = score;
                        }

                       /* tra de quy */
                        chessBoard.numTotalChecked--;
                        chessBoard.ST_CHESSBOARD[stRow][stCol] = STATE.NULL;
                    }
                }
            }
        }

        return aiBestMove;
    }

}
