package com.example.dainguyen.caro_dainguyen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import com.example.dainguyen.caro_dainguyen.R;

import java.util.ArrayList;
import java.util.List;



public class ChessBoard {

    private Context context;
    private Canvas canvas;
    private Paint paint;
    private   int          bmWidth, bmHeight;
    public    static int   numCol, numRow;


    private List<Line> lineList;
    public static STATE[][] ST_CHESSBOARD; // Trang thai ban co
    public static STATE     ST_ENDGAME;  // Trang thai ket thuc ban co
    public static STATE     crrStPlayer; // NGuoi choi hien tai
    public static boolean   endGame;   // Kiem tra ket thuc van co;
    public static int       N = 3;
    public static int       numTotalChecked; //Tong so nuoc da danh;
    private int             heightBoxTouched;

    public static int       prvRow;
    public static int       prvCol;





    public ChessBoard(Context context, Canvas canvas, Paint paint, int bmWidth, int bmHeight, int numRow, int numCol) {
        this.context  = context;
        this.canvas   = canvas;
        this.paint    = paint;
        this.bmHeight = bmHeight;
        this.bmWidth  = bmWidth;
        this.numRow   = numRow;
        this.numCol   = numCol;
    }

    /* Khoi tao ban co*/
    public void initChessBoard(){
        /* init Chessboard */
        lineList = new ArrayList<>();

        for(int i = 0; i <= numRow; i++){
            if( i == 0) {
                lineList.add( new Line(new Point(0, i * bmHeight/ numRow +  (int)paint.getStrokeWidth()/2),
                        new Point(bmWidth, i * bmHeight/ numRow +  (int)paint.getStrokeWidth()/2)));
            } else if ( i == numRow) {
                lineList.add( new Line(new Point(0, i * bmHeight/ numRow -  (int)paint.getStrokeWidth()/2),
                        new Point(bmWidth, i * bmHeight/ numRow -  (int)paint.getStrokeWidth()/2)));
            } else {
                lineList.add( new Line(new Point(0, i * bmHeight/ numRow), new Point(bmWidth, i * bmHeight/ numRow)));
            }

        }

        for(int i = 0; i <= numCol; i++) {
            if( i == 0) {
                lineList.add(new Line(new Point(i * bmWidth/ numCol + (int)paint.getStrokeWidth()/2, 0),
                        new Point(i * bmWidth/ numCol + (int)paint.getStrokeWidth()/2, bmHeight)));
            } else if ( i == numCol) {
                lineList.add(new Line(new Point(i * bmWidth/ numCol - (int)paint.getStrokeWidth()/2 , 0),
                        new Point(i * bmWidth/ numCol - (int)paint.getStrokeWidth()/2, bmHeight)));
            } else {
                lineList.add(new Line(new Point(i * bmWidth/ numCol, 0), new Point(i * bmWidth/ numCol, bmHeight)));
            }
        }

        /* init State chessboard */
        ST_CHESSBOARD = new STATE[numRow][numCol];
        for (int i = 0; i < numRow; i++){
            for (int j = 0; j < numCol; j++){
                ST_CHESSBOARD[i][j] = STATE.NULL;
            }
        }

        /**/
        heightBoxTouched = bmHeight/numRow;

        /* init player */
        crrStPlayer     = STATE.PLAYER;
        numTotalChecked = 0;
        endGame         = false;
        ST_ENDGAME      = STATE.NULL;

    }

    /* Ve ban co*/
    public void drawChessBoard(){
        for (Line line : lineList) {
            canvas.drawLine(line.getStartPoint().getX(), line.getStartPoint().getY(),
                    line.getEndPoint().getX(), line.getEndPoint().getY(), paint);
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        Point point = new Point((int) event.getX(), (int) event.getY());

        int width = v.getWidth();
        int height = v.getHeight();

        int widthOfBox  = width  / numCol;
        int heightOfBox = height / numRow;

        int stateRow    = point.getY() / widthOfBox;
        int stateCol    = point.getX() / heightOfBox;

        if ( ST_CHESSBOARD[stateRow][stateCol] == STATE.NULL) {

            ST_CHESSBOARD[stateRow][stateCol] = STATE.PLAYER;

            prvRow = stateRow;
            prvCol = stateCol;

            /* Tang so luot danh len +1 */
            numTotalChecked++;

            /* Ve ban co */
            drawChessBox(stateRow, stateCol, v, STATE.PLAYER);
            endGame = checkIfVictory(stateRow, stateCol, STATE.PLAYER);

            return true;
        }
        return false;
    }

    /* Com tim nuoc di */
    public void AiMove(int prvRow, int prvCol, View view){
        AiMove aiMove;
        AI ai = new AI(this);
        // aiMove =  ai.getBestMove(prvRow, prvCol, STATE.COMPUTER, 0);

        /* Tim kiem nuoc di tot nhat - minimax*/
        aiMove = ai.getBestMoveAlphaBeta(prvRow, prvCol, STATE.COMPUTER, 0, AI.MIN, AI.MAX);

        /* Danh dau nuoc di va ve ban co*/
        ST_CHESSBOARD[aiMove.row][aiMove.col] = STATE.COMPUTER;
        drawChessBox(aiMove.row, aiMove.col, view, STATE.COMPUTER);


        /* Kiem tra trang thai ban co*/
        numTotalChecked++;
        endGame = this.checkIfVictory(aiMove.row, aiMove.col, STATE.COMPUTER);
    }


    /* Ve on co da banh */
    private void drawChessBox(int stateRow, int stateCol, View view, STATE crrStPlayer) {
        /* Tam O*/
        Point center = new Point((int) ((stateCol + 0.5) * bmWidth / numCol), (int) ((stateRow + 0.5) * bmHeight / numRow));
        Point leftTop = new Point(center.getX() - heightBoxTouched / 2, center.getY() - heightBoxTouched / 2);
        Point rightBottom = new Point(center.getX() + heightBoxTouched / 2, center.getY() + heightBoxTouched / 2);


        Bitmap touchedBitmap = null;
        if (crrStPlayer == STATE.PLAYER) {
            touchedBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.o);
        } else if (crrStPlayer == STATE.COMPUTER) {
            touchedBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.x);
        }

        if (touchedBitmap != null) {
            canvas.drawBitmap(touchedBitmap, new Rect(0, 0, touchedBitmap.getWidth(), touchedBitmap.getHeight()),
                    new Rect(leftTop.getX(), leftTop.getY(),
                            rightBottom.getX(), rightBottom.getY()),
                    paint);
            view.invalidate();
        }
    }


    /* Kiem tra thang thua */
    public boolean checkIfVictory(int crrRow, int crrCol, STATE player){
        /* Danh full ban cmn co */
        if( checkRow(crrRow, crrCol, player) >= N || checkCol(crrRow, crrCol, player) >= N ||
                checkDiagonalLine(crrRow, crrCol, player) >= N || checkBackslash(crrRow, crrCol, player) >= N) {
            ST_ENDGAME = player;
            return true;
        }

        if(numTotalChecked == (numRow*numCol)) {
            ST_ENDGAME = STATE.TIE_VAL;
            return true;
        }

        return false;
    }


    public int checkRow(int crrRow, int crrCol, STATE player){
        int count = 1;
        /* Di len */
        int row = crrRow;
        do {
            row--;
            if(row >= 0 && ST_CHESSBOARD[row][crrCol] == player) {
                count++;
            } else break;
        } while (true);

        /* Di len */
        row = crrRow;
        do {
            row++;
            if(row < numRow && ST_CHESSBOARD[row][crrCol] == player) {
                count++;
            } else break;
        } while (true);

        return count;
    }

    public int checkCol(int crrRow, int crrCol , STATE player){
        int count = 1;
        /* Di qua trai */
        int col = crrCol;
        do {
            col--;
            if(col >= 0 && ST_CHESSBOARD[crrRow][col] == player) {
                count++;
            } else break;
        } while (true);

        /* Di quan phai */
        col = crrCol;
        do {
            col++;
            if(col < numCol && ST_CHESSBOARD[crrRow][col] == player) {
                count++;
            } else break;
        } while (true);

        return count;
    }

    public int checkDiagonalLine(int crrRow, int crrCol, STATE player) {
        int count = 1;

         /* Di len doc phai tren */
        int col = crrCol;
        int row = crrRow;
        do {
            col--;
            row--;
            if(col >= 0 && row >= 0 && ST_CHESSBOARD[row][col] == player) {
                count++;
            } else break;
        } while (true);

        /* Di quan phai */
        col = crrCol;
        row = crrRow;
        do {
            col++;
            row++;
            if(col < numCol && row < numRow && ST_CHESSBOARD[row][col] == player) {
                count++;
            } else break;
        } while (true);

        return count;
    }


    public int checkBackslash(int crrRow, int crrCol, STATE player) {
        int count = 1;

         /* Di len goc phai tren */
        int col = crrCol;
        int row = crrRow;
        do {
            col++;
            row--;
            if(col < numCol && row >= 0 && ST_CHESSBOARD[row][col] == player) {
                count++;
            } else break;
        } while (true);

        /* Di quan trai duoi */
        col = crrCol;
        row = crrRow;
        do {
            col--;
            row++;
            if(col >= 0 && row < numRow  && ST_CHESSBOARD[row][col] == player) {
                count++;
            } else break;
        } while (true);

        return count;
    }

}
