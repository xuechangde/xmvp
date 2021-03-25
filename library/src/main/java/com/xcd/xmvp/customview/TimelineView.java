package com.xcd.xmvp.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.blankj.utilcode.util.SizeUtils;
import com.xcd.xmvp.R;

/**
 * Created by Vipul Asri on 05-12-2015.
 */
public class TimelineView extends View {

    public static final String TAG = TimelineView.class.getSimpleName();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineOrientation.HORIZONTAL, LineOrientation.VERTICAL})
    public @interface LineOrientation {
        int HORIZONTAL = 0;
        int VERTICAL = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineType.NORMAL, LineType.START, LineType.END, LineType.ONLYONE})
    private @interface LineType {
        int NORMAL = 0;
        int START = 1;
        int END = 2;
        int ONLYONE = 3;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineStyle.NORMAL, LineStyle.DASHED})
    public @interface LineStyle {
        int NORMAL = 0;
        int DASHED = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineRound.NORMAL, LineRound.ROUND})
    public @interface LineRound {
        int NORMAL = 0;
        int ROUND = 1;
    }

    private Drawable mMarker;
    private int mMarkerSize;
    private int mMarkerPaddingLeft;
    private int mMarkerPaddingTop;
    private int mMarkerPaddingRight;
    private int mMarkerPaddingBottom;
    private boolean mMarkerInCenter;
    private Paint mLinePaint = new Paint();
    private boolean mDrawStartLine = false;
    private boolean mDrawEndLine = false;
    private float mStartLineStartX, mStartLineStartY, mStartLineStopX, mStartLineStopY;
    private float mEndLineStartX, mEndLineStartY, mEndLineStopX, mEndLineStopY;
    private int mStartLineColor;
    private int mEndLineColor;
    private int mLineWidth;
    private int mLineOrientation;
    private int mLineStyle;
    private int mLineStyleDashLength;
    private int mLineStyleDashGap;
    private int mLineStyleRound;
    private int mLinePadding;
    private int mLinePosition;

    private Rect mBounds;

    public TimelineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TimelineView);
        mMarker = typedArray.getDrawable(R.styleable.TimelineView_marker);
        mMarkerSize = typedArray.getDimensionPixelSize(R.styleable.TimelineView_markerSize, SizeUtils.dp2px(20));
        mMarkerPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.TimelineView_markerPaddingLeft, 0);
        mMarkerPaddingTop = typedArray.getDimensionPixelSize(R.styleable.TimelineView_markerPaddingTop, 0);
        mMarkerPaddingRight = typedArray.getDimensionPixelSize(R.styleable.TimelineView_markerPaddingRight, 0);
        mMarkerPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.TimelineView_markerPaddingBottom, 0);
        mMarkerInCenter = typedArray.getBoolean(R.styleable.TimelineView_markerInCenter, true);
        mStartLineColor = typedArray.getColor(R.styleable.TimelineView_startLineColor, getResources().getColor(android.R.color.darker_gray));
        mEndLineColor = typedArray.getColor(R.styleable.TimelineView_endLineColor, getResources().getColor(android.R.color.darker_gray));
        mLineWidth = typedArray.getDimensionPixelSize(R.styleable.TimelineView_lineWidth, SizeUtils.dp2px(2));
        mLineOrientation = typedArray.getInt(R.styleable.TimelineView_lineOrientation, LineOrientation.VERTICAL);
        mLinePadding = typedArray.getDimensionPixelSize(R.styleable.TimelineView_linePadding, 0);
        mLineStyle = typedArray.getInt(R.styleable.TimelineView_lineStyle, LineStyle.NORMAL);
        mLineStyleDashLength = typedArray.getDimensionPixelSize(R.styleable.TimelineView_lineStyleDashLength, SizeUtils.dp2px(8f));
        mLineStyleDashGap = typedArray.getDimensionPixelSize(R.styleable.TimelineView_lineStyleDashGap,SizeUtils.dp2px(4f));
        mLineStyleRound = typedArray.getInt(R.styleable.TimelineView_lineStyleRound, LineStyle.NORMAL);
        typedArray.recycle();

        if(isInEditMode()) {
            mDrawStartLine = true;
            mDrawEndLine = true;
        }

        if(mMarker == null) {
            mMarker = getResources().getDrawable(R.drawable.marker);
        }

        initTimeline();
        initLinePaint();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Width measurements of the width and height and the inside view of child controls
        int w = mMarkerSize + getPaddingLeft() + getPaddingRight();
        int h = mMarkerSize + getPaddingTop() + getPaddingBottom();

        // Width and height to determine the final view through a systematic approach to decision-making
        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);

        setMeasuredDimension(widthSize, heightSize);
        initTimeline();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        initTimeline();
    }

    private void initTimeline() {

        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        int width = getWidth();// Width of current custom view
        int height = getHeight();

        int cWidth = width - pLeft - pRight;// Circle width
        int cHeight = height - pTop - pBottom;

        int markSize = Math.min(mMarkerSize, Math.min(cWidth, cHeight));

        if(mMarkerInCenter) { //Marker in center is true
            int left = (width/2) - (markSize/2);
            int top = (height/2) - (markSize/2);
            int right = (width/2) + (markSize/2);
            int bottom = (height/2) + (markSize/2);

            switch (mLineOrientation) {
                case LineOrientation.HORIZONTAL: {
                    left += mMarkerPaddingLeft - mMarkerPaddingRight;
                    right += mMarkerPaddingLeft - mMarkerPaddingRight;
                    break;
                }
                case LineOrientation.VERTICAL: {
                    top += mMarkerPaddingTop - mMarkerPaddingBottom;
                    bottom += mMarkerPaddingTop - mMarkerPaddingBottom;
                    break;
                }
            }

            if(mMarker != null) {
                mMarker.setBounds(left, top, right, bottom);
                mBounds = mMarker.getBounds();
            }

        } else { //Marker in center is false

            int left = pLeft;
            int top = pTop;
            int right = pLeft + markSize;
            int bottom = pTop;

            switch (mLineOrientation) {
                case LineOrientation.HORIZONTAL: {
                    top = (height/2) - (markSize/2);
                    bottom = (height/2) + (markSize/2);
                    left += mMarkerPaddingLeft - mMarkerPaddingRight;
                    right += mMarkerPaddingLeft - mMarkerPaddingRight;
                    break;
                }
                case LineOrientation.VERTICAL: {
                    top += mMarkerPaddingTop - mMarkerPaddingBottom;
                    bottom += markSize + mMarkerPaddingTop - mMarkerPaddingBottom;
                    break;
                }
            }

            if(mMarker != null) {
                mMarker.setBounds(left, top, right, bottom);
                mBounds = mMarker.getBounds();
            }
        }

        if (mLineOrientation == LineOrientation.HORIZONTAL) {

            if (mDrawStartLine) {
                mStartLineStartX = pLeft;
                mStartLineStartY = mBounds.centerY();
                mStartLineStopX = mBounds.left - mLinePadding;
                mStartLineStopY = mBounds.centerY();
            }

            if (mDrawEndLine) {
                if (mLineStyle == LineStyle.DASHED) {
                    mEndLineStartX = getWidth() - mLineStyleDashGap;
                    mEndLineStartY = mBounds.centerY();
                    mEndLineStopX = mBounds.right + mLinePadding;
                    mEndLineStopY = mBounds.centerY();
                } else {
                    mEndLineStartX = mBounds.right + mLinePadding;
                    mEndLineStartY = mBounds.centerY();
                    mEndLineStopX = getWidth();
                    mEndLineStopY = mBounds.centerY();
                }
            }
        } else {

            if (mDrawStartLine) {
                mStartLineStartX = mBounds.centerX();
                mStartLineStartY = pTop;
                mStartLineStopX = mBounds.centerX();
                mStartLineStopY = mBounds.top - mLinePadding;
            }

            if (mDrawEndLine) {
                if (mLineStyle == LineStyle.DASHED) {
                    mEndLineStartX = mBounds.centerX();
                    mEndLineStartY = getHeight() - mLineStyleDashGap;
                    mEndLineStopX = mBounds.centerX();
                    mEndLineStopY = mBounds.bottom + mLinePadding;
                } else {
                    mEndLineStartX = mBounds.centerX();
                    mEndLineStartY = mBounds.bottom + mLinePadding;
                    mEndLineStopX = mBounds.centerX();
                    mEndLineStopY = getHeight();
                }
            }
        }

        invalidate();
    }

    private void initLinePaint() {
        mLinePaint.setAlpha(0);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mStartLineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mLineWidth);
        if(mLineStyleRound == LineRound.ROUND){
            mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        }

        if (mLineStyle == LineStyle.DASHED) {
            mLinePaint.setPathEffect(new DashPathEffect(new float[]{(float) mLineStyleDashLength, (float) mLineStyleDashGap}, 0.0f));
        }else {
            mLinePaint.setPathEffect(new PathEffect());
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mMarker != null) {
            mMarker.draw(canvas);
        }
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(SizeUtils.sp2px(14f));
        paint.setFakeBoldText(true);
        drawTextWithCenterPoint(canvas,mBounds.centerX(),mBounds.centerY(),mLinePosition+"",paint);
        if(mDrawStartLine) {
            mLinePaint.setColor(mStartLineColor);
            canvas.drawLine(mStartLineStartX, mStartLineStartY, mStartLineStopX, mStartLineStopY, mLinePaint);
        }

        if(mDrawEndLine) {
            mLinePaint.setColor(mEndLineColor);
            canvas.drawLine(mEndLineStartX, mEndLineStartY, mEndLineStopX, mEndLineStopY, mLinePaint);
        }
    }

    /**
     * Sets marker.
     *
     * @param marker will set marker drawable to timeline
     */
    public void setMarker(Drawable marker) {
        mMarker = marker;
        initTimeline();
    }

    public Drawable getMarker() {
        return mMarker;
    }

    /**
     * Sets marker.
     *
     * @param marker will set marker drawable to timeline
     * @param color  with a color
     */
    public void setMarker(Drawable marker, int color) {
        mMarker = marker;
        mMarker.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        initTimeline();
    }

    /**
     * Sets marker color.
     *
     * @param color the color
     */
    public void setMarkerColor(int color) {
        mMarker.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        initTimeline();
    }

    /**
     * Sets start line.
     *
     * @param color    the color of the start line
     * @param viewType the view type
     */
    public void setStartLineColor(int color, int viewType,int position) {
        mStartLineColor = color;
        initLine(viewType,position);
    }

    public int getStartLineColor() {
        return mStartLineColor;
    }

    /**
     * Sets end line.
     *
     * @param color    the color of the end line
     * @param viewType the view type
     */
    public void setEndLineColor(int color, int viewType,int position) {
        mEndLineColor = color;
        initLine(viewType,position);
    }

    public int getEndLineColor() {
        return mEndLineColor;
    }

    /**
     * Sets marker size.
     *
     * @param markerSize the marker size
     */
    public void setMarkerSize(int markerSize) {
        mMarkerSize = markerSize;
        initTimeline();
    }

    public int getMarkerSize() {
        return mMarkerSize;
    }

    /**
     * Sets marker left padding
     * @param markerPaddingLeft the left padding to marker, works only in vertical orientation
     */
    public void setMarkerPaddingLeft(int markerPaddingLeft) {
        mMarkerPaddingLeft = markerPaddingLeft;
        initTimeline();
    }

    public int getMarkerPaddingLeft() {
        return mMarkerPaddingLeft;
    }

    /**
     * Sets marker top padding
     * @param markerPaddingTop the top padding to marker, works only in horizontal orientation
     */
    public void setMarkerPaddingTop(int markerPaddingTop) {
        mMarkerPaddingTop = markerPaddingTop;
        initTimeline();
    }

    public int getMarkerPaddingTop() {
        return mMarkerPaddingTop;
    }

    /**
     * Sets marker right padding
     * @param markerPaddingRight the right padding to marker, works only in vertical orientation
     */
    public void setMarkerPaddingRight(int markerPaddingRight) {
        mMarkerPaddingRight = markerPaddingRight;
        initTimeline();
    }

    public int getMarkerPaddingRight() {
        return mMarkerPaddingRight;
    }

    /**
     * Sets marker bottom padding
     * @param markerPaddingBottom the bottom padding to marker, works only in horizontal orientation
     */
    public void setMarkerPaddingBottom(int markerPaddingBottom) {
        mMarkerPaddingBottom = markerPaddingBottom;
        initTimeline();
    }

    public int getMarkerPaddingBottom() {
        return mMarkerPaddingBottom;
    }

    public boolean isMarkerInCenter() {
        return mMarkerInCenter;
    }

    /**
     * Sets marker in center
     * @param markerInCenter the marker position
     */
    public void setMarkerInCenter(boolean markerInCenter) {
        this.mMarkerInCenter = markerInCenter;
        initTimeline();
    }

    /**
     * Sets line width.
     *
     * @param lineWidth the line width
     */
    public void setLineWidth(int lineWidth) {
        mLineWidth = lineWidth;
        initTimeline();
    }

    public int getLineWidth() {
        return mLineWidth;
    }

    /**
     * Sets line padding
     * @param padding the line padding
     */
    public void setLinePadding(int padding) {
        mLinePadding = padding;
        initTimeline();
    }

    public int getLineOrientation() {
        return mLineOrientation;
    }

    /**
     * Sets line orientation
     * @param lineOrientation the line orientation i.e horizontal or vertical
     */
    public void setLineOrientation(int lineOrientation) {
        this.mLineOrientation = lineOrientation;
    }

    public int getLineStyle() {
        return mLineStyle;
    }

    /**
     * Sets line style
     * @param lineStyle the line style i.e normal or dashed
     */
    public void setLineStyle(int lineStyle) {
        this.mLineStyle = lineStyle;
        initLinePaint();
    }

    public int getLineStyleDashLength() {
        return mLineStyleDashLength;
    }

    /**
     * Sets dashed line length
     * @param lineStyleDashLength the dashed line length
     */
    public void setLineStyleDashLength(int lineStyleDashLength) {
        this.mLineStyleDashLength = lineStyleDashLength;
        initLinePaint();
    }

    public int getLineStyleDashGap() {
        return mLineStyleDashGap;
    }

    /**
     * Sets dashed line gap
     * @param lineStyleDashGap the dashed line gap
     */
    public void setLineStyleDashGap(int lineStyleDashGap) {
        this.mLineStyleDashGap = lineStyleDashGap;
        initLinePaint();
    }

    public int getLinePadding() {
        return mLinePadding;
    }

    private void showStartLine(boolean show) {
        mDrawStartLine = show;
    }

    private void showEndLine(boolean show) {
        mDrawEndLine = show;
    }

    /**
     * Init line.
     *
     * @param viewType the view type
     */
    public void initLine(int viewType,int position) {
        if(viewType == LineType.START) {
            showStartLine(false);
            showEndLine(true);
        } else if(viewType == LineType.END) {
            showStartLine(true);
            showEndLine(false);
        } else if(viewType == LineType.ONLYONE) {
            showStartLine(false);
            showEndLine(false);
        } else {
            showStartLine(true);
            showEndLine(true);
        }
        mLinePosition = position;
        initTimeline();
    }

    /**
     * Gets timeline view type.
     *
     * @param position the position of current item view
     * @param totalSize the total size of the items
     * @return the timeline view type
     */
    public static int getTimeLineViewType(int position, int totalSize) {
        if(totalSize == 1) {
            return LineType.ONLYONE;
        } else if(position == 0) {
            return LineType.START;
        } else if(position == totalSize - 1) {
            return LineType.END;
        } else {
            return LineType.NORMAL;
        }
    }

    /**
     * 以中心点绘制文字
     *
     * @param canvas
     * @param centerX
     * @param centerY
     * @param text
     * @param paint
     */
    private void drawTextWithCenterPoint(Canvas canvas, int centerX, int centerY, String text, Paint paint) {
        //获取文本的宽度，但是是一个比较粗略的结果
        float textWidth = paint.measureText(text);
        //文字度量
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        //得到基线的位置
        float baselineY = centerY + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        //绘制
        canvas.drawText(text, centerX - textWidth / 2, baselineY, paint);
    }
}
