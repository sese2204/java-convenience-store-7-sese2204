package store.domain;

import java.time.LocalDate;
public class Promotion {
    private final String name;
    private final int buyCount;
    private final int freeCount;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(final String name, final int buyCount, final int freeCount, final LocalDate startDate, final LocalDate endDate) {
        this.name = name;
        this.buyCount = buyCount;
        this.freeCount = freeCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public int getFreeCount() {
        return freeCount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
