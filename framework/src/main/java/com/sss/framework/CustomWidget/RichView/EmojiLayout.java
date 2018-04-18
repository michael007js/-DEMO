package com.sss.framework.CustomWidget.RichView;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.framework.Adapater.ViewPagerAdpter;
import com.sss.framework.Library.Fresco.FrescoUtils;
import com.sss.framework.Library.SSSAdapter.SSS_Adapter;
import com.sss.framework.Library.SSSAdapter.SSS_HolderHelper;
import com.sss.framework.R;

import java.util.ArrayList;
import java.util.List;


/*
    <com.sss.framework.CustomWidget.RichView.EmojiLayout
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/fl_emotionview_main"
        android:layout_width="match_parent"
        android:layout_height="250dp"
        android:layout_alignParentBottom="true"
        app:richIndicatorFocus="@mipmap/page_indicator_focused"
        app:richIndicatorUnFocus="@mipmap/page_indicator_unfocused"
        app:richMarginBottom="8dp"
        app:richLayoutNumColumns="5"
        app:richLayoutNumRows="3"
        app:richMarginTop="15dp" />
* */


/**
 * 承载表情布局
 * Created by shuyu on 2016/9/2.
 */

public class EmojiLayout extends LinearLayout {

    private ViewPager edittextBarVPager;
    private LinearLayout edittextBarViewGroupFace;
    private LinearLayout edittextBarLlFaceContainer;
    private LinearLayout edittextBarMore;

    private RichEditText editTextEmoji;
    private List<String> reslist;
    private ImageView[] imageFaceViews;


    private Drawable focusIndicator;
    private Drawable unFocusIndicator;
    private String deleteIconName = "delete_expression";

    private int richMarginBottom;
    private int richMarginTop;

    private int numColumns = 7;

    private int numRows = 3;

    private int pageCount = (numColumns * numRows) - 1;


    public EmojiLayout(Context context) {
        super(context);
        init(context, null);
    }

    public EmojiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EmojiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.rich_layout_emoji_container, this, true);
        if (isInEditMode())
            return;


        edittextBarVPager = (ViewPager) findViewById(R.id.edittext_bar_vPager);

        edittextBarViewGroupFace = (LinearLayout) findViewById(R.id.edittext_bar_viewGroup_face);

        edittextBarLlFaceContainer = (LinearLayout) findViewById(R.id.edittext_bar_ll_face_container);

        edittextBarMore = (LinearLayout) findViewById(R.id.edittext_bar_more);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EmojiLayout);
            String deleteIconName = array.getString(R.styleable.EmojiLayout_richDeleteIconName);
            if (!TextUtils.isEmpty(deleteIconName)) {
                this.deleteIconName = deleteIconName;
            }
            focusIndicator = array.getDrawable(R.styleable.EmojiLayout_richIndicatorFocus);
            unFocusIndicator = array.getDrawable(R.styleable.EmojiLayout_richIndicatorUnFocus);
            richMarginBottom = (int) array.getDimension(R.styleable.EmojiLayout_richMarginBottom, dip2px(getContext(), 8));
            richMarginTop = (int) array.getDimension(R.styleable.EmojiLayout_richMarginTop, dip2px(getContext(), 15));
            numColumns = array.getInteger(R.styleable.EmojiLayout_richLayoutNumColumns, 7);
            numRows = array.getInteger(R.styleable.EmojiLayout_richLayoutNumRows, 3);
            pageCount = numColumns * numRows - 1;
            array.recycle();
        }

        if (focusIndicator == null) {
            focusIndicator = getContext().getResources().getDrawable(R.drawable.rich_page_indicator_focused);
        }

        if (unFocusIndicator == null) {
            unFocusIndicator = getContext().getResources().getDrawable(R.drawable.rich_page_indicator_unfocused);
        }

        initViews();

    }


    /**
     * 初始化View
     */
    private void initViews() {

        int size = dip2px(getContext(), 5);

        int marginSize = dip2px(getContext(), 5);

        // 表情list
        reslist = SmileUtils.getTextList();

        int viewSize = (int) Math.ceil(reslist.size() * 1.0f / pageCount);

        // 初始化表情viewpager
        List<View> views = new ArrayList<>();
        for (int i = 0; i < viewSize; i++) {
            View gv = getGridChildView(i + 1);
            views.add(gv);
        }

        ImageView imageViewFace;
        imageFaceViews = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            LayoutParams margin = new LayoutParams(size, size);
            margin.setMargins(marginSize, 0, 0, 0);
            imageViewFace = new ImageView(getContext());
            imageViewFace.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            imageFaceViews[i] = imageViewFace;
            if (i == 0) {
                imageFaceViews[i].setBackground(focusIndicator);
            } else {
                imageFaceViews[i].setBackground(unFocusIndicator);
            }
            edittextBarViewGroupFace.addView(imageFaceViews[i], margin);
        }
        edittextBarVPager.setAdapter(new ViewPagerAdpter(views));
        edittextBarVPager.addOnPageChangeListener(new GuidePageChangeListener());

    }


    /**
     * 获取表情的gridview的子view
     */
    private View getGridChildView(int i) {
        View view = View.inflate(getContext(), R.layout.rich_expression_gridview, null);
        LockGridView gv = (LockGridView) view.findViewById(R.id.gridview);
        gv.setNumColumns(numColumns);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) gv.getLayoutParams();
        layoutParams.setMargins(0, richMarginTop, 0, richMarginBottom);

        List<String> list = new ArrayList<String>();

        int startInd = (i - 1) * pageCount;
        if ((startInd + pageCount) >= reslist.size()) {
            list.addAll(reslist.subList(startInd, startInd + (reslist.size() - startInd)));
        } else {
            list.addAll(reslist.subList(startInd, startInd + pageCount));
        }
        list.add(deleteIconName);
        final SSS_Adapter sss_adapter = new SSS_Adapter<String>(getContext(), R.layout.rich_smile_image_row_expression) {
            @Override
            protected void setView(SSS_HolderHelper helper, final int position, String bean, SSS_Adapter instance) {
                if ("delete_expression".equals(bean)) {
                    FrescoUtils.showImage(false, 56, 56,
                            Uri.parse("res://" + getContext().getPackageName() + "/" +
                                    getContext().getResources().getIdentifier(getItem(position), "drawable", getContext().getPackageName())
                            ),
                            (SimpleDraweeView) helper.getView(R.id.icon),
                            0f);
                } else {
                    FrescoUtils.showImage(false, 56, 56,
                            Uri.parse("res://" + getContext().getPackageName() + "/" +
                                    SmileUtils.getRedId(getItem(position))
                            ),
                            (SimpleDraweeView) helper.getView(R.id.icon),
                            0f);
                }
                helper.getView(R.id.icon).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if (!deleteIconName.equals(getItem(position))) { // 不是删除键，显示表情
                                (editTextEmoji).insertIcon(getItem(position));
                            } else { // 删除文字或者表情
                                if (!TextUtils.isEmpty(editTextEmoji.getText())) {

                                    int selectionStart = editTextEmoji.getSelectionStart();// 获取光标的位置
                                    if (selectionStart > 0) {
                                        String body = editTextEmoji.getText().toString();
                                        String tempStr = body.substring(0, selectionStart);
                                        int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                        int end = tempStr.lastIndexOf("]");// 获取最后一个表情的位置
                                        if (i != -1 && end == (selectionStart - 1)) {
                                            CharSequence cs = tempStr.substring(i, selectionStart);
                                            if (SmileUtils.containsKey(cs.toString()))
                                                editTextEmoji.getEditableText().delete(i, selectionStart);
                                            else
                                                editTextEmoji.getEditableText().delete(selectionStart - 1,
                                                        selectionStart);
                                        } else {
                                            editTextEmoji.getEditableText().delete(selectionStart - 1, selectionStart);
                                        }
                                    }
                                }

                            }
                    }
                });

            }

            @Override
            protected void setItemListener(SSS_HolderHelper helper) {

            }
        };
        gv.setAdapter(sss_adapter);
        sss_adapter.setList(list);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        return view;
    }

    /**
     * dip转为PX
     */
    private int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }

    private class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < imageFaceViews.length; i++) {
                imageFaceViews[arg0].setBackground(focusIndicator);
                if (arg0 != i) {
                    imageFaceViews[i].setBackground(unFocusIndicator);
                }
            }
        }
    }


    public LinearLayout getEdittextBarViewGroupFace() {
        return edittextBarViewGroupFace;
    }

    public LinearLayout getEdittextBarLlFaceContainer() {
        return edittextBarLlFaceContainer;
    }

    public LinearLayout getEdittextBarMore() {
        return edittextBarMore;
    }

    public RichEditText getEditTextEmoji() {
        return editTextEmoji;
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        Activity context = (Activity) getContext();
        if (context.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (context.getCurrentFocus() != null) {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 显示键盘
     */
    public void showKeyboard() {
        editTextEmoji.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editTextEmoji.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editTextEmoji, 0);
    }


    public RichEditText getEditTextSmile() {
        return editTextEmoji;
    }

    public void setEditTextSmile(RichEditText editTextSmile) {
        this.editTextEmoji = editTextSmile;
    }
}

