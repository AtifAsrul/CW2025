package com.comp2042;

/**
 * Data class representing the result of a down movement or drop.
 * aggregated the cleared row information and the current view data.
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    /**
     * Constructs a new DownData object.
     *
     * @param clearRow the cleared row information (if any).
     * @param viewData the current view data.
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Gets the cleared row information.
     *
     * @return the ClearRow object.
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Gets the view data.
     *
     * @return the ViewData object.
     */
    public ViewData getViewData() {
        return viewData;
    }
}
