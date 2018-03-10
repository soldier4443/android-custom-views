package com.midasit.androidcustomviews;

import com.annimon.stream.function.Predicate;

/**
 * Created by nyh0111 on 2018-03-10.
 */

public class PredifinedMeasureRules {
    public static Predicate<Character> isHanguel = ch -> {
        if (0x1100 <= ch && ch <= 0x11FF) return true;
        if (0x3130 <= ch && ch <= 0x318F) return true;
        if (0xAC00 <= ch && ch <= 0xD7A3) return true;
        return false;
    };
    
    public static Predicate<Character> get(String name) {
        switch (name) {
            case "hangul":
            case "hangeul":
                return isHanguel;
        }
        
        return ch -> false;
    }
}
