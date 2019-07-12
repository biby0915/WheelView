package com.zby.wheelview.source;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-07-09
 */
public class NumberDataHolder<T extends Number> implements DataHolder<T> {
    private Number min;
    private Number max;
    private Number step;
    private boolean includeLast;

    private int size;

    public NumberDataHolder(Number min, Number max, Number step, boolean includeLast) {
        this.min = min;
        this.max = max;
        this.step = step;
        this.includeLast = includeLast;

        if (min.getClass() != max.getClass()
                || step.getClass() != min.getClass()
                || step.getClass() != max.getClass()) {
            throw new IllegalArgumentException("The parameter type must be the same");
        }

        if (min instanceof Integer) {
            size = (max.intValue() - min.intValue()) / step.intValue() + 1;
            if (includeLast && (max.intValue() - min.intValue()) % step.intValue() > 0) {
                size++;
            }
        } else if (min instanceof Float) {
            size = (int) ((max.floatValue() - min.floatValue()) / step.floatValue() + 1);
            if (includeLast && max.floatValue() > (min.floatValue() + step.floatValue() * size)) {
                size++;
            }
        } else if (min instanceof Double) {
            size = (int) ((max.doubleValue() - min.doubleValue()) / step.doubleValue() + 1);
            if (includeLast && max.doubleValue() > (min.doubleValue() + step.doubleValue() * size)) {
                size++;
            }
        } else {
            throw new UnsupportedOperationException("parameter type not supported");
        }

    }

    @Override
    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int position) {
        Number v = null;
        if (min instanceof Integer) {
            v = min.intValue() + step.intValue() * position;
            if (v.intValue() > max.intValue()) {
                v = max.intValue();
            }
        } else if (min instanceof Float) {
            v = min.floatValue() + step.floatValue() * position;
            if (v.floatValue() > max.floatValue()) {
                v = max.floatValue();
            }
        } else if (min instanceof Double) {
            v = min.doubleValue() + step.doubleValue() * position;
            if (v.doubleValue() > max.doubleValue()) {
                v = max.doubleValue();
            }
        }
        return (T) v;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> toList() {
        List<Number> list = new ArrayList<>();
        if (min instanceof Integer) {
            for (int i = min.intValue(); i < max.intValue(); i += step.intValue()) {
                list.add(i);
            }

            if (includeLast && size > list.size()) {
                list.add(max.intValue());
            }
        } else if (min instanceof Float) {
            for (float i = min.floatValue(); i < max.floatValue(); i += step.floatValue()) {
                list.add(i);
            }

            if (includeLast && size > list.size()) {
                list.add(max.floatValue());
            }
        } else if (min instanceof Double) {
            for (double i = min.doubleValue(); i < max.doubleValue(); i += step.doubleValue()) {
                list.add(i);
            }

            if (includeLast && size > list.size()) {
                list.add(max.doubleValue());
            }
        }
        return (List<T>) list;
    }
}
