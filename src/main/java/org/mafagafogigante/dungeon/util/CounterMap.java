package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * CounterMap class that maps a generic key to an integer and provides methods manipulate this integer. Implements
 * Iterable sorted by descending values.
 */
public class CounterMap<K> implements Serializable, Iterable<K> {

  private static final long serialVersionUID = Version.MAJOR;
  // The wrapped HashMap.
  private final HashMap<K, Integer> map = new HashMap<>();

  /**
   * Constructs a new empty CounterMap.
   */
  public CounterMap() {
  }

  /**
   * Returns a Set view of the keys contained in this map.
   */
  public Set<K> keySet() {
    return map.keySet();
  }

  public boolean isNotEmpty() {
    return !map.isEmpty();
  }

  /**
   * Increments the count of a given key in the CounterMap by 1.
   *
   * <p>If the key does not exist, it will be created an assigned a value of 1.
   */
  public void incrementCounter(K key) {
    incrementCounter(key, 1);
  }

  /**
   * Increments the count of a given key in the CounterMap by a given amount.
   *
   * <p>If the key does not exist, it will be created an assigned the added value.
   */
  public void incrementCounter(K key, Integer amount) {
    Integer counter = map.get(key);
    if (counter == null) {
      counter = amount;
    } else {
      counter = counter + amount;
    }
    map.put(key, counter);
  }

  /**
   * Retrieves the counter mapped to a certain key. If no counter is mapped to the provided key, 0 will be returned.
   */
  public int getCounter(K key) {
    Integer counter = map.get(key);
    if (counter == null) {
      return 0;
    } else {
      return counter;
    }
  }

  @Override
  public String toString() {
    return String.format("CounterMap{map=%s}", map);
  }

  @Override
  public Iterator<K> iterator() {
    List<Entry<K, Integer>> entryList = new ArrayList<>(map.entrySet());
    Collections.sort(entryList, new Comparator<Entry<K, Integer>>() {
      @Override
      public int compare(Entry<K, Integer> o1, Entry<K, Integer> o2) {
        return o2.getValue().compareTo(o1.getValue());
      }
    });
    List<K> sortedKeyList = new ArrayList<>();
    for (Entry<K, Integer> entry : entryList) {
      sortedKeyList.add(entry.getKey());
    }
    return sortedKeyList.iterator();
  }

}
