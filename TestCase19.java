import java.util.ArrayList;

public class TestCase19
{
    // This makes sure your skiplist links all the way through on all levels
    public static void main(String [] args)
    {
        // Create skiplist with 100,000 random elements
        SkipList<Integer> skiplist = new SkipList<Integer>();
        for (int i = 0; i < 100000; i++)
            skiplist.insert((int)(Math.random() * 1e9));

        // Check 1: it should have a height of 17
        // If you fail this you'll probably wreck the rest of the test cases.
        System.out.println((skiplist.height() == 17) ? "Hooray!" : "fail whale :(");

        int[] heights = new int[17];
        Node current = skiplist.head();
        while (current != null)
        {
            heights[current.height() - 1]++;
            current = current.next(0);
        }

        for (int i = 0; i < 17; i++)
        {
            // Checks 2-18: Make sure it's linked correctly on all levels
            current = skiplist.head();
            int count = 0;
            while (current != null)
            {
                count++;
                current = current.next(i);
            }
            System.out.println((count == sumSubArray(heights, i)) ? "Hooray!" : "fail whale :(");
        }
    }

    private static int sumSubArray(int[] array, int level)
    {
        int total = 0;
        for (int i = level; i < array.length; i++)
        {
            total += array[i];
        }
        return total;
    }
}
