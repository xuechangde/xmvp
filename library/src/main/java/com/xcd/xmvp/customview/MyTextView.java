package com.xcd.xmvp.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.xcd.xmvp.R;

public class MyTextView extends View {
    //内容填充画笔
    private Paint contentPaint;
    //标准的字体颜色
    private String contentNormalColor = "#737373";
    //有焦点的字体颜色
    private String contentFocuedColor = "#333333";
    //控件宽度
    private int viewWidth = 0;
    //控件高度
    private int viewHeight = 0;
    //标准的字的样式
    public static final int TEXT_TYPE_NORMAL = 1;
    //控件获取焦点的时候进行的处理
    public static final int TEXT_TYPE_FOCUED = 2;
    //默认是标准的样式
    private int currentTextType = TEXT_TYPE_NORMAL;
    //默认当前的颜色
    private String textColor = "#444444";
    //字体大小
    private int textSize = 60;
    //内容
    private String mText = "";
    //最小view高度
    private float minHeight = 0;
    //最小view宽度
    private float minWidth = 0;
    //行间距
    private float lineSpace;
    //最大行数
    private int maxLines = 0;
    //文字测量工具
    private Paint.FontMetricsInt textFm;
    ///真实的行数
    private int realLines;
    //当前显示的行数
    private int line;
    //在末尾是否显示省略号
    private boolean showEllipsise;

    //文字显示区的宽度
    private int textWidth;
    //单行文字的高度
    private int signleLineHeight;
    private int mPaddingLeft,mPaddingRight,mPaddingTop,mPaddingBottom;
    /**
     * 省略号
     **/
    private String ellipsis = "...";
    public MyTextView(Context context) {
        this(context,null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context,attrs);
        init();
    }
    private boolean isFristload;

    /**
     * 初始化
     */
    private void init() {
        contentPaint = new Paint();
        contentPaint.setTextSize(textSize);
        contentPaint.setAntiAlias(true);
        contentPaint.setStrokeWidth(2);
        contentPaint.setColor(Color.parseColor(textColor));
        contentPaint.setTextAlign(Paint.Align.LEFT);
        textFm = contentPaint.getFontMetricsInt();
        signleLineHeight= Math.abs(textFm.top-textFm.bottom);
    }

    /**
     * 初始化属性
     * @param context
     * @param attrs
     */
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        mPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MyTextView_paddingLeft, 0);
        mPaddingRight = typedArray.getDimensionPixelSize(R.styleable.MyTextView_paddingRight, 0);
        mPaddingTop = typedArray.getDimensionPixelSize(R.styleable.MyTextView_paddingTop, 0);
        mPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MyTextView_paddingBottom, 0);

        mText = typedArray.getString(R.styleable.MyTextView_text);
        textColor = typedArray.getString(R.styleable.MyTextView_textColor);
        if(textColor==null){
            textColor="#444444";
        }
        //   textSize = typedArray.getDimensionPixelSize(R.styleable.MyTextView_textSize, 60);

        textSize = typedArray.getDimensionPixelSize(R.styleable.MyTextView_textSize, SizeUtils.sp2px(15));
        lineSpace = typedArray.getInteger(R.styleable.MyTextView_lineSpacing1, 20);
        typedArray.recycle();
    }

    public void setText(String ss){
        this.mText=ss;
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        viewWidth=getMeasuredWidth();
        textWidth=viewWidth-mPaddingLeft-mPaddingRight;
        viewHeight= (int) getViewHeight();
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private float getViewHeight() {
        char[] textChars=mText.toCharArray();
        float ww=0.0f;
        int count=0;
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<textChars.length;i++){
            float v = contentPaint.measureText(textChars[i] + "");
            if(ww+v<=textWidth){
                sb.append(textChars[i]);
                ww+=v;
            }
            else{
                count++;
                sb=new StringBuilder();
                ww=0.0f;
                ww+=v;
                sb.append(textChars[i]);
            }
        }
        if(sb.toString().length()!=0){
            count++;
        }
        return count*signleLineHeight+lineSpace*(count-1)+mPaddingBottom+mPaddingTop;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawText(canvas);
    }

    /**
     * 循环遍历画文字
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        char[] textChars=mText.toCharArray();
        float ww=0.0f;
        float startL=0.0f;
        float startT=0.0f;
        startL+=mPaddingLeft;
        startT+=mPaddingTop+signleLineHeight/2+ (textFm.bottom-textFm.top)/2 - textFm.bottom;
        StringBuilder sb=new StringBuilder();

        for(int i=0;i<textChars.length;i++){
            float v = contentPaint.measureText(textChars[i] + "");
            if(ww+v<=textWidth){
                sb.append(textChars[i]);
                ww+=v;
            }
            else{
                canvas.drawText(sb.toString(),startL,startT,contentPaint);
                startT+=signleLineHeight+lineSpace;
                sb=new StringBuilder();
                ww=0.0f;
                ww+=v;
                sb.append(textChars[i]);
            }
        }

        if(sb.toString().length()>0){
            canvas.drawText(sb.toString(),startL,startT,contentPaint);
        }
    }
}
