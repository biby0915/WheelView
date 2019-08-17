package com.zby.wheelview.source;

import java.util.List;

/**
 * @author ZhuBingYang
 * @date 2019-07-09
 */
public class ListDataHolder<T> implements DataHolder<T> {

    private List<T> mDataList;

    public ListDataHolder(List<T> data) {
        this.mDataList = data;
    }

    @Override
    public int size() {
        return mDataList.size();
    }

    @Override
    public T get(int position) {
        return mDataList.get(position);
    }

    @Override
    public boolean isEmpty() {
        return mDataList.isEmpty();
    }

    @Override
    public List<T> toList() {
        return mDataList;
    }

    @Override
    public int indexOf(T item) {
        return mDataList.indexOf(item);
    }
}
