package com.apps.reflection;

import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.regex.Pattern;

public class RandomPattern {

    private final Pattern pattern;
    private final List<Object> values;
    private RandomUtils random = new RandomUtils();

    public RandomPattern(String pattern, List<Object> values) {
        this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        this.values = values;
    }

    public boolean match(String fieldName){
        return pattern.matcher(fieldName).find();
    }

    public Object getRandomValue(){
        return values.get(random.nextInt(0, values.size()));
    }

    public Pattern getPattern() {
        return pattern;
    }

    public List<Object> getValues() {
        return values;
    }

}
