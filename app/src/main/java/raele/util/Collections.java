package raele.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by lpr on 21/09/15.
 */
public class Collections {

    public static <A, B> Collection<B> map(Collection<A> from, Mapper<A, B> mapper) {
        return map(from, new ArrayList<B>(from.size()), mapper);
    }

    public static <A, B, CollectionType extends Collection<B>> CollectionType map(
            Collection<A> from, CollectionType container, Mapper<A, B> mapper) {
        container.clear();
        for (A a : from) {
            container.add(mapper.map(a));
        }
        return container;
    }

    public interface Mapper<A, B> {
        B map(A a);
    }

}
