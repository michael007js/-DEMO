package com.sss.framework.CustomWidget.RichView.listener;

import android.content.Context;

import com.sss.framework.CustomWidget.RichView.model.TopicModel;
import com.sss.framework.CustomWidget.RichView.model.UserModel;
import com.sss.framework.CustomWidget.RichView.span.ClickAtUserSpan;
import com.sss.framework.CustomWidget.RichView.span.ClickTopicSpan;
import com.sss.framework.CustomWidget.RichView.span.LinkSpan;


/**
 * Created by guoshuyu on 2017/8/31.
 */

public interface SpanCreateListener {

    ClickAtUserSpan getCustomClickAtUserSpan(Context context, UserModel userModel, int color, SpanAtUserCallBack spanClickCallBack);

    ClickTopicSpan getCustomClickTopicSpan(Context context, TopicModel topicModel, int color, SpanTopicCallBack spanTopicCallBack);

    LinkSpan getCustomLinkSpan(Context context, String url, int color, SpanUrlCallBack spanUrlCallBack);
}
