package com.naijaplanet.magosla.android.moviesplanet.data;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class for the results of Models
 * @param <D> the data type
 */
abstract public class Results<D> {
    protected List<D> results;
    protected int page;
    protected int totalPages;
    protected int totalResults;

    public Results(){
        results = new ArrayList<>();
    }

    public List<D> getResults() {
        return results;
    }

    public void setResults(List<D> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

}
