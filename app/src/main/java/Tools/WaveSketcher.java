package Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.brainbeats.R;

import java.util.ArrayList;
import java.util.List;

import Models.BeatModel;

/**
 * Created by Douglas on 4/7/2015.
 */
public class WaveSketcher extends SurfaceView implements SurfaceHolder.Callback {

    // Global variables
    public Boolean _run;
    public boolean isDrawing = true;
    public DrawThread thread;
    public BeatModel mBeatModel;
    Bitmap mBitmap;

    public WaveSketcher(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        getHolder().addCallback(this);
        thread = new DrawThread(getHolder());
    }
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 1000;
        int desiredHeight = 3000;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    public void SetBeat(BeatModel beatmodel){
        mBeatModel = beatmodel;
    }

    public class DrawThread extends  Thread {
        private SurfaceHolder mSurfaceHolder;

        // All of our graphics variables
        public Paint mCircleLabelsPaint;
        public Paint mCenterPaint;
        public Paint mCenterCircleFramePaint;
        public Paint mCircleWaveHeaderPaint;
        public Paint mWavePaint;
        public Paint mAlphaPaint = new Paint();
        public Paint mWaveTilesPaint;
        public Paint mGammaPaint = new Paint();
        public Paint mThataPaint = new Paint();
        public Paint mDeltaPaint = new Paint();
        public Paint framePaint;

        //Screen bounds
        int maxX;
        int maxY;

        // Calculate the middle
        int halfX;
        int halfY;

        //Center circle starting radius
        int centerCircleRadius;
        int numberOfCircles = 100;
        int centerCircleStartY;
        int centerCircleMarginX = 125;
        int centerCircleMarginY = 880;


        //Wave variables
        //Alpha params
        double  mAlphaWaveAmplitude;
        double  mAlphaWaveCrestHeight;
        double  mAlphaHertz;
        double  alphwaveStartY = 1350;
        List<Point> alphaWavePoints;
        List<Point> alphaJumpPoints;
        boolean alphaIsIncreasing = false;
        double alphaYdy = 0;
        double alphaXdx = 1;

        //Gamma params
        double  mGammaWaveAmplitude;
        double  mGammaWaveCrestHeight;
        double  mGammaHertz;
        double gammawaveStartY = 1750;
        List<Point> gammaWavePoints;
        List<Point> gammaJumpPoints;
        boolean gammaIsIncreasing = false;
        double  gammaYdy = 0;
        double  gammaXdx = 1;
        double waveLength = 530;

        //Theta params
        double  mThetaWaveAmplitude;
        double  mThetaWaveCrestHeight;
        double  mThetaHertz;
        double ThetawaveStartY = 2150;
        List<Point> thetaWavePoints;
        List<Point> thetaJumpPoints;
        boolean thetaIsIncreasing = false;
        double  thetaYdy = 0;
        double  thetaXdx = 1;

        //Delta params
        double  mDeltaWaveAmplitude;
        double  mDeltaWaveCrestHeight;
        double  mDeltaHertz;
        double deltaWaveStartY = 2550;
        List<Point> deltaWavePoints;
        List<Point> deltaJumpPoints;
        boolean deltaIsIncreasing = false;
        double  deltaYdy = 0;
        double  deltaXdx = 1;

        //Wave changing factors
        double xdx = 1;
        double ydy = 0;

        public DrawThread (SurfaceHolder surfaceHolder){
            mSurfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean run) {
            _run = run;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            InitDrawingParams(); //initializes all of drawing paramaters
            while (_run){
                UpdateDrawingParams(); //makes all changes and updates to our wave
                if(isDrawing == true){
                    try {
                        canvas = mSurfaceHolder.lockCanvas(null);
                        canvas.drawColor(Color.WHITE);
                        maxX = canvas.getWidth();
                        maxY = canvas.getHeight();
                        halfX = maxX / 2;
                        halfY = maxY / 2;

                        //circle dimensions
                        centerCircleStartY = halfY - centerCircleMarginY;
                        centerCircleRadius = halfX - centerCircleMarginX;

                        //Declare our points for drawing the curve
                        Path alphaWavePath = new Path();
                        alphaWavePoints = new ArrayList<Point>(); //holds the points of the wave
                        alphaJumpPoints = new ArrayList<Point>(); //holds the points where the wave hits the amplitude
                        alphaJumpPoints.add(new Point(0, (int) alphwaveStartY)); //add the first point to our jump points
                        mAlphaWaveCrestHeight = mAlphaWaveAmplitude;
                        alphaYdy = 0;

                        Path gammaWavePath = new Path();
                        gammaWavePoints = new ArrayList<Point>(); //holds the points of the wave
                        gammaJumpPoints = new ArrayList<Point>(); //holds the points where the wave hits the amplitude
                        gammaJumpPoints.add(new Point(0, (int) gammawaveStartY)); //add the first point to our jump points
                        mGammaWaveCrestHeight = mGammaWaveAmplitude;
                        gammaYdy = 0;


                        //Theta waves
                        Path thetaWavePath = new Path();
                        thetaWavePoints = new ArrayList<Point>(); //holds the points of the wave
                        thetaJumpPoints = new ArrayList<Point>(); //holds the points where the wave hits the amplitude
                        thetaJumpPoints.add(new Point(0, (int) ThetawaveStartY)); //add the first point to our jump points
                        mThetaWaveCrestHeight = mThetaWaveAmplitude;
                        thetaYdy = 0;

                        //Delta waves
                        Path deltaWavePath = new Path();
                        deltaWavePoints = new ArrayList<Point>(); //holds the points of the wave
                        deltaJumpPoints = new ArrayList<Point>(); //holds the points where the wave hits the amplitude
                        deltaJumpPoints.add(new Point(0, (int) deltaWaveStartY)); //add the first point to our jump points
                        mDeltaWaveCrestHeight = mDeltaWaveAmplitude;
                        deltaYdy = 0;

                        waveLength = maxX;

/*                        canvas.drawText("Pre-Surgery", 220, 100, mCircleWaveHeaderPaint);*/
/*                        canvas.drawLine(0, 140, maxX, 140, mWavePaint);*/

                        //Draw header
                        canvas.drawLine(280, 362, 800,880, mWavePaint);
                        canvas.drawLine(800, 362, 280, 880, mWavePaint);

                        //draw center dashboard labels
                        canvas.drawText("\u03B8", halfX, (float) (centerCircleStartY - centerCircleRadius - 20), mThataPaint);
                        canvas.drawText("\u03B2", halfX, (float) (centerCircleStartY + centerCircleRadius + 50), mGammaPaint);
                        canvas.drawText("\u03B3", halfX - centerCircleRadius - 50, centerCircleStartY, mDeltaPaint);
                        canvas.drawText("\u03B1", halfX + centerCircleRadius + 20, centerCircleStartY, mAlphaPaint);

                        canvas.drawCircle(halfX, centerCircleStartY, centerCircleRadius, mCenterCircleFramePaint);
                        //Draw Center circle and all quarter circle sectors
                        for(int centerCircleIndex = 0; centerCircleIndex < numberOfCircles; centerCircleIndex++){
                            canvas.drawCircle(halfX, centerCircleStartY, centerCircleRadius, mCenterPaint);
                            if((numberOfCircles - centerCircleIndex) <= mGammaWaveAmplitude){ // gamma circle sectors
                                Point gammaPoint = new Point(halfX, halfY - centerCircleMarginY);
                                float radius = centerCircleRadius;
                                final RectF oval = new RectF();
                                oval.set(gammaPoint.x - radius, gammaPoint.y - radius, gammaPoint.x + radius, gammaPoint.y + radius);
                                Path gammaPath = new Path();
                                gammaPath.arcTo(oval, 45, 90, true);
                                canvas.drawPath(gammaPath,mGammaPaint);
                            }

                            if((numberOfCircles - centerCircleIndex) <= mAlphaWaveAmplitude){ // alpha circle sector
                                Point alphaPoint = new Point(halfX, halfY - centerCircleMarginY);
                                float radius = centerCircleRadius;
                                final RectF ovalAlpha = new RectF();
                                ovalAlpha.set(alphaPoint.x - radius, alphaPoint.y - radius, alphaPoint.x + radius, alphaPoint.y + radius);
                                Path alphaPath = new Path();
                                alphaPath.arcTo(ovalAlpha, 315, 90, true);
                                canvas.drawPath(alphaPath,mAlphaPaint);
                            }

                            if((numberOfCircles - centerCircleIndex) <=  mThetaWaveAmplitude){ //theta wave
                                Point thetaPoint = new Point(halfX , halfY - centerCircleMarginY);
                                float radius = centerCircleRadius;
                                final RectF ovalTheta = new RectF();
                                ovalTheta.set(thetaPoint.x - radius, thetaPoint.y - radius, thetaPoint.x + radius, thetaPoint.y + radius);
                                Path thetaPath = new Path();
                                thetaPath.arcTo(ovalTheta, 225, 90, true);
                                canvas.drawPath(thetaPath,mThataPaint);
                            }

                            //draws the alpha half circle
                            if((numberOfCircles - centerCircleIndex) <=  mDeltaWaveAmplitude){
                                Point deltaPoint = new Point(halfX , halfY - centerCircleMarginY);
                                float radius = centerCircleRadius;
                                final RectF ovalDelta = new RectF();
                                ovalDelta.set(deltaPoint.x - radius, deltaPoint.y - radius, deltaPoint.x + radius, deltaPoint.y+ radius);
                                Path deltaPath = new Path();
                                deltaPath.arcTo(ovalDelta, 135, 90, true);
                                canvas.drawPath(deltaPath,mDeltaPaint);
                            }
                            centerCircleRadius = centerCircleRadius - 10;
                        }

                        //Alpha Tile
                        canvas.drawRect(centerCircleMarginX, 1200, maxX - centerCircleMarginX, 1500,framePaint);
                        canvas.drawRect(centerCircleMarginX, 1200, maxX - centerCircleMarginX, 1500,mWaveTilesPaint);

                        //Gamma Tile
                        canvas.drawRect(centerCircleMarginX, 1600, maxX - centerCircleMarginX, 1900,framePaint);
                        canvas.drawRect(centerCircleMarginX, 1600, maxX - centerCircleMarginX, 1900, mWaveTilesPaint);

                        //Theta Tile
                        canvas.drawRect(centerCircleMarginX, 2000, maxX - centerCircleMarginX, 2300,framePaint);
                        canvas.drawRect(centerCircleMarginX, 2000, maxX - centerCircleMarginX, 2300, mWaveTilesPaint);

                        //Theta Tile
                        canvas.drawRect(centerCircleMarginX, 2400, maxX - centerCircleMarginX, 2700,framePaint);
                        canvas.drawRect(centerCircleMarginX, 2400, maxX - centerCircleMarginX, 2700, mWaveTilesPaint);

                       //Draw the gamma wave
                        for (int i = centerCircleMarginX; i< maxX - centerCircleMarginX;i++){
                            int y = SketchWave(i,mGammaHertz, mGammaWaveCrestHeight, gammawaveStartY,maxX);
                            gammaWavePoints.add(new Point(i, y));
                            //amplitude hit change amplitude for the next period
                            if(y == mGammaWaveCrestHeight + gammawaveStartY) {
                                Log.d("Data", "Amplitude Reached the x value is" + i + "y value is" + y);
                                //Add a point to indicate where the wave has changed amplitude
                                gammaJumpPoints.add(new Point(i, y));
                                try{
                                    gammaWavePath.quadTo(gammaJumpPoints.get(gammaJumpPoints.size()).x,gammaJumpPoints.get(gammaJumpPoints.size() ).y,i+1,SketchWave(i,mGammaHertz, mGammaWaveCrestHeight - 20, gammawaveStartY,maxX));
                                    canvas.drawPath(gammaWavePath,mAlphaPaint);
                                    gammaWavePath.moveTo(i+1,SketchWave(i,mGammaHertz, mGammaWaveCrestHeight - 20, gammawaveStartY,maxX));
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                // canvas.drawLine(wavePoints.get(i).x,wavePoints.get(i).y,wavePoints.get(i).x,SketchWave(i,Hertz, crestHeight - 1,waveStartY,maxX),wavePaint);
                                if(gammaYdy < mBeatModel.mGammaWave.getWaveAmplitudeUpperBound()) {
                                    gammaYdy = gammaYdy + .8;
                                } //change in amplitude
                            }
                            canvas.drawPoint(i, y, mGammaPaint);
                        }

                        //Draw the theta wave
                        for (int i = centerCircleMarginX; i< maxX - centerCircleMarginX;i++){
                            int y = SketchWave(i,mThetaHertz, mThetaWaveCrestHeight,ThetawaveStartY,maxX);
                            thetaWavePoints.add(new Point(i, y));
                            //amplitude hit change amplitude for the next period
                            if(y == mThetaWaveCrestHeight + ThetawaveStartY) {
                                Log.d("Data", "Amplitude Reached the x value is" + i + "y value is" + y);
                                //Add a point to indicate where the wave has changed amplitude
                                thetaJumpPoints.add(new Point(i, y));
                                try{
                                    thetaWavePath.quadTo(thetaJumpPoints.get(thetaJumpPoints.size()).x,thetaJumpPoints.get(thetaJumpPoints.size() ).y,i+1,SketchWave(i,mThetaHertz, mThetaWaveCrestHeight - 20, ThetawaveStartY,maxX));
                                    canvas.drawPath(thetaWavePath, mThataPaint);
                                    thetaWavePath.moveTo(i + 1, SketchWave(i, mThetaHertz, mThetaWaveCrestHeight - 20, ThetawaveStartY, maxX));
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                // canvas.drawLine(wavePoints.get(i).x,wavePoints.get(i).y,wavePoints.get(i).x,SketchWave(i,Hertz, crestHeight - 1,waveStartY,maxX),wavePaint);
                                if(thetaYdy < mBeatModel.mThetaWave.getWaveAmplitudeUpperBound()) {
                                    thetaYdy = thetaYdy + .8;
                                } //change in amplitude
                            }
                            canvas.drawPoint(i, y, mThataPaint);
                        }

                        //Draw the delta wave
                        for (int i = centerCircleMarginX; i< maxX - centerCircleMarginX;i++){
                            int y = SketchWave(i,mDeltaHertz, mDeltaWaveCrestHeight,deltaWaveStartY,maxX);
                            deltaWavePoints.add(new Point(i, y));
                            //amplitude hit change amplitude for the next period
                            if(y ==  mDeltaWaveCrestHeight + deltaWaveStartY) {
                                Log.d("Data", "Amplitude Reached the x value is" + i + "y value is" + y);
                                //Add a point to indicate where the wave has changed amplitude
                                deltaJumpPoints.add(new Point(i, y));
                                try{
                                    deltaWavePath.quadTo(deltaJumpPoints.get(deltaJumpPoints.size()).x,deltaJumpPoints.get(deltaJumpPoints.size() ).y,i+1,SketchWave(i,mDeltaHertz, mDeltaWaveCrestHeight - 20, deltaWaveStartY,maxX));
                                    canvas.drawPath(deltaWavePath, mDeltaPaint);
                                    deltaWavePath.moveTo(i + 1, SketchWave(i, mDeltaHertz, mDeltaWaveCrestHeight - 20, deltaWaveStartY, maxX));
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                // canvas.drawLine(wavePoints.get(i).x,wavePoints.get(i).y,wavePoints.get(i).x,SketchWave(i,Hertz, crestHeight - 1,waveStartY,maxX),wavePaint);
                                if(deltaYdy < mBeatModel.mDeltaWave.getWaveAmplitudeUpperBound()) {
                                    deltaYdy = deltaYdy + .8;
                                } //change in amplitude
                            }
                            canvas.drawPoint(i, y, mDeltaPaint);
                        }

                        //Draw the alpha wave
                        for (int i = centerCircleMarginX; i< maxX - centerCircleMarginX; i++){
                            int y = SketchWave(i,mAlphaHertz, mAlphaWaveCrestHeight, alphwaveStartY,maxX);
                            alphaWavePoints.add(new Point(i, y));
                            //amplitude hit change amplitude for the next period
                            if(y == mAlphaWaveCrestHeight + alphwaveStartY) {
                                Log.d("Data", "Amplitude Reached the x value is" + i + "y value is" + y);
                                //Add a point to indicate where the wave has changed amplitude
                                alphaJumpPoints.add(new Point(i, y));
                                try{
                                    alphaWavePath.quadTo(alphaJumpPoints.get(alphaJumpPoints.size()).x,alphaJumpPoints.get(alphaJumpPoints.size() ).y,i+1,SketchWave(i,mAlphaHertz, mAlphaWaveCrestHeight - 20, alphwaveStartY,maxX));
                                    canvas.drawPath(alphaWavePath,mAlphaPaint);
                                    alphaWavePath.moveTo(i+1,SketchWave(i,mAlphaHertz, mAlphaWaveCrestHeight - 20, alphwaveStartY,maxX));
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                // canvas.drawLine(wavePoints.get(i).x,wavePoints.get(i).y,wavePoints.get(i).x,SketchWave(i,Hertz, crestHeight - 1,waveStartY,maxX),wavePaint);
                                if(alphaYdy < mBeatModel.mAlphaWave.getWaveAmplitudeUpperBound()) {
                                    alphaYdy = alphaYdy + .7;
                                } //change in amplitude
                            }
                            canvas.drawPoint(i, y, mAlphaPaint);
                        }
                    } finally {
                        if(mSurfaceHolder.getSurface() != null && canvas != null){
                            try {
                                mSurfaceHolder.unlockCanvasAndPost(canvas);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        private void InitDrawingParams() {

            mCircleLabelsPaint = new Paint(); //start paint params
            mCircleLabelsPaint.setTextSize(60);
            mCircleLabelsPaint.setColor(Color.BLACK);
            mCenterPaint = new Paint();
            mCenterPaint.setColor(Color.BLACK);
            mCenterPaint.setStyle(Paint.Style.STROKE);
            mCenterPaint.setStrokeWidth(2);
            mCenterPaint.setColor(Color.parseColor("#D8E1ED"));
            mCenterCircleFramePaint = new Paint();
            mCenterCircleFramePaint.setShadowLayer(4, 0, 0, Color.parseColor("#CFD8DC"));
            mCenterCircleFramePaint.setStyle(Paint.Style.STROKE);
            mCircleWaveHeaderPaint= new Paint();
            mCircleWaveHeaderPaint.setTextSize(100);
            mCircleWaveHeaderPaint.setColor(Color.BLACK);

            mWavePaint= new Paint();
            mWavePaint.setColor(Color.BLACK);
            mWavePaint.setStrokeWidth(4);
/*            mAlphaPaint.setColor(getResources().getColor(R.color.alpha_wave_color));
            mAlphaPaint.setStrokeWidth(4);
            mAlphaPaint.setTextSize(60);
            mGammaPaint.setColor(getResources().getColor(R.color.beta_wave_color));
            mGammaPaint.setStrokeWidth(4);
            mGammaPaint.setTextSize(60);
            mThataPaint.setColor(getResources().getColor(R.color.delta_wave_color));
            mThataPaint.setStrokeWidth(4);
            mThataPaint.setTextSize(60);
            mDeltaPaint.setColor(getResources().getColor(R.color.gamma_wave_color));*/
            mDeltaPaint.setStrokeWidth(4);
            mDeltaPaint.setTextSize(60);
            mWaveTilesPaint = new Paint();
            mWaveTilesPaint.setStyle(Paint.Style.STROKE);
            mWaveTilesPaint.setStrokeWidth(2);
            mWaveTilesPaint.setColor(Color.parseColor("#757575"));
            framePaint = new Paint();
            framePaint.setColor(Color.WHITE);
            framePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            framePaint.setShadowLayer(10, 6, 8, Color.BLACK);


            // end paint params
            mAlphaWaveAmplitude = mBeatModel.mAlphaWave.getWaveAmplitude(); //Start Alpha wave variables
            mAlphaHertz = mBeatModel.mAlphaWave.getWaveFrequency();
            mAlphaWaveCrestHeight = mBeatModel.mAlphaWave.getWaveAmplitudeUpperBound();

            mGammaWaveAmplitude =  mBeatModel.mGammaWave.getWaveAmplitude();  // start gamma wave variables
            mGammaHertz =  mBeatModel.mGammaWave.getWaveFrequency();
            mGammaWaveCrestHeight = mBeatModel.mGammaWave.getWaveAmplitudeUpperBound();

            mThetaWaveAmplitude = mBeatModel.mThetaWave.getWaveAmplitude(); // start theta wave variables
            mThetaWaveCrestHeight =  mBeatModel.mThetaWave.getWaveAmplitudeUpperBound();
            mThetaHertz =  mBeatModel.mThetaWave.getWaveFrequency();

            mDeltaWaveAmplitude = mBeatModel.mDeltaWave.getWaveAmplitude(); //start delta wave variables
            mDeltaWaveCrestHeight =  mBeatModel.mDeltaWave.getWaveAmplitudeUpperBound();
            mDeltaHertz =  mBeatModel.mDeltaWave.getWaveFrequency();


        }
        private void UpdateDrawingParams() {

            //Controls the amplitude increasing and decreasing

            //alpha increasing and decreasing
            //Upper Bound
            if(mAlphaWaveCrestHeight >= mBeatModel.mAlphaWave.getWaveAmplitudeUpperBound() && alphaIsIncreasing){
                alphaIsIncreasing = false;
            }
            //Lower Bound
            if(mAlphaWaveCrestHeight <= mBeatModel.mAlphaWave.getWaveAmplitudeLowerBOund() && !alphaIsIncreasing){
                alphaIsIncreasing = true;
            }
            //Increase Amplitude
            if(mAlphaWaveCrestHeight <= mBeatModel.mAlphaWave.getWaveAmplitudeUpperBound() && alphaIsIncreasing){
                mAlphaWaveAmplitude = increaseAmplitude(mAlphaWaveCrestHeight,alphaXdx);
            }
            //Deacrease Amplitude
            else if(mAlphaWaveCrestHeight >= mBeatModel.mAlphaWave.getWaveAmplitudeLowerBOund() && !alphaIsIncreasing) {
                mAlphaWaveAmplitude = decreaseAmplitude(mAlphaWaveCrestHeight,alphaXdx);
            }


            //Gamma increasing and decreasing
            //Upper Bound
            if(mGammaWaveCrestHeight >= mBeatModel.mGammaWave.getWaveAmplitudeUpperBound() && gammaIsIncreasing){
                gammaIsIncreasing = false;
            }
            //Lower Bound
            if(mGammaWaveCrestHeight <= mBeatModel.mGammaWave.getWaveAmplitudeLowerBOund() && !gammaIsIncreasing){
                gammaIsIncreasing = true;
            }
            //Increase Amplitude
            if(mGammaWaveCrestHeight <= mBeatModel.mGammaWave.getWaveAmplitudeUpperBound() && gammaIsIncreasing){
                mGammaWaveAmplitude = increaseAmplitude(mGammaWaveCrestHeight,gammaXdx);
            }
            //Deacrease Amplitude
            else if(mGammaWaveCrestHeight >= mBeatModel.mAlphaWave.getWaveAmplitudeLowerBOund() && !gammaIsIncreasing) {
                mGammaWaveAmplitude = decreaseAmplitude(mGammaWaveCrestHeight,gammaXdx);
            }

            // /Theta increasing and decreasing
            //Upper Bound
            if(mThetaWaveCrestHeight >= mBeatModel.mThetaWave.getWaveAmplitudeUpperBound() && thetaIsIncreasing){
                thetaIsIncreasing = false;
            }
            //Lower Bound
            if(mThetaWaveCrestHeight <= mBeatModel.mThetaWave.getWaveAmplitudeLowerBOund() && !thetaIsIncreasing){
                thetaIsIncreasing = true;
            }
            //Increase Amplitude
            if(mThetaWaveCrestHeight <= mBeatModel.mThetaWave.getWaveAmplitudeUpperBound() && thetaIsIncreasing){
                mThetaWaveAmplitude = increaseAmplitude(mThetaWaveCrestHeight,thetaXdx);
            }
            //Deacrease Amplitude
            else if(mThetaWaveCrestHeight >= mBeatModel.mThetaWave.getWaveAmplitudeLowerBOund() && !thetaIsIncreasing) {
                mThetaWaveAmplitude = decreaseAmplitude(mThetaWaveCrestHeight,thetaXdx);
            }


            // /Delta increasing and decreasing
            //Upper Bound
            if(mDeltaWaveCrestHeight >= mBeatModel.mDeltaWave.getWaveAmplitudeUpperBound() && deltaIsIncreasing){
                deltaIsIncreasing = false;
            }
            //Lower Bound
            if(mDeltaWaveCrestHeight <= mBeatModel.mDeltaWave.getWaveAmplitudeLowerBOund() && !deltaIsIncreasing){
                deltaIsIncreasing = true;
            }
            //Increase Amplitude
            if(mDeltaWaveCrestHeight <= mBeatModel.mDeltaWave.getWaveAmplitudeUpperBound() && deltaIsIncreasing){
                mDeltaWaveAmplitude = increaseAmplitude(mDeltaWaveCrestHeight,deltaXdx);
            }
            //Deacrease Amplitude
            else if(mDeltaWaveCrestHeight >= mBeatModel.mDeltaWave.getWaveAmplitudeLowerBOund() && !deltaIsIncreasing) {
                mDeltaWaveAmplitude = decreaseAmplitude(mDeltaWaveCrestHeight,deltaXdx);
            }
        }
    }

    int SketchWave(int x, double Hertz, double Amplitude, double waveStartY, int maxX) {
        double piDouble = 2 * Math.PI;
        double factor = piDouble / Hertz;
        return (int) (Math.sin(x * factor) * Amplitude + waveStartY);
    }
    double getCircleAlphaY(double alphaydy){
        return alphaydy;
    }

    public double increaseAmplitude(double Amplitude, double xdx){
        double amp = Amplitude;
        amp = amp + xdx;
        return amp;
    }

    public double decreaseAmplitude(double Amplitude, double xdx){
        double amp = Amplitude;
        amp = amp - xdx;
        return amp;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Starts thread execution
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Create a Bitmap with the dimensions of the View
        mBitmap =  Bitmap.createBitmap (width, height, Bitmap.Config.ARGB_8888);
        thread.isInterrupted();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Finish thread execution
        _run = false;
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
