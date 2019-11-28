package com.casper.testdrivendevelopment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private DrawThread drawThread;
    private ArrayList<Sprite>sprites=new ArrayList<>();

    float xTouch=-1,yTouch=-1;
    public GameView(Context context) {

        super(context);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);

        sprites.add(new Sprite(R.drawable.position));
        sprites.add(new Sprite(R.drawable.position));
        sprites.add(new Sprite(R.drawable.position));
        sprites.add(new Sprite(R.drawable.position));

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xTouch=event.getX();
                yTouch=event.getY();
                return false;
            }
        });

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //添加创建线程的代码
        if(null==drawThread){
            drawThread=new DrawThread();
            drawThread.start();//启动线程
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(null!=drawThread){
            drawThread.stopThread();
            drawThread=null;
        }
    }

    private class DrawThread extends Thread {

        private boolean beAlive = false;//控制线程结束
        public void stopThread(){
            beAlive=false;//理论上该赋值语句会导致函数线程在10秒内结束
            while(true){
                try{
                    this.join();//保证run方法执行完毕
                    break;
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        public void run() {

            beAlive = true;
            while (beAlive) {//使循环永久执行，直到beAlive在其他线程被置为false
                Canvas canvas = null;
                try {
                    synchronized (surfaceHolder) {
                        canvas = surfaceHolder.lockCanvas();//同步锁定
                        canvas.drawColor(Color.WHITE);//用白色填充画布对象
                        Paint paint=new Paint();
                        paint.setTextSize(50);
                        paint.setColor(Color.BLACK);
                        //canvas.drawText("打地鼠",getWidth()/2-50,40,paint);
                        //------------------------------------------------
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

                        if(xTouch > 0) {
                            canvas.drawText("你点击了" + xTouch + "," + yTouch, 100, 50, p);
                            //这里编辑点击后的事件

                            //(1,1)
                            if ((xTouch > canvas.getWidth() / 4 - 125) && (xTouch < canvas.getWidth() / 4 + 125)) {
                                if ((yTouch > canvas.getHeight() / 4 - 125) && (yTouch < canvas.getHeight() / 4 + 125)) {
                                    canvas.drawPoint(canvas.getWidth() / 4, canvas.getHeight() / 4, point);

                                }
                            }
                            //(1,2)
                            if ((xTouch > canvas.getWidth() / 2 - 125) && (xTouch < canvas.getWidth() / 2 + 125)) {
                                if ((yTouch > canvas.getHeight() / 4 - 125) && (yTouch < canvas.getHeight() / 4 + 125)) {
                                    canvas.drawPoint(canvas.getWidth() / 2, canvas.getHeight() / 4, point);
                                }
                            }
                            //(1,3)
                            if((xTouch > canvas.getWidth() / 4 * 3 - 125) && (xTouch < canvas.getWidth() * 3 / 4 + 125)){
                                if((yTouch > canvas.getHeight() / 4 - 125) && (yTouch < canvas.getHeight() / 4 + 125)){
                                    canvas.drawPoint(canvas.getWidth() / 4 * 3, canvas.getHeight() / 4, point);
                                }
                            }
                            //(2,1)
                            if((xTouch > canvas.getWidth() / 4 - 125) && (xTouch < canvas.getWidth() / 4 + 125)){
                                if((yTouch > canvas.getHeight() / 2 - 125) && (yTouch < canvas.getHeight() / 2 + 125)){
                                    canvas.drawPoint(canvas.getWidth() / 4, canvas.getHeight() / 2, point);
                                }
                            }

                            //(2,2)
                            if((xTouch > canvas.getWidth() / 2 - 125) && (xTouch < canvas.getWidth() / 2 + 125)){
                                if((yTouch > canvas.getHeight() / 2 - 125) && (yTouch < canvas.getHeight() / 2 + 125)){
                                    canvas.drawPoint(canvas.getWidth() / 2, canvas.getHeight() / 2, point);
                                }
                            }
                            //(2,3)
                            if((xTouch > canvas.getWidth() / 4 * 3 - 125) && (xTouch < canvas.getWidth() / 4 * 3 + 125)){
                                if((yTouch > canvas.getHeight() / 2 - 125) && (yTouch < canvas.getHeight() / 2 + 125)){
                                    canvas.drawPoint(canvas.getWidth() / 4 * 3, canvas.getHeight() / 2, point);
                                }
                            }
                            //(3,1)
                            if((xTouch > canvas.getWidth() / 4 - 125) && (xTouch < canvas.getWidth() / 4 + 125)){
                                if((yTouch > canvas.getHeight() / 4 * 3 - 125) && (yTouch < canvas.getHeight() / 4 * 3 + 125)){
                                    canvas.drawPoint(canvas.getWidth() / 4, canvas.getHeight() / 4 * 3, point);
                                }
                            }
                            //(3,2)
                            if((xTouch > canvas.getWidth() / 2 - 125) && (xTouch < canvas.getWidth() / 2 + 125)){
                                if((yTouch > canvas.getHeight() / 4 * 3 - 125) && (yTouch < canvas.getHeight() / 4 * 3 + 125)){
                                    canvas.drawPoint(canvas.getWidth() / 2, canvas.getHeight() / 4 * 3, point);
                                }
                            }
                            //(3,3)
                            if((xTouch > canvas.getWidth() / 4 * 3 - 125) && (xTouch < canvas.getWidth() / 4 * 3 + 125)){
                                if((yTouch > canvas.getHeight() / 4 * 3 - 125) && (yTouch < canvas.getHeight() / 4 * 3 + 125)){
                                    canvas.drawPoint(canvas.getWidth() / 4 * 3, canvas.getHeight() / 4 * 3, point);
                                }
                            }
                        }
                        else
                            canvas.drawText("hello world!",100,50,p);
                        //================================================

                        for(int i=0;i<sprites.size();i++)sprites.get(i).move();//让所有精灵移动
                        for(int i=0;i<sprites.size();i++)sprites.get(i).draw(canvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != canvas) {
                        surfaceHolder.unlockCanvasAndPost(canvas);//绘制完毕，通知界面更新
                    }
                }
                try {
                    Thread.sleep(10);//使线程休眠10毫秒，10毫秒后开始下一轮刷新
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class Sprite {
        private int resouceId;
        private int x, y;

        //设置两个数组，一个二维数组保存9个点的位置信息（三个黑点的圆心位置）
        //另一个二维数组存放相应的位置状态，即当前时刻有无地鼠出现
        int[][] position={{}};
        public Sprite(int resouceId) {
            this.resouceId = resouceId;
            int[][] position={{}};
            //ThreadLocalRandom.current().nextInt(0, MAX_RANGE);
            //使用ThreadLocalRandom.nextInt(0, n)生成0到n之间的随机整数
            x = (int) (Math.random() * getWidth());
            y = (int) (Math.random() * getHeight());//随机生成初始位置x和y

        }

        public void move() {
            //将移动的位置限定一下
            //下一个位置在一定概率下才会变化


            /*x += 15 * Math.cos(direction);
            //下面的两个if使得精灵在移动到屏幕外时从另一端出现
            if (x < 0)
                x = getWidth();
            else if (x > getWidth())
                x = 0;
            y += 15 * Math.sin(direction);
            if (y < 0) y = getHeight();
            else if (y > getHeight()) y = 0;
            //移动方向以二十分之一的概率随机变化
            if (Math.random() < 0.05)
                direction = Math.random() * 2 * Math.PI;*/
        }
        public void draw(Canvas canvas){
            //在画布的指定位置画出图标
            Drawable drawable= getContext().getResources().getDrawable(R.drawable.down_round);
            Rect drawableRect=new Rect(x,y,x+drawable.getIntrinsicWidth(),y+drawable.getIntrinsicHeight());
            drawable.setBounds(drawableRect);
            drawable.draw(canvas);
        }
    }

}