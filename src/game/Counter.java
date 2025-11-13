package game;


public class Counter {
    private int count;


    public Counter(int value) {
        count = value;
    }


    public void increase(int number) {
        count += number;
    }



    public void decrease(int number) {
        count -= number;
    }



    public int getValue() {
        return count;
    }
}