package com.chaolu.imsdk.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SViewUtil
{
    private SViewUtil()
    {
    }

    /**
     * 设置view的背景
     *
     * @param view
     * @param drawable
     */
    public static void setBackgroundDrawable(View view, Drawable drawable)
    {
        if (view == null)
        {
            return;
        }
        int paddingLeft = view.getPaddingLeft();
        int paddingTop = view.getPaddingTop();
        int paddingRight = view.getPaddingRight();
        int paddingBottom = view.getPaddingBottom();
        view.setBackgroundDrawable(drawable);
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    /**
     * 设置view的背景
     *
     * @param view
     * @param resId 背景资源id
     */
    public static void setBackgroundResource(View view, int resId)
    {
        if (view == null)
        {
            return;
        }
        int paddingLeft = view.getPaddingLeft();
        int paddingTop = view.getPaddingTop();
        int paddingRight = view.getPaddingRight();
        int paddingBottom = view.getPaddingBottom();
        view.setBackgroundResource(resId);
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    /**
     * 设置view的左边margin（当view的LayoutParams instanceof MarginLayoutParams才有效）
     *
     * @param view
     * @param left
     */
    public static void setMarginLeft(View view, int left)
    {
        MarginLayoutParams params = getMarginLayoutParams(view);
        if (params == null)
        {
            return;
        }
        if (params.leftMargin != left)
        {
            params.leftMargin = left;
            view.setLayoutParams(params);
        }
    }

    /**
     * 设置view的顶部margin（当view的LayoutParams instanceof MarginLayoutParams才有效）
     *
     * @param view
     * @param top
     */
    public static void setMarginTop(View view, int top)
    {
        MarginLayoutParams params = getMarginLayoutParams(view);
        if (params == null)
        {
            return;
        }
        if (params.topMargin != top)
        {
            params.topMargin = top;
            view.setLayoutParams(params);
        }
    }

    /**
     * 设置view的右边margin（当view的LayoutParams instanceof MarginLayoutParams才有效）
     *
     * @param view
     * @param right
     */
    public static void setMarginRight(View view, int right)
    {
        MarginLayoutParams params = getMarginLayoutParams(view);
        if (params == null)
        {
            return;
        }
        if (params.rightMargin != right)
        {
            params.rightMargin = right;
            view.setLayoutParams(params);
        }
    }

    /**
     * 设置view的底部margin（当view的LayoutParams instanceof MarginLayoutParams才有效）
     *
     * @param view
     * @param bottom
     */
    public static void setMarginBottom(View view, int bottom)
    {
        MarginLayoutParams params = getMarginLayoutParams(view);
        if (params == null)
        {
            return;
        }
        if (params.bottomMargin != bottom)
        {
            params.bottomMargin = bottom;
            view.setLayoutParams(params);
        }
    }

    /**
     * 设置view的上下左右margin（当view的LayoutParams instanceof MarginLayoutParams才有效）
     *
     * @param view
     * @param margins
     */
    public static void setMargins(View view, int margins)
    {
        setMargin(view, margins, margins, margins, margins);
    }

    /**
     * 设置view的上下左右margin（当view的LayoutParams instanceof MarginLayoutParams才有效）
     *
     * @param view
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setMargin(View view, int left, int top, int right, int bottom)
    {
        MarginLayoutParams params = getMarginLayoutParams(view);
        if (params == null)
        {
            return;
        }

        boolean needSet = false;
        if (params.leftMargin != left)
        {
            params.leftMargin = left;
            needSet = true;
        }
        if (params.topMargin != top)
        {
            params.topMargin = top;
            needSet = true;
        }
        if (params.rightMargin != right)
        {
            params.rightMargin = right;
            needSet = true;
        }
        if (params.bottomMargin != bottom)
        {
            params.bottomMargin = bottom;
            needSet = true;
        }
        if (needSet)
        {
            view.setLayoutParams(params);
        }
    }

    /**
     * 获得view的MarginLayoutParams，返回值可能为null
     *
     * @param view
     * @return
     */
    public static MarginLayoutParams getMarginLayoutParams(View view)
    {
        if (view == null)
        {
            return null;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params instanceof MarginLayoutParams)
        {
            return (MarginLayoutParams) params;
        } else
        {
            return null;
        }
    }

    /**
     * 设置该view在父布局中的gravity
     *
     * @param view
     * @param gravity
     */
    public static void setLayoutGravity(View view, int gravity)
    {
        if (view == null)
        {
            return;
        }

        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null)
        {
            return;
        }

        if (p instanceof FrameLayout.LayoutParams)
        {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) p;
            if (params.gravity != gravity)
            {
                params.gravity = gravity;
                view.setLayoutParams(params);
            }
        } else if (p instanceof LinearLayout.LayoutParams)
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) p;
            if (params.gravity != gravity)
            {
                params.gravity = gravity;
                view.setLayoutParams(params);
            }
        }
    }

    /**
     * 设置view的左边padding
     *
     * @param view
     * @param left
     */
    public static void setPaddingLeft(View view, int left)
    {
        if (view == null)
        {
            return;
        }
        view.setPadding(left, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    /**
     * 设置view的顶部padding
     *
     * @param view
     * @param top
     */
    public static void setPaddingTop(View view, int top)
    {
        if (view == null)
        {
            return;
        }
        view.setPadding(view.getPaddingLeft(), top, view.getPaddingRight(), view.getPaddingBottom());
    }

    /**
     * 设置view的右边padding
     *
     * @param view
     * @param right
     */
    public static void setPaddingRight(View view, int right)
    {
        if (view == null)
        {
            return;
        }
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), right, view.getPaddingBottom());
    }

    /**
     * 设置view的底部padding
     *
     * @param view
     * @param bottom
     */
    public static void setPaddingBottom(View view, int bottom)
    {
        if (view == null)
        {
            return;
        }
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), bottom);
    }

    /**
     * 根据传入的宽度，获得按指定比例缩放后的高度
     *
     * @param scaleWidth  指定的比例宽度
     * @param scaleHeight 指定的比例高度
     * @param width       宽度
     * @return
     */
    public static int getScaleHeight(int scaleWidth, int scaleHeight, int width)
    {
        if (scaleWidth == 0)
        {
            return 0;
        }
        return scaleHeight * width / scaleWidth;
    }

    /**
     * 根据传入的高度，获得按指定比例缩放后的宽度
     *
     * @param scaleWidth  指定的比例宽度
     * @param scaleHeight 指定的比例高度
     * @param height      高度
     * @return
     */
    public static int getScaleWidth(int scaleWidth, int scaleHeight, int height)
    {
        if (scaleHeight == 0)
        {
            return 0;
        }
        return scaleWidth * height / scaleHeight;
    }

    /**
     * 测量view，测量后，可以获得view的测量宽高
     *
     * @param v
     */
    public static void measureView(View v)
    {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
    }

    /**
     * 获得view的高度
     *
     * @param view
     * @return
     */
    public static int getHeight(View view)
    {
        int height = 0;
        height = view.getHeight();
        if (height <= 0)
        {
            if (view.getLayoutParams() != null)
            {
                height = view.getLayoutParams().height;
            }
        }
        if (height <= 0)
        {
            measureView(view);
            height = view.getMeasuredHeight();
        }
        return height;
    }

    /**
     * 获得view的宽度
     *
     * @param view
     * @return
     */
    public static int getWidth(View view)
    {
        int width = 0;
        width = view.getWidth();
        if (width <= 0)
        {
            if (view.getLayoutParams() != null)
            {
                width = view.getLayoutParams().width;
            }
        }
        if (width <= 0)
        {
            measureView(view);
            width = view.getMeasuredWidth();
        }
        return width;
    }


    /**
     * 设置view的高度
     *
     * @param view
     * @param height
     * @return
     */
    public static boolean setHeight(View view, int height)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null)
        {
            if (params.height != height)
            {
                params.height = height;
                view.setLayoutParams(params);
            }
            return true;
        }
        return false;
    }

    /**
     * 设置view的宽度
     *
     * @param view
     * @param width
     * @return
     */
    public static boolean setWidth(View view, int width)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null)
        {
            if (params.width != width)
            {
                params.width = width;
                view.setLayoutParams(params);
            }
            return true;
        }
        return false;
    }

    /**
     * 设置view的宽度和高度
     *
     * @param view
     * @param width
     * @param height
     * @return
     */
    public static boolean setSize(View view, int width, int height)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null)
        {
            boolean needSet = false;
            if (params.width != width)
            {
                params.width = width;
                needSet = true;
            }
            if (params.height != height)
            {
                params.height = height;
                needSet = true;
            }
            if (needSet)
            {
                view.setLayoutParams(params);
            }
            return true;
        }
        return false;
    }

    /**
     * 当view的父布局是RelativeLayout的时候，设置view的布局规则
     *
     * @param view
     * @param anchorId
     * @param rules
     */
    public static void addRule(View view, int anchorId, Integer... rules)
    {
        if (view == null || rules == null)
        {
            return;
        }
        if (!(view.getLayoutParams() instanceof RelativeLayout.LayoutParams))
        {
            return;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

        for (Integer item : rules)
        {
            if (anchorId != 0)
            {
                params.addRule(item, anchorId);
            } else
            {
                params.addRule(item);
            }
        }
        view.setLayoutParams(params);
    }

    /**
     * 当view的父布局是RelativeLayout的时候，移除view的布局规则
     *
     * @param view
     * @param rules 要移除的布局规则
     */
    @TargetApi(17)
    public static void removeRule(View view, Integer... rules)
    {
        if (view == null || rules == null)
        {
            return;
        }
        if (!(view.getLayoutParams() instanceof RelativeLayout.LayoutParams))
        {
            return;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

        for (Integer item : rules)
        {
            params.removeRule(item);
        }
        view.setLayoutParams(params);
    }

    /**
     * 返回View的可见状态
     *
     * @param view
     * @return -1(view为null的时候)<br> {@link View#VISIBLE}<br> {@link View#INVISIBLE}<br> {@link View#GONE}<br>
     */
    public static int getVisibility(View view)
    {
        if (view == null)
        {
            return -1;
        }
        return view.getVisibility();
    }

    /**
     * 设置view的可见状态
     *
     * @param view
     * @param visibility
     * @return true-view处于设置的状态
     */
    public static boolean setVisibility(View view, int visibility)
    {
        if (view == null)
        {
            return false;
        }
        if (visibility == View.VISIBLE ||
                visibility == View.INVISIBLE ||
                visibility == View.GONE)
        {
            view.setVisibility(visibility);
            return true;
        } else
        {
            throw new IllegalArgumentException("visibility is Illegal");
        }
    }

    /**
     * 设置View.VISIBLE或者View.GONE
     *
     * @param view
     * @param visible true-View.VISIBLE；false-View.GONE
     * @return true-view处于VISIBLE
     */
    public static boolean setVisibleOrGone(View view, boolean visible)
    {
        if (view == null)
        {
            return false;
        }
        if (visible)
        {
            view.setVisibility(View.VISIBLE);
            return true;
        } else
        {
            view.setVisibility(View.GONE);
            return false;
        }
    }

    /**
     * 设置View.VISIBLE或者View.INVISIBLE
     *
     * @param view
     * @param visible true-View.VISIBLE；false-View.INVISIBLE
     * @return true-view处于VISIBLE
     */
    public static boolean setVisibleOrInvisible(View view, boolean visible)
    {
        if (view == null)
        {
            return false;
        }
        if (visible)
        {
            view.setVisibility(View.VISIBLE);
            return true;
        } else
        {
            view.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    /**
     * 设置view在VISIBLE和GONE之间切换
     *
     * @param view
     * @return true-view处于VISIBLE
     */
    public static boolean toggleVisibleOrGone(View view)
    {
        if (view == null)
        {
            return false;
        }
        if (view.getVisibility() == View.VISIBLE)
        {
            view.setVisibility(View.GONE);
            return false;
        } else
        {
            view.setVisibility(View.VISIBLE);
            return true;
        }
    }

    /**
     * 设置view在VISIBLE和INVISIBLE之间切换
     *
     * @param view
     * @return true-view处于VISIBLE
     */
    public static boolean toggleVisibleOrInvisible(View view)
    {
        if (view == null)
        {
            return false;
        }
        if (view.getVisibility() == View.VISIBLE)
        {
            view.setVisibility(View.INVISIBLE);
            return false;
        } else
        {
            view.setVisibility(View.VISIBLE);
            return true;
        }
    }

    /**
     * view是否被添加到界面上
     *
     * @param view
     * @return
     */
    public static boolean isAttached(View view)
    {
        if (view == null)
        {
            return false;
        }
        ViewParent parent = view.getRootView().getParent();
        if (parent == null)
        {
            return false;
        }
        if (!(parent instanceof View))
        {
            return true;
        }
        return isAttached((View) parent);
    }

    /**
     * view是否在某个屏幕的触摸点下面
     *
     * @param view
     * @param event       触摸点
     * @param outLocation 用于接收view的x和y坐标的数组，可以为null
     * @return
     */
    public static boolean isViewUnder(View view, MotionEvent event, int[] outLocation)
    {
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();
        return isViewUnder(view, x, y, outLocation);
    }

    /**
     * view是否在某个屏幕坐标下面
     *
     * @param view
     * @param x           屏幕x坐标
     * @param y           屏幕y坐标
     * @param outLocation 用于接收view的x和y坐标的数组，可以为null
     * @return
     */
    public static boolean isViewUnder(View view, int x, int y, int[] outLocation)
    {
        final int[] location = getLocationOnScreen(view, outLocation);
        final int left = location[0];
        final int top = location[1];
        final int right = left + view.getWidth();
        final int bottom = top + view.getHeight();

        return left < right && top < bottom
                && x >= left && x < right && y >= top && y < bottom;
    }

    /**
     * 获得view在屏幕上的坐标
     *
     * @param view
     * @param outLocation 如果为null或者长度不等于2，内部会创建一个长度为2的数组返回
     * @return
     */
    public static int[] getLocationOnScreen(View view, int[] outLocation)
    {
        if (outLocation == null || outLocation.length != 2)
        {
            outLocation = new int[]{0, 0};
        }
        view.getLocationOnScreen(outLocation);
        return outLocation;
    }

    /**
     * 获得view的截图
     *
     * @param view
     * @return
     */
    public static Bitmap createViewBitmap(View view)
    {
        Bitmap bmp = null;
        if (view != null)
        {
            view.setDrawingCacheEnabled(true);
            Bitmap drawingCache = view.getDrawingCache();
            if (drawingCache != null)
            {
                bmp = Bitmap.createBitmap(drawingCache);
            }
            view.destroyDrawingCache();
        }
        return bmp;
    }

    /**
     * 获得view的镜像
     *
     * @param view
     * @return
     */
    public static ImageView getViewsImage(View view)
    {
        Bitmap bmp = createViewBitmap(view);
        if (bmp == null)
        {
            return null;
        }
        ImageView imageView = new ImageView(view.getContext());
        imageView.setImageBitmap(bmp);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(bmp.getWidth(), bmp.getHeight());
        imageView.setLayoutParams(params);
        return imageView;
    }

    public static void wrapperPopupWindow(PopupWindow pop)
    {
        if (pop == null)
        {
            return;
        }
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        pop.setBackgroundDrawable(dw);
        pop.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        pop.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
    }

    /**
     * 按指定比例缩放view的高度
     *
     * @param view
     * @param scaleWidth  指定比例宽度
     * @param scaleHeight 指定比例高度
     */
    public static void scaleWidth(View view, int scaleWidth, int scaleHeight)
    {
        if (view == null || scaleHeight == 0)
        {
            return;
        }
        int height = getHeight(view);
        int width = getScaleWidth(scaleWidth, scaleHeight, height);
        setWidth(view, width);
    }

    /**
     * 按指定比例缩放view的高度
     *
     * @param view
     * @param scaleWidth  指定比例宽度
     * @param scaleHeight 指定比例高度
     */
    public static void scaleHeight(View view, int scaleWidth, int scaleHeight)
    {
        if (view == null || scaleWidth == 0)
        {
            return;
        }
        int width = getWidth(view);
        int height = getScaleHeight(scaleWidth, scaleHeight, width);
        setHeight(view, height);
    }

    /**
     * 设置view的宽度为WRAP_CONTENT
     *
     * @param view
     */
    public static void setWidthWrapContent(View view)
    {
        setWidth(view, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 设置view的宽度为MATCH_PARENT
     *
     * @param view
     */
    public static void setWidthMatchParent(View view)
    {
        setWidth(view, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 设置view的高度为WRAP_CONTENT
     *
     * @param view
     */
    public static void setHeightWrapContent(View view)
    {
        setHeight(view, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 设置view的高度为MATCH_PARENT
     *
     * @param view
     */
    public static void setHeightMatchParent(View view)
    {
        setHeight(view, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 设置view的宽度和weight，仅当view处于LinearLayout里面时有效
     *
     * @param view
     * @param width
     * @param weight
     */
    public static void setWidthWeight(View view, int width, float weight)
    {
        if (view == null)
        {
            return;
        }
        ViewGroup.LayoutParams vgParams = view.getLayoutParams();
        if (vgParams instanceof LinearLayout.LayoutParams)
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vgParams;

            boolean needSet = false;
            if (params.width != width)
            {
                params.width = width;
                needSet = true;
            }
            if (params.weight != weight)
            {
                params.weight = weight;
                needSet = true;
            }

            if (needSet)
            {
                view.setLayoutParams(params);
            }
        }
    }

    /**
     * 设置view的高度和weight，仅当view处于LinearLayout里面时有效
     *
     * @param view
     * @param height
     * @param weight
     */
    public static void setHeightWeight(View view, int height, float weight)
    {
        if (view == null)
        {
            return;
        }

        ViewGroup.LayoutParams vgParams = view.getLayoutParams();
        if (vgParams instanceof LinearLayout.LayoutParams)
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vgParams;

            boolean needSet = false;
            if (params.height != height)
            {
                params.height = height;
                needSet = true;
            }
            if (params.weight != weight)
            {
                params.weight = weight;
                needSet = true;
            }

            if (needSet)
            {
                view.setLayoutParams(params);
            }
        }
    }

    /**
     * 开始动画Drawable
     *
     * @param drawable
     */
    public static void startAnimationDrawable(Drawable drawable)
    {
        if (drawable instanceof AnimationDrawable)
        {
            AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
            if (!animationDrawable.isRunning())
            {
                animationDrawable.start();
            }
        }
    }

    /**
     * 停止动画Drawable
     *
     * @param drawable
     */
    public static void stopAnimationDrawable(Drawable drawable)
    {
        stopAnimationDrawable(drawable, 0);
    }

    /**
     * 停止动画Drawable
     *
     * @param drawable
     * @param stopIndex 要停止在第几帧
     */
    public static void stopAnimationDrawable(Drawable drawable, int stopIndex)
    {
        if (drawable instanceof AnimationDrawable)
        {
            AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
            animationDrawable.stop();
            animationDrawable.selectDrawable(stopIndex);
        }
    }

    /**
     * 重置view
     *
     * @param view
     */
    public static void resetView(View view)
    {
        if (view != null)
        {
            view.setAlpha(1.0f);
            view.setRotation(0.0f);
            view.setRotationX(0.0f);
            view.setRotationY(0.0f);
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        }
    }

    /**
     * 测量文字的宽度
     *
     * @param textView
     * @param content  文字内容
     * @return
     */
    public static float measureText(TextView textView, String content)
    {
        float width = 0;
        if (textView != null && content != null)
        {
            width = textView.getPaint().measureText(content);
        }
        return width;
    }

    /**
     * 把view从它的父容器移除
     *
     * @param view
     */
    public static void removeView(final View view)
    {
        try
        {
            final ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.removeView(view);
        } catch (Exception e)
        {
        }
    }

    /**
     * 用新的view去替换布局中的旧view
     *
     * @param oldView
     * @param newView
     */
    public static void replaceOldView(final View oldView, final View newView)
    {
        if (oldView == null || newView == null || oldView == newView)
        {
            return;
        }
        final ViewParent parent = oldView.getParent();
        if (parent instanceof ViewGroup)
        {
            final ViewGroup viewGroup = (ViewGroup) parent;
            final int index = viewGroup.indexOfChild(oldView);
            final ViewGroup.LayoutParams params = oldView.getLayoutParams();

            removeView(oldView);
            removeView(newView);

            viewGroup.addView(newView, index, params);
        }
    }

    /**
     * 替换child到parent，仅保留当前child对象在容器中
     *
     * @param parent
     * @param child
     */
    public static void replaceView(View parent, View child)
    {
        addView(parent, child, true);
    }

    /**
     * 添加child到parent
     *
     * @param parent
     * @param child
     */
    public static void addView(View parent, View child)
    {
        addView(parent, child, false);
    }

    /**
     * 添加child到parent
     *
     * @param parent         父容器
     * @param child          要添加的view
     * @param removeAllViews 添加的时候是否需要先移除parent的所有子view
     */
    private static void addView(final View parent, final View child, final boolean removeAllViews)
    {
        if (parent == null || child == null)
        {
            return;
        }
        if (!(parent instanceof ViewGroup))
        {
            throw new IllegalArgumentException("parent must be instance of ViewGroup");
        }

        final ViewGroup viewGroup = (ViewGroup) parent;
        if (child.getParent() != viewGroup)
        {
            if (removeAllViews)
            {
                viewGroup.removeAllViews();
            }
            removeView(child);
            viewGroup.addView(child);
        } else
        {
            if (removeAllViews)
            {
                final int count = viewGroup.getChildCount();
                for (int i = 0; i < count; i++)
                {
                    final View item = viewGroup.getChildAt(i);
                    if (item != child)
                    {
                        viewGroup.removeView(item);
                    }
                }
            }
        }
    }

    /**
     * 切换容器的内容为view<br>
     * 如果child没有被添加到parent，child将会被添加到parent，最终只显示child，隐藏parent的所有其他child
     *
     * @param parent
     * @param child
     */
    public static void toggleView(final ViewGroup parent, final View child)
    {
        if (child == null || parent == null)
        {
            return;
        }
        if (child.getParent() != parent)
        {
            removeView(child);
            parent.addView(child);
        }

        final int count = parent.getChildCount();
        for (int i = 0; i < count; i++)
        {
            final View item = parent.getChildAt(i);
            if (item == child)
            {
                item.setVisibility(View.VISIBLE);
            } else
            {
                item.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置状态栏背景透明
     *
     * @param activity
     * @param transparent true-透明，false不透明
     */
    public static boolean setStatusBarTransparent(Activity activity, boolean transparent)
    {
        try
        {
            Window window = activity.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (transparent)
            {
                params.flags |= bits;
            } else
            {
                params.flags &= ~bits;
            }
            window.setAttributes(params);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }
}
