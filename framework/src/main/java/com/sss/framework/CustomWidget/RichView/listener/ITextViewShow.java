package com.sss.framework.CustomWidget.RichView.listener;

import android.content.Context;
import android.text.method.MovementMethod;

import com.sss.framework.CustomWidget.RichView.model.TopicModel;
import com.sss.framework.CustomWidget.RichView.model.UserModel;
import com.sss.framework.CustomWidget.RichView.span.ClickAtUserSpan;
import com.sss.framework.CustomWidget.RichView.span.ClickTopicSpan;
import com.sss.framework.CustomWidget.RichView.span.LinkSpan;


/**
 * textview 显示接口
 * Created by guoshuyu on 2017/8/22.
 */

public interface ITextViewShow {
    void setText(CharSequence charSequence);

    CharSequence getText();

    void setMovementMethod(MovementMethod movementMethod);

    void setAutoLinkMask(int flag);

    ClickAtUserSpan getCustomClickAtUserSpan(Context context, UserModel userModel, int color, SpanAtUserCallBack spanClickCallBack);

    ClickTopicSpan getCustomClickTopicSpan(Context context, TopicModel topicModel, int color, SpanTopicCallBack spanTopicCallBack);

    LinkSpan getCustomLinkSpan(Context context, String url, int color, SpanUrlCallBack spanUrlCallBack);

    int emojiSize();

    int verticalAlignment();
}
