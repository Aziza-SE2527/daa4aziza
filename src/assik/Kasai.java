package assik;

import java.util.Arrays;

public class Kasai
{

    // O(n) LCP construction
    public static int[] buildLCP(String s, int[] sa)
    {
        int n= s.length();
        int[] lcp= new int[n];
        int[] rank= new int[n];

        for (int i=0;i<n;i++) rank[sa[i]] = i;

        int k= 0;
        for (int i = 0; i < n; i++)
        {
            if (rank[i] == n - 1)
            {
                k = 0;
                continue;
            }
            int j = sa[rank[i] + 1]; // next suffix
            while (i + k < n && j + k < n && s.charAt(i + k) == s.charAt(j + k))
            {
                k++;
            }
            lcp[rank[i]] = k;
            if (k > 0) k--;
        }
        return lcp;
    }

    // helper SA builder
    public static int[] buildSA(String s)
    {
        int n=s.length();
        Integer[] idx= new Integer[n];
        for (int i=0;i<n;i++) idx[i]= i;

        Arrays.sort(idx, (a, b) -> s.substring(a).compareTo(s.substring(b)));

        int[] sa=new int[n];
        for (int i=0;i<n;i++) sa[i]= idx[i];
        return sa;
    }
}