import org.apache.commons.collections4.*;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.collections4.trie.PatriciaTrie;

import java.util.*;

public class CollectionsExamples {
    public static void main(String[] args) {
        bidiMapExample();
        multiMapExample();
        multiSetExample();
        queueExample();
        trieExample();
        lruMapExample();
        multiKeyMapExample();
        iterableUtilsExample();
        iteratorUtilsExample();
        listUtilsExample();
        setUtilsExample();
    }

    public static void bidiMapExample() {
        System.out.println("\nExamples of BidiMap");
        System.out.println("-------------------");

        BidiMap<String, Integer> bidiMap = new DualHashBidiMap<>();
        // BidiMap<String, Integer> bidiMap = new DualLinkedHashBidiMap<>();
        // BidiMap<String, Integer> bidiMap = new DualTreeBidiMap<>();
        // BidiMap<String, Integer> bidiMap = new TreeBidiMap<>();

        bidiMap.put("string-id-1", 1);
        bidiMap.put("string-id-3", 3);
        bidiMap.put("string-id-2", 2);

        System.out.println(String.format(
                "Using key '%s' to retrieve value %d", "string-id-1", bidiMap.get("string-id-1")));
        System.out.println(String.format(
                "Using value %d to retrieve key '%s'", 1, bidiMap.getKey(1)));

        for (Map.Entry<String, Integer> entry : bidiMap.entrySet())
            System.out.println(String.format(
                    "Key: '%s', value: %d", entry.getKey(), entry.getValue()));

        for (Map.Entry<Integer, String> entry : bidiMap.inverseBidiMap().entrySet())
            System.out.println(String.format(
                    "Key: %d, value: '%s'", entry.getKey(), entry.getValue()));

        System.out.println("\nBidiMap with repeated values");

        BidiMap<String, Integer> badBidiMap = new DualHashBidiMap<>();
        badBidiMap.put("string-id-1", 1);
        badBidiMap.put("string-id-2", 1);

        for (Map.Entry<String, Integer> entry : badBidiMap.entrySet())
            System.out.println(String.format(
                    "Key: '%s', value: %d", entry.getKey(), entry.getValue()));

        for (Map.Entry<Integer, String> entry : badBidiMap.inverseBidiMap().entrySet())
            System.out.println(String.format(
                    "Key: %d, value: '%s'", entry.getKey(), entry.getValue()));
    }

    public static void multiMapExample() {
        System.out.println("\nExamples of MultiMap");
        System.out.println("--------------------");

        ArrayListValuedHashMap<String, String> multiMap = new ArrayListValuedHashMap<>();
        // ArrayListValuedHashMap<String, String> multiMap = new HashSetValuedHashMap<>();

        multiMap.put("language", "Java");
        multiMap.put("language", "Python");
        multiMap.put("language", "C++");
        multiMap.put("skill", "Good");
        multiMap.put("skill", "Excellent");

        System.out.println(String.format(
                "The map has %s elements", multiMap.size()));
        System.out.println(String.format(
                "Keys in the map (with counts): %s", multiMap.keys()));
        System.out.println(String.format(
                "Unique keys in the map: %s", multiMap.keySet()));
        System.out.println(String.format(
                "Values in the map: %s", multiMap.values()));

        for (String key : multiMap.keySet())
            System.out.println(String.format(
                    "Key '%s' contains values: %s", key, String.join(", ", multiMap.get(key))));
    }

    public static void multiSetExample() {
        System.out.println("\nExamples of MultiSet");
        System.out.println("--------------------");

        MultiSet<String> multiSet = new HashMultiSet<>();

        multiSet.add("Java");
        multiSet.add("programming");
        multiSet.add("Python");
        multiSet.add("programming");

        System.out.println(String.format(
                "There are %d elements in the multiset", multiSet.size()));
        System.out.println(String.format(
                "There are %d unique elements in the multiset", multiSet.uniqueSet().size()));

        System.out.println("Iterate the MultiSet entries");
        for (MultiSet.Entry s : multiSet.entrySet())
            System.out.println(String.format("\t'%s' appears %d time(s)", s.getElement(), s.getCount()));

        System.out.println("Iterate only the set elements");
        for (String s : multiSet.uniqueSet())
            System.out.println(String.format("\t'%s' appears %d time(s)", s, multiSet.getCount(s)));
    }

    public static void queueExample() {
        System.out.println("\nExamples of CircularFifoQueue");
        System.out.println("-----------------------------");

        CircularFifoQueue<String> queue = new CircularFifoQueue<>(2);

        queue.add("A");
        System.out.println(String.format(
                "Elements: %d out of %d, queue at full capacity?: %b",
                queue.size(), queue.maxSize(), queue.isAtFullCapacity()));

        queue.add("B");
        System.out.println(String.format(
                "Elements: %d out of %d, queue at full capacity?: %b",
                queue.size(), queue.maxSize(), queue.isAtFullCapacity()));

        System.out.println(new ArrayList<>(queue));

        queue.add("C");
        System.out.println(String.format(
                "Elements: %d out of %d, queue at full capacity?: %b, queue full?: %b",
                queue.size(), queue.maxSize(), queue.isAtFullCapacity(), queue.isFull()));
        System.out.println(new ArrayList<>(queue));

    }

    public static void trieExample() {
        System.out.println("\nExamples of Trie");
        System.out.println("----------------");

        Trie<String, Integer> trie = new PatriciaTrie<>();

        trie.put("car", 1);
        trie.put("cart", 2);
        trie.put("carton", 3);
        trie.put("cartridge", 4);
        trie.put("cartography", 5);
        trie.put("caravan", 6);
        trie.put("carbon", 7);
        trie.put("castle", 8);
        trie.put("coal", 9);

        for (String prefix : Arrays.asList("c", "ca", "car", "cart", "carto"))
            System.out.println(String.format(
                    "Values for prefix '%s': %s", prefix, trie.prefixMap(prefix)));

        System.out.println(String.format(
                "Keys before 'cart': %s", trie.headMap("cart")));
        System.out.println(String.format(
                "Keys after 'cart': %s", trie.tailMap("cart")));
        System.out.println(String.format(
                "Keys between 'cart' and 'carton': %s", trie.subMap("cart", "carton")));
    }

    public static void lruMapExample() {
        System.out.println("\nExamples of LRUMap");
        System.out.println("-------------------");

        LRUMap<String, Integer> lruMap = new LRUMap<>(2);

        lruMap.put("value1", 1);
        lruMap.put("value2", 2);

        System.out.println(String.format(
                "Elements in the map: %s", lruMap.entrySet()));

        lruMap.put("value3", 3);

        System.out.println(String.format(
                "Elements in the map: %s", lruMap.entrySet()));

        lruMap.get("value2");
        lruMap.put("value4", 4);

        System.out.println(String.format(
                "Elements in the map: %s", lruMap.entrySet()));
    }

    public static void multiKeyMapExample() {
        System.out.println("\nExamples of MultiKeyMap");
        System.out.println("-----------------------");

        MultiKeyMap<String, String> multiKeyMap = new MultiKeyMap<>();

        multiKeyMap.put("John", "Doe", "id1");
        multiKeyMap.put("Jane", "Doe", "id2");

        System.out.println(String.format(
                "Using key '%s': %s", "(\"John\", \"Doe\")", multiKeyMap.get("John", "Doe")));
        System.out.println(String.format(
                "Using key '%s': %s", "(\"Jane\", \"Doe\")", multiKeyMap.get("Jane", "Doe")));
    }

    public static void iterableUtilsExample() {
        System.out.println("\nExamples of IterableUtils");
        System.out.println("-------------------------");

        List<String> list1 = Arrays.asList("one", "two", "three");
        List<String> list2 = Arrays.asList("A", "B", "C");

        for (String s: IterableUtils.chainedIterable(list1, list2))
            System.out.print(s + " ");

        System.out.println();

        for (String s: IterableUtils.zippingIterable(list1, list2))
            System.out.print(s + " ");

        System.out.println();
    }

    public static void iteratorUtilsExample() {
        System.out.println("\nExamples of IteratorUtils");
        System.out.println("-------------------------");

        List<String> list1 = Arrays.asList("one", "two", "three");
        List<String> list2 = Arrays.asList("A", "B", "C");

        Iterator<String> chainedIterator = IteratorUtils.chainedIterator(list1.iterator(), list2.iterator());
        while (chainedIterator.hasNext())
            System.out.print(chainedIterator.next() + " ");

        System.out.println();

        Iterator<String> zippingIterator = IteratorUtils.zippingIterator(list1.iterator(), list2.iterator());
        while (zippingIterator.hasNext())
            System.out.print(zippingIterator.next() + " ");

        System.out.println();
    }

    public static void listUtilsExample() {
        System.out.println("\nExamples of ListUtils");
        System.out.println("---------------------");

        List<String> list1 = new ArrayList<>(Arrays.asList("one", "two", "three"));
        List<String> list2 = new ArrayList<>(Arrays.asList("one", "two", "four"));

        System.out.println(String.format(
                "Union: %s", ListUtils.union(list1, list2)));
        System.out.println(String.format(
                "Intersection: %s", ListUtils.intersection(list1, list2)));
        System.out.println(String.format(
                "Sum: %s", ListUtils.sum(list1, list2)));
        System.out.println(String.format(
                "Subtract: %s", ListUtils.subtract(list1, list2)));

        System.out.println(String.format(
                "Longest common subsequence: %s", ListUtils.longestCommonSubsequence(list1, list2)));

        Factory<Integer> factory = () -> new Random().nextInt();

        List<Integer> lazyList = ListUtils.lazyList(new ArrayList<>(), factory);
        System.out.println(String.format(
                "Lazy list length: %d", lazyList.size()));

        lazyList.get(2);
        System.out.println(String.format(
                "Lazy list length: %d", lazyList.size()));
        System.out.println(String.format(
                "First element: %d", new ArrayList<>(lazyList).get(0)));
    }

    public static void setUtilsExample() {
        System.out.println("\nExamples of SetUtils");
        System.out.println("--------------------");

        Set<String> set1 = new HashSet<>(Arrays.asList("one", "two", "three"));
        Set<String> set2 = new HashSet<>(Arrays.asList("one", "four"));

        System.out.println(String.format(
                "Union: %s", SetUtils.union(set1, set2)));
        System.out.println(String.format(
                "Intersection: %s", SetUtils.intersection(set1, set2)));
        System.out.println(String.format(
                "Disjunction: %s", SetUtils.disjunction(set1, set2)));
        System.out.println(String.format(
                "Difference: %s", SetUtils.difference(set1, set2)));
    }
}
