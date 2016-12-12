package com.dhermanu.soulfull;

import com.yelp.clientlib.entities.Category;

import java.text.DecimalFormat;
import java.util.List;

import autovalue.shaded.org.apache.commons.lang.ArrayUtils;

/**
 * Created by dean on 9/24/16.
 */

public class Utility {

    public static String distance(double distance) {
        DecimalFormat df = new DecimalFormat("0.#");

        double miles = distance/1609.344;
        String result = df.format(miles) + " mi";

        return result;
    }

    public static String convertImageURL(String url){
        char[] urlArray = url.toCharArray();
        urlArray[urlArray.length - 6] = 'l';
        urlArray = ArrayUtils.remove(urlArray, urlArray.length-5);


        return new String(urlArray);
    }

    public static String listToString(List<String> arrayList){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String s : arrayList)
        {
            sb.append(s);
            if(i != arrayList.size() - 1)
                sb.append(", ");
            i++;
        }
        return new String(sb);
    }

    public static String catListToString(List<Category> arrayList){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(Category category : arrayList){
            sb.append(category.name());
            if(i != arrayList.size() - 1)
                sb.append(", ");
            i++;
        }
        return new String(sb);
    }

}





