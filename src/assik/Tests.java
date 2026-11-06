package assik;

import java.util.Arrays;
import java.util.List;

public class Tests
{

    public static void main(String[] args)
    {
        // LCS tests
        check(DP.lcs("ALGO", "ALGO"), 4, "LCS same");
        check(DP.lcs("AAAA", "BBBB"), 0, "LCS diff");
        check(DP.lcs("ABC", ""), 0, "LCS empty");

        // Edit distance tests
        check(DP.editDist("JAVA", "JAVA"), 0, "ED same");
        check(DP.editDist("HI", ""), 2, "ED empty");
        check(DP.editDist("CAT", "CUT"), 1, "ED sub");
        check(DP.editDist("CAT", "CART"), 1, "ED ins");
        check(DP.editDist("CART", "CAT"), 1, "ED del");

        // String matching tests
        checkMatch("ABABACABABA", "ABA", Arrays.asList(0, 2, 6, 8), "Match multi");
        checkMatch("ABCDEFG", "XYZ", List.of(), "Match absent");
        checkMatch("HI", "HELLO", List.of(), "Match longer");
        checkMatch("TEXT", "", List.of(), "Match empty");

        // Kasai test ("banana")
        int[] sa= {5, 3, 1, 0, 4, 2};
        int[] exp= {1, 3, 0, 0, 2, 0};
        if (!Arrays.equals(Kasai.buildLCP("banana",sa),exp))
        {
            throw new AssertionError("Kasai fail");
        }
        System.out.println("[OK] Kasai LCP");
        System.out.println("All tests passed!");
    }

    private static void check(int act,int exp,String msg)
    {
        if (act != exp) throw new AssertionError(msg + " failed");
        System.out.println("[OK] " + msg);
    }

    private static void checkMatch(String t,String p,List<Integer> exp, String msg)
    {
        List<Integer> r1=StringMatch.naive(t, p);
        List<Integer> r2=StringMatch.kmp(t, p);
        List<Integer> r3=StringMatch.rk(t, p);
        if (!r1.equals(exp) || !r2.equals(r1) || !r3.equals(r1))
        {
            throw new AssertionError(msg + " failed");
        }
        System.out.println("[OK] " + msg);
    }
}