package io.github.rhemon.aggregatesieveanalysis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.jar.Attributes;
/**
 * Created by rhemon on 9/30/16.
 */
public class SemiLogGraph extends View {

    final Paint _paint = new Paint();
    JSONObject _XYData = null;
    final String LOG_TAG = getClass().getSimpleName();
    double D60, D10;

    public SemiLogGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SemiLogGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SemiLogGraph(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final float ScreenWidth = getWidth();
        final float ScreenHeight = getHeight();
		final float _StartPointX = 80;
		final float _StartPointY = 20;
		final float _EndPointX = ScreenWidth - 15;
		final float _EndPointY = ScreenHeight - 45;
		final float GraphWidth = _EndPointX - _StartPointX;
		final float GraphHeight = _EndPointY - _StartPointY;
        final float SectionWidth = GraphWidth/4;
        final float SectionHeight = GraphHeight/10;

        _paint.setAntiAlias(true);
        _paint.setColor(getResources().getColor(R.color.BLACK));
        _paint.setStrokeWidth(3);

        _paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));

		// Axis lines
		canvas.drawLine(_StartPointX, _StartPointY, _StartPointX, _EndPointY, _paint);
		canvas.drawLine(_StartPointX, _EndPointY, _EndPointX, _EndPointY, _paint);
		// Cords labels
		canvas.drawText("10", _StartPointX-15, _EndPointY+35, _paint);
		canvas.drawText("0", _StartPointX-30, _EndPointY+5, _paint);

		_paint.setStrokeWidth(1);

        // Logarithmic scaled X Lines
        int at = 1;
        double XPoint = _StartPointX;
        double PresXPoint = _StartPointX;
        float value = 10;
        for (int i=0; i<4; i++) {
            _paint.setColor(getResources().getColor(R.color.GRAY400));
            for (int j=0; j<9; j++) {
                PresXPoint = XPoint + (SectionWidth - (Math.log10(at)*SectionWidth));
                canvas.drawLine((float) PresXPoint, _StartPointY, (float) PresXPoint, _EndPointY, _paint);
                at += 1;
            }
            PresXPoint = XPoint + (SectionWidth - (Math.log10(at)*SectionWidth));
            _paint.setStrokeWidth(3);
            _paint.setColor(getResources().getColor(R.color.GRAY600));
            canvas.drawLine((float) PresXPoint, _StartPointY, (float) PresXPoint, _EndPointY, _paint);
            if (i > 0) {
                value /= 10;
                _paint.setColor(getResources().getColor(R.color.BLACK));
                canvas.drawText(String.valueOf(value), (float) PresXPoint-18, _EndPointY+35, _paint);
            }
            at = 1;
            XPoint += Math.log10(10)*SectionWidth;
        }

        // Linear scaled Y Lines
        float PresYPoint = _EndPointY;
        value = 0;
        _paint.setStrokeWidth(2);
        _paint.setColor(getResources().getColor(R.color.GRAY400));
        for (int i=0; i<10; i++) {
            PresYPoint -= SectionHeight;
            value += 10;
            _paint.setColor(getResources().getColor(R.color.GRAY400));
            canvas.drawLine(_StartPointX, PresYPoint, _EndPointX, PresYPoint, _paint);
            _paint.setColor(getResources().getColor(R.color.BLACK));
            canvas.drawText(String.valueOf(value), _StartPointX - 79, PresYPoint + 9, _paint);
        }

        // Plotting Values
        _paint.setStrokeWidth(2);
        _paint.setColor(getResources().getColor(R.color.colorLightBlue700));

        if (_XYData != null) {
            try {
                float StartPointX = _StartPointX;
                float StartPointY = _EndPointY;
                JSONArray XData = _XYData.getJSONArray("xData");
                JSONArray YData = _XYData.getJSONArray("yData");

                float prevXPoint = Float.parseFloat(XData.get(0).toString());
                prevXPoint = (float) (StartPointX + SectionWidth - (Math.log10(prevXPoint) * SectionWidth));
                float prevYPoint = Float.parseFloat(YData.get(0).toString());
                prevYPoint = (float) (StartPointY - (SectionHeight * prevYPoint/10));
                float presXPoint;
                float presYPoint;
                for (int i = 1; i < XData.length(); i++){
                    presXPoint = Float.parseFloat(XData.get(i).toString());
                    presYPoint = Float.parseFloat(YData.get(i).toString());
                    if (presXPoint >= 1) {
                        StartPointX = _StartPointX;
                    } else if (presXPoint >= 0.1) {
                        StartPointX = (float) (_StartPointX +(Math.log10(10) * SectionWidth));
                        presXPoint *= 10;
                    } else if (presXPoint >= 0.01) {
                        StartPointX = (float) (_StartPointX + (Math.log10(100) * SectionWidth));
                        presXPoint *= 100;
                    }
                    presXPoint = (float) (StartPointX + (SectionWidth - (Math.log10(presXPoint) * SectionWidth)));
                    presYPoint = (StartPointY - (SectionHeight * presYPoint/10));

                    // Finding D60 and D10
                    float FROM = _StartPointX;
                    if (Float.parseFloat(YData.get(i-1).toString()) >= 60 &&
                            Float.parseFloat(YData.get(i).toString()) <= 60) {
                        float GradientOfSegment = (presYPoint-prevYPoint)/(presXPoint-prevXPoint);
                        float x = ((StartPointY-(SectionHeight*6) - prevYPoint)/GradientOfSegment) + prevXPoint;
                        double divisonBy = 1;
                        if (x > _StartPointX+(Math.log10(10)*SectionWidth) &&
                                x < _StartPointX+(Math.log10(100) * SectionWidth)) {
                            FROM = (float) (_StartPointX + (Math.log10(10) * SectionWidth));
                            divisonBy = 10;
                        } else if (x > _StartPointX+(Math.log10(100) * SectionWidth) &&
                                x < _StartPointX+(Math.log10(1000) * SectionWidth)) {
                            FROM = (float) (_StartPointX + (Math.log10(100) * SectionWidth));
                            divisonBy = 100;
                        }
                        D60 = Math.pow(10, (((FROM + SectionWidth - x)/SectionWidth)))/divisonBy;
                        Log.d(LOG_TAG, String.format("%.2f", D60));
                    } else if (Float.parseFloat(YData.get(i-1).toString()) >= 10 &&
                            Float.parseFloat(YData.get(i).toString()) <= 10) {
                        float GradientOfSegment = (presYPoint-prevYPoint)/(presXPoint-prevXPoint);
                        float x = ((StartPointY-(SectionHeight*1) - prevYPoint)/GradientOfSegment) + prevXPoint;
                        double divisonBy = 1;
                        if (x > _StartPointX+(Math.log10(10)*SectionWidth) &&
                                x < _StartPointX+(Math.log10(100) * SectionWidth)) {
                            FROM = (float) (_StartPointX + (Math.log10(10) * SectionWidth));
                            divisonBy = 10;
                        } else if (x > _StartPointX+(Math.log10(100) * SectionWidth) &&
                                x < _StartPointX+(Math.log10(1000) * SectionWidth)) {
                            FROM = (float) (_StartPointX + (Math.log10(100) * SectionWidth));
                            divisonBy = 100;
                        }
                        D10 = Math.pow(10, (((FROM + SectionWidth - x)/SectionWidth)))/divisonBy;
                        Log.d(LOG_TAG, String.format("%.2f", D10));
                    }
                    canvas.drawLine(prevXPoint, prevYPoint, presXPoint, presYPoint, _paint);
                    prevXPoint = presXPoint;
                    prevYPoint = presYPoint;
                }

            } catch (JSONException e) {
                Log.d(LOG_TAG, "JSONException occured in SemiLogGraph.java");
            }
            try {
                ((ResultActivity) getContext()).set_resultData(D60, D10);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setData(JSONObject data) {
        this._XYData = data;
        invalidate();
    }
}
