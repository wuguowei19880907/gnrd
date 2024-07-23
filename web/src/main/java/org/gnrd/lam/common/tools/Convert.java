package org.gnrd.lam.common.tools;

import java.util.ArrayList;
import java.util.List;

public class Convert {

    public static <T> List<T> toList(Object object, Class<T> clazz) {
        final List<T> result = new ArrayList<T>();
        if (object instanceof List<?>) {
            for (Object o : (List<?>) object) {
                result.add(clazz.cast(o));
            }
        }
        return result;
    }
}
