package assik;

public class DP
{

    // LCS length
    public static int lcs(String a,String b)
    {
        if (a==null||b==null) return 0;
        int n= a.length(), m = b.length();
        int[][] dp= new int[n+1][m+1];

        for (int i=1;i<=n;i++)
        {
            for (int j=1;j<= m;j++)
            {
                // chars match
                if (a.charAt(i - 1) == b.charAt(j - 1))
                {
                    dp[i][j]=dp[i - 1][j - 1]+1;
                }
                else
                {
                    // max of skipping either
                    dp[i][j]= Math.max(dp[i- 1][j],dp[i][j- 1]);
                }
            }
        }
        return dp[n][m];
    }

    // Levenshtein distance
    public static int editDist(String a, String b)
    {
        if (a== null||b ==null) return 0;
        int n= a.length(),m = b.length();
        int[][] dp=new int[n + 1][m + 1];

        // base cases
        for (int i = 0;i<= n;i++)dp[i][0] = i;
        for (int j = 0;j<= m;j++)dp[0][j] = j;

        for (int i = 1; i <= n; i++)
        {
            for (int j = 1; j <= m; j++)
            {
                // same char
                if (a.charAt(i- 1)==b.charAt(j - 1))
                {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // min of del, ins, sub
                    int del= dp[i- 1][j];
                    int ins= dp[i][j- 1];
                    int sub= dp[i- 1][j- 1];
                    dp[i][j] = 1 + Math.min(del, Math.min(ins, sub));
                }
            }
        }
        return dp[n][m];
    }
}