package com.zby.wheelview.source;

import java.util.ArrayList;
import java.util.Arrays;
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

    int indexOf(T item);

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
            return new ArrayList<>();
        }

        @Override
        public int indexOf(T item) {
            return 0;
        }
    }

    class DebugHolder<T> implements DataHolder<T> {
        final List<String> data = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4", "Item 5",
                "Item 6", "Item 7", "Item 8", "Item 9");

        @Override
        public int size() {
            return data.size();
        }

        @SuppressWarnings("unchecked")
        @Override
        public T get(int position) {
            return (T) data.get(position);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<T> toList() {
            return (List<T>) data;
        }

        @Override
        public int indexOf(T item) {
            return 0;
        }
    }
}
