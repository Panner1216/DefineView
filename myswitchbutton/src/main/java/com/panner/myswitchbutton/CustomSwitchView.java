package com.panner.myswitchbutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author panzhijie
 * @version 2017-03-29 11:29
 */
public class CustomSwitchView extends View {
    //定义背景图
    private Bitmap switchBackgroundBitmap;
    //定义前景图
    private Bitmap switchForegroundBitmap;
    //画笔
    private Paint mPaint;
    private boolean isSwitchState = true;//开关状态
    private boolean isTouchState = false;//触摸状态
    private float currentPosition;//当前开关位置
    private int maxPosition;//最大开关位置

    private OnSwitchStateUpdateListener onSwitchStateUpdateListener;

    /**
     * 代码创建控件
     */
    public CustomSwitchView(Context context) {
        this(context, null);
    }

    /**
     * XML创建控件，可以指定自定义的属性
     *
     * 为了我们自定义的属性可以使用，在这里需要重写，AttributeSet就存放了这些自定义的属性
     */
    public CustomSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        //设置命名空间
        String namespace="http://schemas.android.com/apk/res/customswitch";
        // 通过命名空间 和 属性名称 找到对应的资源对象
        int switchBackgroundResource =
                attrs.getAttributeResourceValue(namespace, "switch_background", -1);
        int switchForegroundResource =
                attrs.getAttributeResourceValue(namespace, "switch_foreground", -1);
        isSwitchState = attrs.getAttributeBooleanValue(namespace, "switch_state", false);

        // 将资源对象设置到对应位置
        setswitchBackgroundBitmap(switchBackgroundResource);
        setswitchForegroundBitmap(switchForegroundResource);
    }

    /**
     * XML中使用，可以指定自定义属性，传入样式
     */
    public CustomSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * XML中使用，可以指定自定义属性，指定样式和资源
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomSwitchView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    /**
     * 设置背景图
     *
     * @param backgroundResource 背景图资源
     */
    public void setswitchBackgroundBitmap(int backgroundResource) {
        this.switchBackgroundBitmap = BitmapFactory.decodeResource(getResources(),
                backgroundResource);
    }

    /**
     * 设置前景图
     */
    public void setswitchForegroundBitmap(int foregroundResource) {
        this.switchForegroundBitmap = BitmapFactory.decodeResource(getResources(),
                foregroundResource);
    }

    /**
     * 测量自定义的控件宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(switchBackgroundBitmap.getWidth(), switchBackgroundBitmap.getHeight());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 绘制控件
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景图
        canvas.drawBitmap(switchBackgroundBitmap, 0, 0, mPaint);
        //绘制前景图
        // 如果处于触摸状态
        if (isTouchState) {
            // 触摸位置在开关的中间位置
            float movePosition = currentPosition - switchForegroundBitmap.getWidth() / 2.0f;
            maxPosition = switchBackgroundBitmap.getWidth() - switchForegroundBitmap.getWidth();
            // 限定开关滑动范围 只能在 0 - maxPosition范围内
            if (movePosition < 0) {
                movePosition = 0;
            } else if (movePosition > maxPosition) {
                movePosition = maxPosition;
            }
            // 绘制开关
            canvas.drawBitmap(switchForegroundBitmap, movePosition, 0, mPaint);
        }
        // 直接绘制开关
        else {
            // 如果是真，直接将开关滑块置为开启状态
            if (isSwitchState) {
                maxPosition = switchBackgroundBitmap.getWidth() - switchForegroundBitmap.getWidth();
                canvas.drawBitmap(switchForegroundBitmap, maxPosition, 0, mPaint);
            } else {
                // 否则将开关置为关闭状态
                canvas.drawBitmap(switchForegroundBitmap, 0, 0, mPaint);
            }
        }
    }

    /**
     * 触摸事件的处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouchState = true;//改变触摸状态
                currentPosition = event.getX();//获取当前位置
                break;
            case MotionEvent.ACTION_MOVE:
                currentPosition = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                isTouchState = false;//触摸状态为抬起
                currentPosition = event.getX();
                //中间位置
                float centerPosition = switchBackgroundBitmap.getWidth() / 2.0f;
                //如果当前的开关位于中间位置后，那么开关为关闭
                boolean currentState = currentPosition > centerPosition;
                // 如果当然状态不相同且绑定了监听对象 则执行监听方法
                if (currentState != isSwitchState && onSwitchStateUpdateListener != null) {
                    onSwitchStateUpdateListener.onStateUpdate(currentState);
                }
                // 当前状态置为开关状态
                isSwitchState = currentState;
                isSwitchState = currentState;
                break;
        }
        //系统提供的方法，可以重新调用一次onDraw（）方法
        invalidate();
        return true;
    }

    /**
     * 初始化
     */
    public void initView() {
        mPaint = new Paint();
    }

    public void setOnSwitchStateUpdateListener(
            OnSwitchStateUpdateListener onSwitchStateUpdateListener) {
        this.onSwitchStateUpdateListener = onSwitchStateUpdateListener;
    }

    /**
     * 添加开关状态变化的监听
     */
    public interface OnSwitchStateUpdateListener {
        //将当前的状态传回
        void onStateUpdate(boolean state);
    }
}
