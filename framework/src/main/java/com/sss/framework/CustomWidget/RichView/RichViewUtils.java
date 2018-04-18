package com.sss.framework.CustomWidget.RichView;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.sss.framework.CustomWidget.RichView.listener.OnEditTextUtilJumpListener;
import com.sss.framework.CustomWidget.RichView.listener.SpanAtUserCallBack;
import com.sss.framework.CustomWidget.RichView.listener.SpanCreateListener;
import com.sss.framework.CustomWidget.RichView.listener.SpanTopicCallBack;
import com.sss.framework.CustomWidget.RichView.listener.SpanUrlCallBack;
import com.sss.framework.CustomWidget.RichView.model.TopicModel;
import com.sss.framework.CustomWidget.RichView.model.UserModel;
import com.sss.framework.Utils.ScreenUtils;
import com.sss.framework.Utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;


/**
 * Created by leilei on 2018/3/2.
 */

public class RichViewUtils {

    /**
     * 初始化处理表情(需在setContentView之前调用)
     *
     * @param context
     */
    public static void initEmoji(Context context) {
        if (SmileUtils.isInitSmile()==false){
            List<Integer> data = new ArrayList<>();
            List<String> strings = new ArrayList<>();
            for (int i = 1; i < 64; i++) {
                int resId = context.getResources().getIdentifier("e" + i, "drawable", context.getPackageName());
                data.add(resId);
                strings.add("[e" + i + "]");
            }
            /**初始化为自己的**/
            SmileUtils.addPatternAll(SmileUtils.getEmoticons(), strings, data);
        }
    }

    /**
     * 绑定输入框
     *
     * @param emojiLayout                表情面板
     * @param richEditText               富文本输入框
     * @param nameList                   @人列表
     * @param topicModels                话题列表
     * @param onEditTextUtilJumpListener 输入监听回调
     */
    public static void bindEditText(EmojiLayout emojiLayout, RichEditText richEditText, List<UserModel> nameList, List<TopicModel> topicModels,
                                    OnEditTextUtilJumpListener onEditTextUtilJumpListener) {
        emojiLayout.setEditTextSmile(richEditText);
        new RichEditBuilder().setEditText(richEditText)
                .setUserModels(nameList)
                .setTopicModels(topicModels)
                .setColorAtUser("#1e81d2")
//                .setColorTopic("#F0F0C0")
                .setEditTextAtUtilJumpListener(onEditTextUtilJumpListener)
                .builder();
    }


    /**
     * 插入@对象
     *
     * @param userModel
     * @param richEditText 富文本输入框
     */
    public static void resolveAtResultByEnterAt(UserModel userModel, RichEditText richEditText) {
        richEditText.resolveAtResultByEnterAt(userModel);
    }

    /**
     * 获取文本内容
     *
     * @param richEditText 富文本输入框
     * @return
     */
    public static String getText(RichEditText richEditText) {
        return richEditText.getRealText();
    }

    /**
     * @param richTextView               富文本TextView
     * @param content                    要解析的内容
     * @param nameList                   @人列表
     * @param topicModels                话题列表
     * @param spanAtUserCallBackListener at某人点击回调
     * @param spanTopicCallBackListener  话题点击回调
     * @param spanUrlCallBackListener    url点击回调
     */
    public static void resolveRichShow(RichTextView richTextView, String content, List<UserModel> nameList, List<TopicModel> topicModels,
                                       SpanAtUserCallBack spanAtUserCallBackListener, SpanTopicCallBack spanTopicCallBackListener, SpanUrlCallBack spanUrlCallBackListener) {


//        String content = "这是测试#话题话题#文本哟 www.baidu.com " +
//                "\n来@某个人  @22222 @kkk " +
//                "\n好的,来几个表情[e2][e4][e55]，最后来一个电话 13245685478";
//        SpanUrlCallBack spanUrlCallBack = new SpanUrlCallBack() {
//            @Override
//            public void phone(View view, String phone) {
//                Toast.makeText(view.getContext(), phone + " 被点击了", Toast.LENGTH_SHORT).show();
//                if (view instanceof TextView) {
//                    ((TextView) view).setHighlightColor(Color.TRANSPARENT);
//                }
//            }
//
//            @Override
//            public void url(View view, String url) {
//                Toast.makeText(view.getContext(), url + " 被点击了", Toast.LENGTH_SHORT).show();
//                if (view instanceof TextView) {
//                    ((TextView) view).setHighlightColor(Color.TRANSPARENT);
//                }
//            }
//        };
//
//        SpanAtUserCallBack spanAtUserCallBack = new SpanAtUserCallBack() {
//            @Override
//            public void onClick(View view, UserModel userModel1) {
//                if (view instanceof TextView) {
//                    ((TextView) view).setHighlightColor(Color.TRANSPARENT);
//                }
//            }
//        };
//
//        SpanTopicCallBack spanTopicCallBack = new SpanTopicCallBack() {
//            @Override
//            public void onClick(View view, TopicModel topicModel) {
//                Toast.makeText(view.getContext(), topicModel.getTopicName() + " 被点击了", Toast.LENGTH_SHORT).show();
//                if (view instanceof TextView) {
//                    ((TextView) view).setHighlightColor(Color.TRANSPARENT);
//                }
//            }
//        };


        //直接使用RichTextView
        richTextView.setAtColor(Color.BLUE);
        richTextView.setTopicColor(Color.RED);
        richTextView.setLinkColor(Color.MAGENTA);
        richTextView.setNeedNumberShow(true);
        richTextView.setNeedUrlShow(true);
        richTextView.setSpanAtUserCallBackListener(spanAtUserCallBackListener);
        richTextView.setSpanTopicCallBackListener(spanTopicCallBackListener);
        richTextView.setSpanUrlCallBackListener(spanUrlCallBackListener);
        //所有配置完成后才设置text
        richTextView.setRichText(content, nameList, topicModels);

    }

    /**
     * @param context
     * @param textView                   普通的TextView
     * @param content                    要解析的内容
     * @param nameList                   @人列表
     * @param topicModels                话题列表
     * @param spanAtUserCallBackListener at某人点击回调
     * @param spanTopicCallBackListener  话题点击回调
     * @param spanUrlCallBackListener    url点击回调
     * @param spanCreateListener         自定义span回调
     */
    public static void resolveRichShow(Context context, TextView textView, String content, List<UserModel> nameList, List<TopicModel> topicModels,
                                       SpanAtUserCallBack spanAtUserCallBackListener, SpanTopicCallBack spanTopicCallBackListener, SpanUrlCallBack spanUrlCallBackListener, SpanCreateListener spanCreateListener) {


        RichTextBuilder richTextBuilder = new RichTextBuilder(context);
        richTextBuilder.setContent(content)
                .setAtColor(Color.RED)
                .setLinkColor(Color.BLUE)
                .setTopicColor(Color.YELLOW)
                .setListUser(nameList)
                .setListTopic(topicModels)
                .setTextView(textView)
                .setNeedUrl(false)
                .setNeedNum(false)
                .setEmojiSize(SizeUtils.dp2px(context, 5))
                //.setVerticalAlignment(CenteredImageSpan.ALIGN_CENTER)
                .setSpanAtUserCallBack(spanAtUserCallBackListener)
                .setSpanUrlCallBack(spanUrlCallBackListener)
                .setSpanTopicCallBack(spanTopicCallBackListener)
                //自定义span，如果不需要可不设置
                .setSpanCreateListener(spanCreateListener)
                .build();



    }


}
