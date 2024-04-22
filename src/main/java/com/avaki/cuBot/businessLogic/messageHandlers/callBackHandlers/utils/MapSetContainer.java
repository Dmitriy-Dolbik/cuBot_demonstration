package com.avaki.cuBot.businessLogic.messageHandlers.callBackHandlers.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MapSetContainer<S, T> {

    public final Map<S, Set<T>> map = new ConcurrentHashMap<>();

    public void put(S key , T object) {
        map.computeIfAbsent(key, ignore -> new HashSet<>(Collections.singletonList(object)));

        Set<T> objectsSet = map.get(key);
        if (objectsSet == null) {
            map.put(key, new HashSet<>(Collections.singletonList(object)));
        } else {
            objectsSet.add(object);
        }
    }

    public void put(S key, Set<T> valuesSet) {
        map.put(key, valuesSet);
    }

    public Set<T> remove(S key) {
        return map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set<T> get(S key) {
        return map.get(key);
    }

    public int size() {
        return map.size();
    }
}
