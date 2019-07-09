package com.zby.wheelview.source;

import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-07-09
 */
public interface DataHolder<T> {
    int size();

    T get(int position);

    boolean isEmpty();

    List<T> toList();

    class EmptyHolder<T> implements DataHolder<T> {

        @Override
        public int size() {
            return 0;
        }

        @Override
        public T get(int position) {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public List<T> toList() {
            return null;
        }
    }
}
