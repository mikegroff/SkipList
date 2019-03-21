public class TestCase17 {
    public static void main(String [] args)
    {
        // This test case tests if your skiplist is growing properly
        int total7 = 0;
        int total8 = 0;
        for (int w = 0; w < 1000; w++)
        {
            SkipList<Integer> skiplist = new SkipList<Integer>(7);

            // Insert just under 128 elements. All inserted elements have a height of 7.
            // Note: this kinda ruins the whole point of a skiplist with everything
            // the same height, so it won't have good runtimes.
            for (int i = 0; i < 128 - 2; i++)
                skiplist.insert(RNG.getUniqueRandomInteger(), 7);

            // Add four more elements, causing the height of the SkipList to be increased
            for (int i = 0; i < 4; i++)
                skiplist.insert(RNG.getUniqueRandomInteger());

            Node<Integer> current = skiplist.head();
            int[] counts = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            while (current != null) {
                counts[current.height()]++;
                current = current.next(0);
            }
            total7 += counts[7];
            total8 += counts[8];
        }

        // Make sure that it split 50/50 to increase the node height.
        double diff = Math.abs(total7 / 1000.0 - total8 / 1000.0);
        System.out.println((diff < 3) ? "Hooray!" : "fail whale :(");
    }


}
