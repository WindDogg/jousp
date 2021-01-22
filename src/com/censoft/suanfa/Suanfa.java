package com.censoft.suanfa;

public class Suanfa {
	static int[] coins = { 1, 3, 5 };// 硬币的面值
	static int amount = 11;// 总金额

	int dp(int n) {
		if (n == 0)
			return 0;
		if (n < 0)
			return -1;
		double res = Double.POSITIVE_INFINITY;
		for (int coin : coins) {
			int subproblem = dp(n - coin);
			if (subproblem == -1)
				continue;
			res = Math.min(res, 1 + subproblem);
		}
		if (res != Double.POSITIVE_INFINITY) {
			return (int) res;
		} else {
			return -1;
		}

	}

}
