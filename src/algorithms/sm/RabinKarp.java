package algorithms.sm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** All occurrences, including overlaps; an empty pattern returns no matches. */
public class RabinKarp
{
    // Counts from the most recent call; used in single-threaded experiments.
    public static long characterComparisons;
    public static long hashComparisons;

    public static List<Integer> search(String t, String p)
    {
        characterComparisons = 0;
        hashComparisons = 0;
        Objects.requireNonNull(t, "text");
        Objects.requireNonNull(p, "pattern");
        List<Integer> res= new ArrayList<>();
        int n = t.length(),m = p.length();
        if (m == 0||m > n) return res;

        long b= 256,mod= 1_000_000_007;
        long pHash= 0, tHash = 0, pow = 1;

        for (int i=0;i < m-1;i++) pow = (pow * b) % mod;

        // first window hash
        for (int i= 0;i < m; i++)
        {
            pHash= (b * pHash + p.charAt(i)) % mod;
            tHash= (b * tHash + t.charAt(i)) % mod;
        }

        for (int i= 0; i <= n - m; i++)
        {
            // hash match -> verify chars
            hashComparisons++;
            if (pHash == tHash)
            {
                boolean ok= true;
                for (int j=0; j < m; j++)
                {
                    characterComparisons++;
                    if (t.charAt(i+ j) != p.charAt(j))
                    {
                        ok= false;
                        break;
                    }
                }
                if (ok) res.add(i);
            }

            // roll hash
            if (i<n - m)
            {
                tHash= (tHash - t.charAt(i) * pow) % mod;
                tHash= (tHash * b + t.charAt(i + m)) % mod;
                if (tHash< 0) tHash+= mod;
            }
        }
        return res;
    }
}
