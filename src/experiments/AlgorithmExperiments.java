package experiments;

import algorithms.dp.LCS;
import algorithms.dp.EditDistance;
import algorithms.sm.NaiveStringMatching;
import algorithms.sm.KMP;
import algorithms.sm.RabinKarp;
import algorithms.aca.Kasai;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

public class AlgorithmExperiments
{
    static final long SEED = 42L;
    static final String ALPHA = "ACGT";
    static final int RUNS = 5;
    static final int WARMUPS = 20;
    // Keep the returned value observable, including for DP and LCP calls.
    static volatile Object result;
    static final List<String> rows = new ArrayList<>();

    public static void main(String[] args) throws IOException
    {
        rows.clear();
        rows.add("algorithm,input_size,case,average_ms,preprocessing_comparisons,hash_comparisons,character_comparisons");
        for (int n : new int[]{100, 500, 1000}) {
            Random r = new Random(SEED + n);
            String a = gen(n, r), b = gen(n, r);
            measure("LCS", n, "Random", () -> LCS.compute(a, b), false);
            measure("Edit Distance", n, "Random", () -> EditDistance.compute(a, b), false);
        }

        for (int n : new int[]{10_000, 50_000, 100_000}) {
            Random r = new Random(SEED + n);
            String t = gen(n, r);
            String pIn = t.substring(n / 2, n / 2 + 20);
            // Z is outside the alphabet, so this pattern cannot occur.
            String pOut = pIn.substring(0, 19) + "Z";
            benchMatch("Present", n, t, pIn);
            benchMatch("Absent", n, t, pOut);
        }

        for (int n : new int[]{1000, 5000, 10000}) {
            String s = gen(n, new Random(SEED + n));
            int[] sa = Kasai.buildSA(s); // outside timer
            measure("Kasai", n, "Random", () -> Kasai.buildLCP(s, sa), false);
        }
        Path dir = Path.of("results");
        Files.createDirectories(dir);
        Files.write(dir.resolve("benchmark_results.csv"), rows);
        System.out.println("Saved results/benchmark_results.csv");
    }

    static void benchMatch(String tag, int n, String t, String p)
    {
        List<Integer> expected = NaiveStringMatching.search(t, p);
        if (!expected.equals(KMP.search(t, p)) || !expected.equals(RabinKarp.search(t, p))) {
            throw new AssertionError("Matchers disagree on benchmark input");
        }
        measure("Naive", n, tag, () -> NaiveStringMatching.search(t, p), true);
        measure("KMP", n, tag, () -> KMP.search(t, p), true);
        measure("Rabin-Karp", n, tag, () -> RabinKarp.search(t, p), true);
    }

    static void measure(String name, int n, String tag, Supplier<?> fn, boolean count)
    {
        for (int i = 0; i < WARMUPS; i++) result = fn.get();
        long[] times = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            Object value = fn.get();
            times[i] = System.nanoTime() - start;
            result = value;
        }
        long comparisons = 0, preprocessing = 0, hashes = 0;
        if (name.equals("Naive")) comparisons = NaiveStringMatching.characterComparisons;
        if (name.equals("KMP")) {
            comparisons = KMP.characterComparisons;
            preprocessing = KMP.preprocessingComparisons;
        }
        if (name.equals("Rabin-Karp")) {
            comparisons = RabinKarp.characterComparisons;
            hashes = RabinKarp.hashComparisons;
        }
        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            total += times[i];
        }
        double average = total / (double) RUNS / 1_000_000.0;
        rows.add(String.format(Locale.ROOT, "%s,%d,%s,%.6f,%s,%s,%s", name, n, tag,
                average, count ? Long.toString(preprocessing) : "",
                count ? Long.toString(hashes) : "", count ? Long.toString(comparisons) : ""));
        System.out.printf(Locale.ROOT, "%s N=%d %s: %.6f ms%s%n", name, n, tag,
                average, count ? " (" + comparisons + " character comparisons)" : "");
    }

    public static String gen(int n, Random r)
    {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(ALPHA.charAt(r.nextInt(ALPHA.length())));
        return sb.toString();
    }
}
