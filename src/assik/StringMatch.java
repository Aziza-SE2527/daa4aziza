package assik;

import java.util.ArrayList;
import java.util.List;

public class StringMatch
{

    // op counter
    public static long comps = 0;

    // 1. Naive
    public static List<Integer> naive(String t, String p)
    {
        comps= 0;
        List<Integer> res= new ArrayList<>();
        int n= t.length(),m= p.length();
        if (m==0||m > n) return res;

        for (int i= 0;i <=n- m; i++)
        {
            int j= 0;
            while (j< m)
            {
                comps++;
                if (t.charAt(i+ j) != p.charAt(j)) break;
                j++;
            }
            if (j==m) res.add(i);
        }
        return res;
    }

    // 2. KMP
    public static List<Integer> kmp(String t,String p)
    {
        comps= 0;
        List<Integer> res= new ArrayList<>();
        int n = t.length(),m=p.length();
        if (m== 0 ||m >n) return res;

        int[] lps= getLPS(p);
        int i=0,j = 0;

        while (i< n)
        {
            comps++;
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

    // LPS table
    private static int[] getLPS(String p)
    {
        int m= p.length(),len= 0, i = 1;
        int[] lps= new int[m];

        while (i< m)
        {
            comps++;
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

    // 3. Rabin-Karp
    public static List<Integer> rk(String t, String p)
    {
        comps= 0;
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
            if (pHash == tHash)
            {
                boolean ok= true;
                for (int j=0; j < m; j++)
                {
                    comps++;
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