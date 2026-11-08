package algorithms.sm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** All occurrences, including overlaps; an empty pattern returns no matches. */
public class KMP
{
    // Counts from the most recent call; used in single-threaded experiments.
    public static long characterComparisons;
    public static long preprocessingComparisons;

    public static List<Integer> search(String t,String p)
    {
        characterComparisons = 0;
        preprocessingComparisons = 0;
        Objects.requireNonNull(t, "text");
        Objects.requireNonNull(p, "pattern");
        List<Integer> res= new ArrayList<>();
        int n = t.length(),m=p.length();
        if (m== 0 ||m >n) return res;

        int[] lps= getLPS(p);
        int i=0,j = 0;

        while (i< n)
        {
            characterComparisons++;
            if (t.charAt(i)== p.charAt(j))
            {
                i++;
                j++;
                if(j==m)
                {
                    res.add(i- j);
                    j = lps[j- 1]; // shift
                }
            }
            else
            {
                if (j != 0) j =lps[j-1]; // fallback
                else i++;
            }
        }
        return res;
    }

    private static int[] getLPS(String p)
    {
        int m= p.length(),len= 0, i = 1;
        int[] lps= new int[m];

        while (i< m)
        {
            preprocessingComparisons++;
            if (p.charAt(i)==p.charAt(len))
            {
                lps[i++]= ++len;
            }
            else
            {
                if (len != 0) len=lps[len - 1];
                else lps[i++] = 0;
            }
        }
        return lps;
    }
}
