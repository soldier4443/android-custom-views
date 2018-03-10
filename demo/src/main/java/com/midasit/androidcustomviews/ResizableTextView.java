package com.midasit.androidcustomviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nyh0111 on 2018-02-28.
 */

public class ResizableTextView extends AppCompatTextView {
    
    private List<ResizeConstraint> constraints = new ArrayList<>();
    private Predicate<Character> specialMeasureRule;
    
    public ResizableTextView(Context context) {
        super(context);
    }
    
    public ResizableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    public ResizableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ResizableTextView, 0, 0);
            
            String measureRules = attributes.getString(R.styleable.ResizableTextView_measureRule);
            processMeasureRules(measureRules);
            
            String resizeRules = attributes.getString(R.styleable.ResizableTextView_resizeRule);
            processResizeRules(resizeRules);
            
            attributes.recycle();
        }
    }
    
    private void processMeasureRules(String attr) {
        String[] rules = attr.split("\\|");
        
        for (int i = 0; i < rules.length; i++) {
            if (specialMeasureRule == null) {
                setMeasureRule(PredifinedMeasureRules.get(rules[i]));
            } else {
                addMeasureRule(PredifinedMeasureRules.get(rules[i]));
            }
        }
    }
    
    private void processResizeRules(String attr) {
        String[] rules = attr.split("\\|");
    
        for (int i = 0; i < rules.length; i++) {
            ResizeConstraint constraint = decodeResizeRule(rules[i]);
            
            if (constraint != null) {
                constraints.add(constraint);
            }
        }
    }
    
    private ResizeConstraint decodeResizeRule(String rule) {
        String[] value = rule.split(",");
        String condition = value[0];
        float baseLength = Float.valueOf(value[1]);
        float targetSize = Float.valueOf(value[2]);
        
        ResizeConstraint constraint = new ResizeConstraint();
        
        constraint.setRule(ResizeConstraint.Rule.valueOf(condition));
        constraint.setBaseLength((int) baseLength);
        constraint.setTargetSize((int) targetSize);
        
        return constraint;
    }
    
    private int findFirstIndexOfNumberInString(String string) {
        int length = string.length();
    
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(string.charAt(i))) {
                return i;
            }
        }
        
        return -1;
    }
    
    
    public ResizableTextView addRule(String condition, int length, int sizeInSp) {
        ResizeConstraint constraint = new ResizeConstraint();
        
        constraint.setRule(ResizeConstraint.Rule.of(condition));
        constraint.setBaseLength(length);
        constraint.setTargetSize(sizeInSp);
        
        constraints.add(constraint);
        
        return this;
    }
    
    public ResizableTextView setMeasureRule(Predicate<Character> specialMeasureRule) {
        this.specialMeasureRule = specialMeasureRule;
        return this;
    }
    
    public ResizableTextView addMeasureRule(Predicate<Character> specialMeasureRule) {
        this.specialMeasureRule = Predicate.Util.and(this.specialMeasureRule, specialMeasureRule);
        return this;
    }
    
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        
        if (constraints == null)
            return;
        
        if (specialMeasureRule == null) {
            specialMeasureRule = PredifinedMeasureRules.get("");
        }
        
        int length = TextSizeMesurements.calculateLengthReasonably(text.toString(), specialMeasureRule);
        
        // Below인 것만 가져와서 오름차순으로 정렬하고
        // 테스트를 해봤을 때 가장 먼저 나오는 값을 가져옴.
        // 만약 있으면 testSize로 값을 적용하고 없으면 가만히 있음.
        Stream.of(constraints)
            .filter(constraint -> constraint.getRule().equals(ResizeConstraint.Rule.BELOW))
            .sorted((o1, o2) -> Integer.compare(o1.getBaseLength(), o2.getBaseLength()))
            .filter(constraint -> constraint.getRule().apply(length, constraint.getBaseLength()))
            .findFirst()
            .ifPresent(it -> setTextSize(it.getTargetSize()));
    }
}
