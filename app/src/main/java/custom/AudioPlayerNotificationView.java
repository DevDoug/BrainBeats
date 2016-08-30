package custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.brainbeats.R;

/**
 * Created by douglas on 8/24/2016.
 * Custom view to allow the user to return to their music from the application
 */
public class AudioPlayerNotificationView extends View {

    public Paint mBackgroundPaint;

    public AudioPlayerNotificationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackgroundPaint = new Paint();
        //mBackgroundPaint.setColor(getResources(R.color.theme_accent_color));
        mBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mBackgroundPaint); //draw background
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
