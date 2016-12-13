package fr.husta.android.dockersearch.docker.model.comparator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.husta.android.dockersearch.docker.model.ImageSearchResult;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultImageSearchComparatorTest
{

    @Test
    public void compare() throws Exception
    {
        DefaultImageSearchComparator comparator = new DefaultImageSearchComparator();
        List<ImageSearchResult> list = new ArrayList<>();

        ImageSearchResult elt1 = new ImageSearchResult("elt1", "elt1", true, 10);
        list.add(elt1);

        ImageSearchResult elt2 = new ImageSearchResult("elt2", "elt2", false, 0);
        list.add(elt2);

        ImageSearchResult elt3 = new ImageSearchResult("elt3", "elt3", true, 25);
        list.add(elt3);

        ImageSearchResult elt4 = new ImageSearchResult("elt4", "elt4", false, 3);
        list.add(elt4);

        ImageSearchResult elt5 = new ImageSearchResult("elt5", "elt5", false, 2);
        list.add(elt5);

        Collections.sort(list, comparator);
        assertThat(list).isNotEmpty();
        // #1 => elt3
        assertThat(list.get(0)).isSameAs(elt3);
        // #2 => elt1
        assertThat(list.get(1)).isSameAs(elt1);
        // #3 => elt4
        assertThat(list.get(2)).isSameAs(elt4);
        // #4 => elt5
        assertThat(list.get(3)).isSameAs(elt5);
        // #5 => elt2
        assertThat(list.get(4)).isSameAs(elt2);
    }

}