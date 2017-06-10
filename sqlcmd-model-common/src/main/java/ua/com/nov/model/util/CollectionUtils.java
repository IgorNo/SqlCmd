package ua.com.nov.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CollectionUtils {

    public static String toString(Collection<?> collection, String leftBracket, String rightBracket) {
        StringBuilder sb = new StringBuilder(leftBracket);
        String s = "";
        for (Object o : collection) {
            sb.append(s).append(o.toString());
            s = ", ";
        }
        return sb.append(rightBracket).toString();
    }

    public static String toString(Collection<?> collection) {
        return toString(collection, "", "");
    }

    public static <T> String toString(T[] array, String leftBracket, String rightBracket) {
        StringBuilder sb = new StringBuilder(leftBracket);
        String s = "";
        for (Object o : array) {
            sb.append(s).append(o.toString());
            s = ", ";
        }
        return sb.append(rightBracket).toString();
    }

    public static <T> String toString(T[] array) {
        return toString(array, "", "");
    }

    public static String toString(Map<?,?> map, String entrySeparator, String keyValueSeparator) {
        StringBuilder sb = new StringBuilder();
        String s = "";
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            sb.append(s).append(entry.getKey().toString().trim());
            if (entry.getValue() != null && !entry.getValue().toString().isEmpty())
                sb.append(keyValueSeparator).append(entry.getValue().toString().trim());
            s = entrySeparator;
        }
        return sb.toString();
    }

    public static List<String> toList(Map<?,?> map, String keyValueSeparator, String prefix) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix).append(' ').append(entry.getKey().toString().trim());
            if (entry.getValue() != null && !entry.getValue().toString().isEmpty())
                sb.append(keyValueSeparator).append(entry.getValue().toString().trim());
            result.add(sb.toString());
        }
        return result;
    }

}
