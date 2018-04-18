package com.sss.framework.Utils;


import java.util.ArrayList;
import java.util.List;

/**
 * 数组/列表交叉遍历组合算法工具
 * Created by leilei on 2017/10/27.
 */

public class ArrayListTurnsUtils {
    /**
     * 两两遍历
     *
     * @param array1
     * @param array2
     * @return
     */
    public static String[] doubleTurns(String[] array1, String[] array2) {
        String[] target = new String[array1.length * array2.length];
        for (int i = 0, a1 = 0, a2 = 0; i < array1.length * array2.length; i++) {
            target[i] = array1[a1] + "+" + array2[a2];
            a2++;
            if (a2 == array2.length) {
                a2 = 0;
                a1++;
            }
        }
        return target;
    }


    /**
     * 遍历组合
     *
     * @param arrays
     * @return
     */
    public static String[] turns(String[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }
        if (arrays.length == 0) {
            return null;
        }
        //获得总结果数
        int count = 0;
        for (int i = 0; i < arrays.length; i++) {
            count *= arrays[i].length;
        }
        String target[] = new String[count];
        //两两遍历
        for (int i = 0; i < arrays.length; i++) {
            if (i == 0) {
                target = doubleTurns(arrays[0], arrays[1]);
                i++;
            } else {
                target = doubleTurns(target, arrays[i]);
            }
        }
        return target;
    }

    /**
     * 遍历组合
     *
     * @param arrayList
     * @return
     */
    public static List<String> turns(List<List<String>> arrayList) {
        if (arrayList.size() == 1) {
            return arrayList.get(0);
        }
        if (arrayList.size() == 0) {
            return null;
        }
        //获得总结果数
        int count = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            count *= arrayList.get(i).size();
        }
        List<String> target = new ArrayList<>();
        //两两遍历
        for (int i = 0; i < arrayList.size(); i++) {
            if (i == 0) {
                target = doubleTurns(arrayList.get(0), arrayList.get(1));
                i++;
            } else {
                target = doubleTurns(target, arrayList.get(i));
            }
        }
        return target;
    }

    /**
     * 两两遍历
     *
     * @param list1
     * @param list2
     * @return
     */
    public static List doubleTurns(List<String> list1, List<String> list2) {
        List<String> target = new ArrayList<>();
        for (int i = 0, a1 = 0, a2 = 0; i < list1.size() * list2.size(); i++) {
            target.add(list1.get(a1) + "+" + list2.get(a2));
            a2++;
            if (a2 == list2.size()) {
                a2 = 0;
                a1++;
            }
        }
        return target;
    }
}
