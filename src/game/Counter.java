package game;

/**
 * Counter is a simple class that is used for counting things.
 *
 * 
 */
public class Counter {
    private int count;

    /**
     * Instantiates a new Counter.
     *
     * @param value the value
     */
    public Counter(int value) {
        count = value;
    }

    /**
     * Increase.
     * adds a number to the current count.
     *
     * @param number the added number
     */
    public void increase(int number) {
        count += number;
    }

    /**
     * Decrease.
     * Subtracting a number from the current count.
     *
     * @param number the number
     */

    public void decrease(int number) {
        count -= number;
    }

    /**
     * Gets the current count.
     *
     * @return the value of the count
     */

    public int getValue() {
        return count;
    }
}