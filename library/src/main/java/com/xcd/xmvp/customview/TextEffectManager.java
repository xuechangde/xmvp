package com.xcd.xmvp.customview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class TextEffectManager {
    /**
     int flags：取值有如下四个
     Spannable.SPAN_EXCLUSIVE_EXCLUSIVE ：前后都不包括，即在指定范围的前面和后面插入新字符都不会应用新样式
     Spannable.SPAN_EXCLUSIVE_INCLUSIVE ：前面不包括，后面包括。即仅在范围字符的后面插入新字符时会应用新样式
     Spannable.SPAN_INCLUSIVE_EXCLUSIVE ：前面包括，后面不包括。
     Spannable.SPAN_INCLUSIVE_INCLUSIVE ：前后都包括。
     */

    /**
     * 修改字体颜色
     *
     * @param textView
     * @param text
     * @param start    其实下标，包括
     * @param end      结束下标，不包括
     * @param color
     */
    public static void makeTextColor(TextView textView, String text, int start, int end, int color) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spanString);
    }

    //修改部分文字背景色
    public static void makeTextBg(TextView textView, String text, int start, int end, int color) {
        SpannableString spanString = new SpannableString(text);
        BackgroundColorSpan span = new BackgroundColorSpan(color);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spanString);
    }

    //设置部分文字字体大小
    public static void makeTextSize(TextView textView, String text, int start, int end, int size) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spanString);
    }

    //设置部分文字字体样式
    public static void makeTextStyle(TextView textView, String text, int start, int end, int style) {
        SpannableString spanString = new SpannableString(text);
        StyleSpan span = new StyleSpan(style);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spanString);
    }

    //设置部分文字字体style 引用style资源文件
    public static void makeTextStyle2(Context context, TextView textView, String text, int start, int end, int style) {
        SpannableString spanString = new SpannableString(text);
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(context,style);
        spanString.setSpan(textAppearanceSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spanString);
    }

    /**
     * 设置下划线
     *
     * @param textView
     */
    public static void makeUnderLine(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    //设置部分文字下划线
    public static void makeUnderLine(TextView textView, String text, int start, int end) {
        SpannableString spanString = new SpannableString(text);
        UnderlineSpan span = new UnderlineSpan();
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spanString);
    }

    //设置中划线
    //priceOldTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    //设置部分文字中划线
    public static void makeStrikethrough(TextView textView, String text, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new StrikethroughSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    public static void makeTextSizeColor(TextView textView, String text, int start, int end, int color, int size) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spanString);
    }

    /**
     * 处理html文本超链接点击事件
     * @param context
     * @param tv
     */
    public static void textHtmlClick(Context context, TextView tv, String color, boolean showLine) {
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        int end = text.length();
        Spannable sp = (Spannable) text;
        URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.clearSpans();// should clear old spans
        for (URLSpan url : urls) {
            MyURLSpan myURLSpan = new MyURLSpan(context, url.getURL(), color, showLine);
            style.setSpan(myURLSpan, sp.getSpanStart(url),
                    sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(style);
    }

    private static class MyURLSpan extends ClickableSpan {
        String color;
        boolean showLine;
        Context context;
        String url;

        /**
         * @param context
         * @param url      链接
         * @param color    超链接文本颜色
         * @param showLine 是否显示下划线
         */
        MyURLSpan(Context context, String url, String color, boolean showLine) {
            this.context = context;
            this.url = url;
            this.color = color;
            this.showLine = showLine;
        }

        @Override
        public void onClick(View widget) {
            if (!TextUtils.isEmpty(url)) {
                Intent intent = new Intent();
                intent.setData(Uri.parse(url));//Url 就是你要打开的网址
                intent.setAction(Intent.ACTION_VIEW);
                context.startActivity(intent); //启动浏览器
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (!TextUtils.isEmpty(color)) {
                ds.setColor(Color.parseColor(color));
            }
            ds.setUnderlineText(showLine);//是否显示下划线
        }
    }
}
