package com.midasit.androidcustomviews;

import com.annimon.stream.function.Predicate;

/**
 * Created by nyh0111 on 2018-03-10.
 */

public class TextSizeMesurements {
    public static int calculateLengthReasonably(String text, Predicate<Character> specialRule) {
        int length = 0;
        
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(0);
            
            if (specialRule.test(ch)) {
                length += 2;
            } else {
                length++;
            }
        }
        
        return length;
    }
}
