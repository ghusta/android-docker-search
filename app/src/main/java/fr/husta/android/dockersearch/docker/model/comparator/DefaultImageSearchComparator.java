package fr.husta.android.dockersearch.docker.model.comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;

import fr.husta.android.dockersearch.docker.model.ImageSearchResult;

/**
 * 1. By Official = true
 * 2. By Stars
 */
public class DefaultImageSearchComparator
        implements Comparator<ImageSearchResult>
{

    @Override
    public int compare(ImageSearchResult lhs, ImageSearchResult rhs)
    {
        return new CompareToBuilder()
                .append(rhs.isOfficial(), lhs.isOfficial())
                .append(rhs.getStarCount(), lhs.getStarCount())
                .toComparison();
    }

}
