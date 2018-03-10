package com.midasit.androidcustomviews;

/**
 * Created by nyh0111 on 2018-02-28.
 */

public class ResizeConstraint {
    
    private Rule rule;
    private int baseLength;
    private int targetSize;

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
    
    public int getBaseLength() {
        return baseLength;
    }
    
    public void setBaseLength(int baseLength) {
        this.baseLength = baseLength;
    }
    
    public int getTargetSize() {
        return targetSize;
    }
    
    public void setTargetSize(int targetSize) {
        this.targetSize = targetSize;
    }
    
    public enum Rule {
        BELOW("<", (p1, p2) -> p1 < p2), ABOVE(">", (p1, p2) -> p1 > p2), NONE("", null);
        
        private String condition;
        private Test test;
        
        Rule(String condition, Test test) {
            this.condition = condition;
            this.test = test;
        }
        
        public static Rule of(String condition) {
            Rule[] values = values();
            
            for (Rule value : values)
                if (value != NONE && value.condition.equals(condition))
                    return value;
            
            return NONE;
        }
        
        public boolean apply(int baseLength, int targetSize) {
            return test.apply(baseLength, targetSize);
        }
    }
    
    public interface Test {
        boolean apply(int param1, int param2);
    }
}
