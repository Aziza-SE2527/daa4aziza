package algorithms.dp;

import java.util.Objects;

/** Length of a longest common subsequence, using a full DP table. */
public class LCS
{
    public static int compute(String a,String b)
    {
        Objects.requireNonNull(a, "a");
        Objects.requireNonNull(b, "b");
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
}
