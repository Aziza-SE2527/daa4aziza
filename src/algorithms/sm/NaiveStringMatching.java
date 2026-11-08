package algorithms.sm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** All occurrences, including overlaps; an empty pattern returns no matches. */
public class NaiveStringMatching
{
    // Counts from the most recent call; used in single-threaded experiments.
    public static long characterComparisons;

    public static List<Integer> search(String t, String p)
    {
        characterComparisons= 0;
        Objects.requireNonNull(t, "text");
        Objects.requireNonNull(p, "pattern");
        List<Integer> res= new ArrayList<>();
        int n= t.length(),m= p.length();
        if (m==0||m > n) return res;

        for (int i= 0;i <=n- m; i++)
        {
            int j= 0;
            while (j< m)
            {
                characterComparisons++;
                if (t.charAt(i+ j) != p.charAt(j)) break;
                j++;
            }
            if (j==m) res.add(i);
        }
        return res;
    }
}
