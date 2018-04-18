package com.sss.framework.Library.Log.parser;


import com.sss.framework.Library.Log.common.LogConstant;

/**
 * @Description: 解析器接口
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 16/12/11 10:59.
 */
public interface Parser<T> {
    String LINE_SEPARATOR = LogConstant.BR;

    Class<T> parseClassType();

    String parseString(T t);
}
