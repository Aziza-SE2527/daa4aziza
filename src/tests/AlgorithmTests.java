package tests;

import algorithms.dp.LCS;
import algorithms.dp.EditDistance;
import algorithms.sm.NaiveStringMatching;
import algorithms.sm.KMP;
import algorithms.sm.RabinKarp;
import algorithms.aca.Kasai;
import experiments.AlgorithmExperiments;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AlgorithmTests
{
    private static int passed;
    private static int failed;
    private static final StringBuilder summary = new StringBuilder();

    public static void main(String[] args)
    {
        passed = 0;
        failed = 0;
        summary.setLength(0);
        runGroup("LCS", AlgorithmTests::testLCS);
        runGroup("Edit Distance", AlgorithmTests::testEditDistance);
        runGroup("String Matching", AlgorithmTests::testMatching);
        runGroup("Kasai", AlgorithmTests::testKasai);

        System.out.println("TESTING SUMMARY");
        System.out.printf("%-20s %8s %8s%n", "Category", "Passed", "Failed");
        System.out.print(summary);
        System.out.printf("%-20s %8d %8d%n", "Total", passed, failed);
        if (failed > 0) throw new AssertionError(failed + " checks failed");
        System.out.println("All tests passed!");
    }

    private static void testLCS()
    {
        check(LCS.compute("ALGO", "ALGO"), 4, "LCS same");
        check(LCS.compute("AAAA", "BBBB"), 0, "LCS different");
        check(LCS.compute("ABC", ""), 0, "LCS second empty");
        check(LCS.compute("", "ABC"), 0, "LCS first empty");
        check(LCS.compute("ABCBDAB", "BDCABA"), 4, "LCS common subsequence");
    }

    private static void testEditDistance()
    {
        check(EditDistance.compute("JAVA", "JAVA"), 0, "ED same");
        check(EditDistance.compute("HI", ""), 2, "ED second empty");
        check(EditDistance.compute("CAT", "CUT"), 1, "ED substitution");
        check(EditDistance.compute("CAT", "CART"), 1, "ED insertion");
        check(EditDistance.compute("CART", "CAT"), 1, "ED deletion");
        check(EditDistance.compute("", "HI"), 2, "ED first empty");
        check(EditDistance.compute("kitten", "sitting"), 3, "ED mixed operations");
    }

    private static void testMatching()
    {
        checkMatch("ABABACABABA", "ABA", List.of(0, 2, 6, 8), "multiple occurrences");
        checkMatch("ABCDEFG", "XYZ", List.of(), "absent");
        checkMatch("HI", "HELLO", List.of(), "longer pattern");
        checkMatch("TEXT", "", List.of(), "empty pattern");
        checkMatch("ABCXYZ", "ABC", List.of(0), "beginning");
        checkMatch("XYZABCXYZ", "ABC", List.of(3), "middle");
        checkMatch("XYZABC", "ABC", List.of(3), "end");
        checkMatch("AAAAA", "AAA", List.of(0, 1, 2), "overlapping occurrences");
        checkMatch("", "A", List.of(), "empty text");
        checkMatch("", "", List.of(), "both empty");
        checkMatch("ABC", "ABC", List.of(0), "whole text");
        // Both hashes are 256, but the strings differ. RK must verify characters.
        checkMatch("\u0001\u0000", "\u0000\u0100", List.of(), "hash collision");
    }

    private static void testKasai()
    {
        int[] sa = {5, 3, 1, 0, 4, 2};
        int[] expected = {1, 3, 0, 0, 2, 0};
        checkArray(Kasai.buildLCP("banana", sa), expected, "banana LCP");
        checkArray(Kasai.buildSA("banana"), sa, "banana suffix array");

        Random random = new Random(42);
        for (int trial = 0; trial < 100; trial++) {
            String s = AlgorithmExperiments.gen(trial % 30, random);
            int[] suffixes = Kasai.buildSA(s);
            int[] direct = new int[s.length()];
            for (int i = 0; i + 1 < suffixes.length; i++) {
                int a = suffixes[i], b = suffixes[i + 1], k = 0;
                while (a + k < s.length() && b + k < s.length()
                        && s.charAt(a + k) == s.charAt(b + k)) k++;
                direct[i] = k;
            }
            checkArray(Kasai.buildLCP(s, suffixes), direct, "random LCP " + trial);
        }
    }

    private static void checkMatch(String text, String pattern, List<Integer> expected, String label)
    {
        // All three must equal the same expected list, so they also agree with each other.
        check(NaiveStringMatching.search(text, pattern), expected, "Naive " + label);
        check(KMP.search(text, pattern), expected, "KMP " + label);
        check(RabinKarp.search(text, pattern), expected, "Rabin-Karp " + label);
    }

    private static void check(Object actual, Object expected, String label)
    {
        if (Objects.equals(actual, expected)) {
            passed++;
        } else {
            failed++;
            System.out.println("[FAIL] " + label + ": expected " + expected + ", got " + actual);
        }
    }

    private static void checkArray(int[] actual, int[] expected, String label)
    {
        check(Arrays.toString(actual), Arrays.toString(expected), label);
    }

    private static void runGroup(String name, Runnable tests)
    {
        int previousPassed = passed, previousFailed = failed;
        tests.run();
        summary.append(String.format("%-20s %8d %8d%n", name,
                passed - previousPassed, failed - previousFailed));
    }
}
