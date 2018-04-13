package com.example.dainguyen.caro_dainguyen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    /* final value */
    public static final int NUM_ROW         = 4;
    public static final int NUM_COL         = 4;
    public static final int BM_WIDTH        = 360;
    public static final int BM_HEIGHT       = 360;

    /* */
    private Button btnPlay;
    private ImageView imgChessBoard;
    private TextView txtPlayer;
    private TextView txtComputer;
    private TextView txtKQ;

    /* Ob to draw chessboard */
    private ChessBoard      chessBoard;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* mapping widget */
        mapping();


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPlayer.setBackgroundColor(Color.RED);
                imgChessBoard.setEnabled(true);
                btnPlay.setEnabled(false);


                bitmap = Bitmap.createBitmap(BM_WIDTH, BM_HEIGHT, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                paint = new Paint();
                paint.setARGB(255, 200, 200, 200 );
                paint.setStrokeWidth(2);

                chessBoard = new ChessBoard(MainActivity.this, canvas, paint, BM_WIDTH, BM_HEIGHT, NUM_ROW, NUM_COL);

                chessBoard.initChessBoard();
                chessBoard.drawChessBoard();

                imgChessBoard.setImageBitmap(bitmap);
            }
        });

        imgChessBoard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if ( !chessBoard.onTouch(v, event) ) return false;

                    txtPlayer.setBackgroundColor(Color.GRAY);
                    txtComputer.setBackgroundColor(Color.RED);

                    if (chessBoard.endGame == true){
                        if(ChessBoard.ST_ENDGAME == STATE.TIE_VAL) {
                            txtKQ.setText("DRAW");
                        }else if( ChessBoard.ST_ENDGAME == STATE.COMPUTER) {
                            txtKQ.setText("COM WIN");
                        } else {
                            txtKQ.setText("PLAYER WIN");
                        }
                        imgChessBoard.setEnabled(false);
                        btnPlay.setEnabled(true);
                        return true;
                    }
                } else if ( event.getAction() == MotionEvent.ACTION_UP  ){
                    chessBoard.AiMove(chessBoard.prvRow, chessBoard.prvCol, v);

                    txtPlayer.setBackgroundColor(Color.GREEN);
                    txtComputer.setBackgroundColor(Color.GRAY);

                    if (chessBoard.endGame == true){
                        if(ChessBoard.ST_ENDGAME == STATE.TIE_VAL) {
                            txtKQ.setText("DRAW");
                        }else if( ChessBoard.ST_ENDGAME == STATE.COMPUTER) {
                            txtKQ.setText("COM WIN");
                        } else {
                            txtKQ.setText("PLAYER WIN");
                        }
                        imgChessBoard.setEnabled(false);
                        btnPlay.setEnabled(true);
                        return true;
                    }
                }
                return true;
            }
        });
    }


    private void mapping(){
        this.btnPlay        = (Button)    this.findViewById(R.id.btnPlay);
        this.imgChessBoard  = (ImageView) this.findViewById(R.id.imgChessBoard);
        this.txtComputer    = (TextView)  this.findViewById(R.id.txtComputer);
        this.txtPlayer      = (TextView)  this.findViewById(R.id.txtPlayer);
        this.txtKQ          = (TextView)  this.findViewById(R.id.txtKQ);
    }
}



