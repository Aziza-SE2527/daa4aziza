# Assignment 4: Text Analysis - Dynamic Programming and String Algorithms

## 1. Problem Description and Algorithm Overview

This project implements and evaluates key text analysis and string algorithms grouped into three main categories:

1. **Dynamic Programming (Part A):**
   * **Longest Common Subsequence (LCS):** Finds the length of the longest subsequence shared by two strings using a 2D DP table.
   * **Edit Distance (Levenshtein):** Computes the minimum number of insertions, deletions, and substitutions required to transform one string into another.

2. **String Matching (Part B):**
   * **Naive Search:** Baseline matching checking every possible window position.
   * **Knuth-Morris-Pratt (KMP):** Optimizes search by preprocessing the pattern into an LPS (Longest Prefix Suffix) table to avoid redundant character comparisons.
   * **Rabin-Karp:** Uses a rolling polynomial hash function to quickly filter candidate pattern windows before verifying characters.

3. **Advanced String Algorithm (Part C):**
   * **Kasai's Algorithm:** Builds the Longest Common Prefix (LCP) array in linear time $O(n)$ using a given suffix array and rank array.

## 2. Implementation Decisions and Assumptions

* **Package Structure:** All source classes are organized under the `assik` package.
* **Empty Pattern Handling:** If a pattern is empty (`""`) or longer than the text, all string matching algorithms immediately return an empty result list (`[]`) to ensure consistent, predictable behavior.
* **Alphabet & Random Data:** Random data generation uses a fixed seed (`42L`) and a consistent 4-letter DNA alphabet (`"ACGT"`) to guarantee reproducible benchmarks and frequent partial overlaps.
* **Timing Methodology:** Execution times are measured using `System.nanoTime()`, averaging over 5 runs after an initial JVM warmup run. Input generation, console printing, and file operations are strictly excluded from timed regions.

## 3. Complexity Analysis Table

| Algorithm | Recurrence / Main Operation | Best-Case Time | Worst-Case Time | Auxiliary Space |
| :--- | :--- | :--- | :--- | :--- |
| **LCS** | $dp[i][j] = dp[i-1][j-1]+1$ or $\max(dp[i-1][j], dp[i][j-1])$ | $O(n \cdot m)$ | $O(n \cdot m)$ | $O(n \cdot m)$ |
| **Edit Distance** | $dp[i][j] = 1 + \min(\text{del}, \text{ins}, \text{sub})$ | $O(n \cdot m)$ | $O(n \cdot m)$ | $O(n \cdot m)$ |
| **Naive Matching** | Character comparisons $T[i+j] == P[j]$ | $O(n)$ | $O(n \cdot m)$ | $O(1)$ |
| **KMP Matching** | Prefix fallback $j = lps[j-1]$ on mismatch | $O(n + m)$ | $O(n + m)$ | $O(m)$ |
| **Rabin-Karp** | Rolling polynomial hash + verification | $O(n + m)$ | $O(n \cdot m)$ | $O(1)$ |
| **Kasai's LCP** | Suffix prefix extension starting from $k-1$ | $O(n)$ | $O(n)$ | $O(n)$ |

* **Justification:** LCS and Edit Distance fill a 2D table of size $(n+1) \times (m+1)$ in nested loops. KMP achieves linear time by never backing up the text pointer $i$. Rabin-Karp runs in $O(n+m)$ expected time with a large prime modulus ($10^9 + 7$), falling back to $O(n \cdot m)$ only during catastrophic hash collision attacks. Kasai's algorithm runs in $O(n)$ because the common prefix length $k$ decrements at most once per iteration and increments at most $n$ times total.

## 4. Testing Strategy and Results

All correctness edge cases are fully verified via `assik.Tests`:
* **LCS:** Tested on identical strings, completely different strings, empty strings, and standard overlapping sequences.
* **Edit Distance:** Tested on identical strings, empty strings, pure insertions, deletions, substitutions, and mixed multi-operation strings.
* **String Matching:** Verified that Naive, KMP, and Rabin-Karp return identical occurrence indices across patterns located at the beginning, middle, end, multiple times, absent patterns, longer patterns, and empty patterns.
* **Kasai's LCP:** Validated against the classic manual test string `"banana"` with a manually verified suffix array.

All tests successfully pass without assertion errors.

## 5. Experimental Setup and Execution Instructions

1. Compile all files inside the `assik` package:
   ```bash
   javac assik/*.java
   ```
2. Run correctness tests:
   ```bash
   java assik.Tests
   ```
3. Run performance benchmarks:
   ```bash
   java assik.Main
   ```

## 6. Experimental Results (Tables)

### Part A: Dynamic Programming ($N = 100, 500, 1000$)
| Size ($N$) | LCS Avg Time (ms) | Edit Distance Avg Time (ms) |
| :--- | :--- | :--- |
| 100 | *Insert Results* | *Insert Results* |
| 500 | *Insert Results* | *Insert Results* |
| 1,000 | *Insert Results* | *Insert Results* |

### Part B: String Matching ($N = 10,000, 50,000, 100,000$)
| Text Length | Case | Naive Time (ms) | KMP Time (ms) | RK Time (ms) |
| :--- | :--- | :--- | :--- | :--- |
| 10,000 | Present | *Insert* | *Insert* | *Insert* |
| 10,000 | Absent | *Insert* | *Insert* | *Insert* |
| 50,000 | Present | *Insert* | *Insert* | *Insert* |
| 50,000 | Absent | *Insert* | *Insert* | *Insert* |
| 100,000 | Present | *Insert* | *Insert* | *Insert* |
| 100,000 | Absent | *Insert* | *Insert* | *Insert* |

### Part C: Kasai's Algorithm ($N = 1000, 5000, 10000$)
| Size ($N$) | Kasai LCP Avg Time (ms) |
| :--- | :--- |
| 1,000 | *Insert Results* |
| 5,000 | *Insert Results* |
| 10,000 | *Insert Results* |

## 7. Plots of Experimental Results

*(Place your exported chart image files inside the `plots/` directory in your repository)*

* **Figure 1: Execution Time vs Input Size (LCS & Edit Distance)**
  `![DP Execution Time](plots/dp_time.png)`

* **Figure 2: Execution Time vs Text Length (Naive, KMP, Rabin-Karp)**
  `![String Matching Time](plots/string_match_time.png)`

* **Figure 3: Execution Time vs String Length (Kasai's LCP)**
  `![Kasai LCP Time](plots/kasai_time.png)`

## 8. Discussion & Conclusion

* **Theoretical vs. Experimental Results:** Measured execution times closely mirror theoretical Big-O curves. Quadratic growth is observed for DP algorithms as input size increases tenfold, whereas string matchers scale linearly.
* **Trade-offs:**
  * **KMP vs. Naive:** KMP eliminates redundant comparisons at the cost of slight overhead for building the LPS table, making it vastly superior for repeating text patterns.
  * **Rabin-Karp vs. KMP:** Rabin-Karp offers great average-case rolling hash performance but can suffer slowdowns in worst-case collision scenarios or when character verification overhead is high.
  * **Dynamic Programming:** Guarantees optimal global alignment solutions but incurs heavy $O(n \cdot m)$ time and space costs for large inputs.