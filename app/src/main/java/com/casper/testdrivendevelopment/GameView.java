package com.casper.testdrivendevelopment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    float xTouch=-1,yTouch=-1;
    public static final int MAXTIMEMUL = 60; //精灵持续时间的最大倍数（MAXTIMEMUL*10ms）
    public static final int MINTIMEMUL = 35; //精灵持续时间的最小倍数（MINTIMEMUL*10ms）
    public static final int DISPLAYMAX = 4; //精灵显示的最多数量（一共9只）
    public static final int MINNUM = 1; //精灵显示的最少数量（一共9只）
    private static final int IMGSIZE = 150;  //精灵绘制时的图片大小
    private int clickCount = 0;   //记录打中的次数
    private int[] xPosition;  //三列地鼠开始点的x横坐标
    private int[] yPosition;  //三行地鼠开始点的y纵坐标
    private int counter = 0;  //记录当前出现的地鼠数
    //获取fragment宽度和高度
    int fragmentWidth;
    int fragmentHeight;
    private SurfaceHolder surfaceHolder;
    private DrawThread drawThread;
    private ArrayList<Sprite> sprites = new ArrayList<>();

    public GameView(Context context) {
        super(context);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        fragmentWidth = getResources().getConfiguration().screenWidthDp;
        fragmentHeight = getResources().getConfiguration().screenHeightDp;
        Log.d(TAG, "GameView: fragmentWidth= "+ fragmentWidth +", fragmentHeight= "+ fragmentHeight);

        xPosition = new int[]{fragmentWidth/3+60, fragmentWidth/3*4-80 , fragmentWidth/3*6-80};
        yPosition = new int[]{fragmentHeight/3+60, fragmentHeight/3*3, fragmentHeight /3*5-80};

        //添加九个精灵
        sprites.add(new Sprite(R.drawable.down_round, xPosition[0], yPosition[0]));
        sprites.add(new Sprite(R.drawable.down_round, xPosition[1], yPosition[0]));
        sprites.add(new Sprite(R.drawable.down_round, xPosition[2], yPosition[0]));
        sprites.add(new Sprite(R.drawable.down_round, xPosition[0], yPosition[1]));
        sprites.add(new Sprite(R.drawable.down_round, xPosition[1], yPosition[1]));
        sprites.add(new Sprite(R.drawable.down_round, xPosition[2], yPosition[1]));
        sprites.add(new Sprite(R.drawable.down_round, xPosition[0], yPosition[2]));
        sprites.add(new Sprite(R.drawable.down_round, xPosition[1], yPosition[2]));
        sprites.add(new Sprite(R.drawable.down_round, xPosition[2], yPosition[2]));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (null == drawThread) {
            drawThread = new DrawThread();
            drawThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null!=drawThread) {
            drawThread.stopThread();
            drawThread = null;
        }
    }

    private class DrawThread extends Thread {
        private boolean beAlive = false;     //用于控制线程结束

        //终止线程
        public void stopThread() {
            beAlive = false;  //当执行这一条语句后，run函数将在线程10毫秒(run中设置的等待时间)内结束运行
            while (true) {
                try {
                    this.join();  //处理线程的收尾工作，保证run方法执行完毕
                    break;  //join成功执行时跳出循环
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void run() {
            //一直执行，直到beAlive被其它线程设置为false
            beAlive = true;
            //打乱精灵数组顺序
            ArrayList<Sprite> currentSprites=new ArrayList<Sprite>(sprites);
            Collections.shuffle(currentSprites);
            while (beAlive) {
                counter = 0;  //每一次循环开始前重置计数器
                Canvas canvas = null;
                try {
                    //获取及绘制canvas对象时，用surfaceHolder对象同步锁定
                    synchronized (surfaceHolder) {
                        canvas = surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.WHITE);
                        Paint paint = new Paint();
                        paint.setTextSize(70);
                        paint.setColor(Color.BLACK);
                        canvas.drawText("击中了： ",50,80,paint);
                        canvas.drawText(clickCount+"  个！",330,80,paint);

                        Paint p = new Paint();
                        p.setTextSize(50);
                        p.setColor(Color.BLACK);
                        Paint point = new Paint();//负责改变点的颜色
                        point.setColor(Color.RED);
                        point.setStrokeWidth(250);
                        //布置9张图片
                        //绘制Cap为ROUND的点
                        paint.setStrokeWidth(250);//设置线宽，如果不设置线宽，无法绘制点
                        //绘制的正方形边长是150
                        //canvas.translate(0, yTouch);
                        paint.setStrokeCap(Paint.Cap.SQUARE);
                        canvas.drawPoint(canvas.getWidth() / 4, canvas.getHeight() / 4, paint);
                        canvas.drawPoint(canvas.getWidth() / 2, canvas.getHeight() / 4, paint);
                        canvas.drawPoint(canvas.getWidth() * 3 / 4, canvas.getHeight() / 4, paint);
                        canvas.drawPoint(canvas.getWidth() / 4, canvas.getHeight() / 2, paint);
                        canvas.drawPoint(canvas.getWidth() / 2, canvas.getHeight() / 2, paint);
                        canvas.drawPoint(canvas.getWidth() * 3 / 4, canvas.getHeight() / 2, paint);
                        canvas.drawPoint(canvas.getWidth() / 4, canvas.getHeight() * 3 / 4, paint);
                        canvas.drawPoint(canvas.getWidth() / 2, canvas.getHeight() * 3 / 4, paint);
                        canvas.drawPoint(canvas.getWidth() * 3 / 4, canvas.getHeight() * 3 / 4, paint);

                        for (int i=0;i<currentSprites.size();i++) {
                            currentSprites.get(i).updateDuration();
                            if (currentSprites.get(i).isDisplay())
                                counter++;
                            //最多显示4只
                            if (counter > DISPLAYMAX) currentSprites.get(i).setDisplay(false);
                        }
                        for (int i=0;i<currentSprites.size();i++) currentSprites.get(i).draw(canvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != canvas) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                //循环最后线程休眠10毫秒，再开始下一轮的刷新
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Sprite {
        private int resouceId;
        private int x,y;  //精灵的显示位置
        private boolean isDisplay;
        private int turnout;
        public Sprite(int resouceId, int x, int y) {
            this.resouceId = resouceId;
            this.x = x;
            this.y = y;
            isDisplay = true;
            turnout = 0;
        }

        public void updateDuration() {
            if (0 == turnout) {
                turnout = (int)(Math.random()*100)% MAXTIMEMUL;
                if(turnout<MINTIMEMUL) turnout=MINTIMEMUL;
                isDisplay = Math.random()<0.5;  //true/false
            }
            else {
                turnout--;
            }
        }
        public void draw(Canvas canvas) {
            if (isDisplay) {
                Drawable drawable = getContext().getResources().getDrawable(resouceId);
                Rect drawableRect = new Rect(x, y, x + IMGSIZE, y + IMGSIZE);
                drawable.setBounds(drawableRect);
                drawable.draw(canvas);
            }
        }

        public boolean isDisplay() {
            return this.isDisplay;
        }

        public void setDisplay(boolean display) {
            this.isDisplay = display;
        }

    }

    //点击事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //屏幕点击位置的x横坐标
        int xTouch = (int) event.getX();
        //屏幕点击位置的y纵坐标
        int yTouch = (int) event.getY();
        //判断是否点中了地鼠
        //第一列
        if (xTouch > xPosition[0] && xTouch < xPosition[0]+IMGSIZE) {
            if (yTouch > yPosition[0] && yTouch < yPosition[0] + IMGSIZE) {
                if(sprites.get(0).isDisplay){
                    sprites.get(0).setDisplay(false);  //第一行
                    counter--;
                    clickCount++;
                }
            } else if (yTouch > yPosition[1] && yTouch < yPosition[1] + IMGSIZE) {
                if(sprites.get(3).isDisplay){
                    sprites.get(3).setDisplay(false);  //第二行
                    counter--;
                    clickCount++;
                }
            } else if (yTouch > yPosition[2] && yTouch < yPosition[2] + IMGSIZE) {
                if(sprites.get(6).isDisplay){
                    sprites.get(6).setDisplay(false);  //第三行
                    counter--;
                    clickCount++;
                }
            }
        }
        //第二列
        else if (xTouch > xPosition[1] && xTouch < xPosition[1]+IMGSIZE) {
            if (yTouch > yPosition[0] && yTouch < yPosition[0] + IMGSIZE) {
                if(sprites.get(1).isDisplay){
                    sprites.get(1).setDisplay(false);  //第一行
                    counter--;
                    clickCount++;
                }
            } else if (yTouch > yPosition[1] && yTouch < yPosition[1] + IMGSIZE) {
                if(sprites.get(4).isDisplay){
                    sprites.get(4).setDisplay(false);  //第二行
                    counter--;
                    clickCount++;
                }
            } else if (yTouch > yPosition[2] && yTouch < yPosition[2] + IMGSIZE) {
                if(sprites.get(7).isDisplay){
                    sprites.get(7).setDisplay(false);  //第三行
                    counter--;
                    clickCount++;
                }
            }
        }
        //第三列
        else if (xTouch > xPosition[2] && xTouch < xPosition[2]+IMGSIZE) {
            if (yTouch > yPosition[0] && yTouch < yPosition[0] + IMGSIZE) {
                if(sprites.get(2).isDisplay){
                    sprites.get(2).setDisplay(false);  //第一行
                    counter--;
                    clickCount++;
                }
            } else if (yTouch > yPosition[1] && yTouch < yPosition[1] + IMGSIZE) {
                if(sprites.get(5).isDisplay){
                    sprites.get(5).setDisplay(false);  //第二行
                    counter--;
                    clickCount++;
                }
            } else if (yTouch > yPosition[2] && yTouch < yPosition[2] + IMGSIZE) {
                if(sprites.get(8).isDisplay){
                    sprites.get(8).setDisplay(false);  //第三行
                    counter--;
                    clickCount++;
                }
            }
        }
        return super.onTouchEvent(event);
    }

}

