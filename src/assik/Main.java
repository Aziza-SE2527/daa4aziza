package assik;

import java.util.Random;

public class Main
{

    static final long SEED= 42L;
    static final String ALPHA= "ACGT";
    static final int RUNS= 5;

    public static void main(String[] args)
    {
        System.out.println("=== DP Benchmark ===");
        for (int n : new int[]{100, 500, 1000}) {
            Random r=new Random(SEED + n);
            String a=gen(n,r),b = gen(n,r);
            System.out.printf("LCS (%d): %.4f ms%n",n,time(() -> DP.lcs(a, b)));
            System.out.printf("ED  (%d): %.4f ms%n",n,time(() -> DP.editDist(a, b)));
        }

        System.out.println("\n=== String Matching Benchmark ===");
        for (int n : new int[]{10_000, 50_000, 100_000})
        {
            Random r=new Random(SEED + n);
            String t= gen(n, r);
            String pIn= t.substring(n / 2, n / 2 + 20);
            String pOut= pIn.substring(0, 19) + "Z";

            benchMatch("Present",n,t,pIn);
            benchMatch("Absent",n,t,pOut);
        }

        System.out.println("\n=== Kasai Benchmark ===");
        for (int n : new int[]{1000, 5000, 10000})
        {
            Random r=new Random(SEED + n);
            String s=gen(n, r);
            int[] sa=Kasai.buildSA(s); // outside timer
            System.out.printf("Kasai (%d): %.4f ms%n", n, time(() -> Kasai.buildLCP(s, sa)));
        }
    }

    static void benchMatch(String tag,int n,String t,String p)
    {
        double t1= time(() -> StringMatch.naive(t, p));
        long c1= StringMatch.comps;
        double t2= time(() -> StringMatch.kmp(t, p));
        long c2= StringMatch.comps;
        double t3= time(() -> StringMatch.rk(t, p));
        long c3= StringMatch.comps;

        System.out.printf("[%s N=%d] Naive: %.4f ms (%d comps) | KMP: %.4f ms (%d comps) | RK: %.4f ms (%d comps)%n",
                tag,n,t1,c1,t2,c2,t3,c3);
    }

    static double time(Runnable fn)
    {
        fn.run(); // warmup
        long sum= 0;
        for (int i= 0;i <RUNS;i++)
        {
            long s=System.nanoTime();
            fn.run();
            sum+= System.nanoTime() - s;
        }
        return (sum/(double) RUNS) / 1_000_000.0;
    }

    static String gen(int n,Random r)
    {
        StringBuilder sb=new StringBuilder(n);
        for (int i=0;i<n;i++) sb.append(ALPHA.charAt(r.nextInt(ALPHA.length())));
        return sb.toString();
    }
}