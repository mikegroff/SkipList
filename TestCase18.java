import java.util.ArrayList;

public class TestCase18
{
    public static void main(String [] args)
    {
        // This test case tests if your .get() method is actually O(logn)
        // This should take a few seconds (I'd be worried if it takes more than
        // a minute)
        SkipList<Integer> skiplist = new SkipList<Integer>();

        int elementCount = 0;
        long totalTime, start, end;
        ArrayList<Double> times = new ArrayList<>();
        for (int i = 5; i < 15; i++)
        {
            // Insert 100,000 elements.
            skiplist = addElements(skiplist, (int)Math.pow(2, i));
            elementCount+= (int)Math.pow(2, i);

            start = System.currentTimeMillis();
            getAMilli(skiplist);
            end = System.currentTimeMillis();
            totalTime = end - start;

            // Un-comment out this line to graph the times yourself (in excel,
            // pick scatterplot) and see if the trend is like a log function
            // System.out.println(elementCount + "\t" + (totalTime));

            times.add((double)totalTime);
        }

        // Check 1: if we add 4x number of elements, the time shouldn't go up
        // by that much (probs a better way to test this but idk)
        // Mine averaged a rate around 1.4
        System.out.println((times.get(7) / times.get(5) < 2) ? "Hooray!" : "fail whale :(");

        // Check 2: if we add 1,000x elements, time shouldn't go up by that much
        // I averaged around 4-7 for this one
        System.out.println((times.get(9) / times.get(0) < 10) ? "Hooray!" : "fail whale :(");

    }

    private static SkipList<Integer> addElements(SkipList<Integer> skiplist, int num)
    {
        // insert num random elements (not using RNG bc it gets slow with a lot
        // of numbers bc collisions)
        for (int i = 0; i < num; i++)
            skiplist.insert((int)(Math.random() * 1e9));
        return skiplist;
    }

    private static void getAMilli(SkipList<Integer> skiplist)
    {
        // Find 1,000,000 random numbers (not using RNG here bc it's v slow)
        for (int i = 0; i < 1000000; i++)
        {
            int toFind = (int) (Math.random() * 1e9);
            Node element = skiplist.get(toFind);

        }
    }

}
