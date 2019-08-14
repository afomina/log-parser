package com.ef;

public class test {
    //0  1 2 3 4 5
    //10 9 5 3 2 1
    //

    static int getMaxProfit(Integer[] stockPrices) {
        if (stockPrices.length < 2) {
            throw new IllegalArgumentException("There should be at least 2 stock prices");
        }
        int buyTime = 0; //10
        int sellTime = 1; //9
        for (int i = 1; i < stockPrices.length; i++) {//i=5
            if (stockPrices[i] > stockPrices[sellTime]) {// 2>2?
                sellTime = i;
            } else if (stockPrices[i] < stockPrices[buyTime] && i < stockPrices.length - 1) {//5>5-1
                //2 < 3
                buyTime = i; //4 2
                sellTime = i + 1; //5 1
            }
        }
        return stockPrices[sellTime] - stockPrices[buyTime];//1-2
    }


}
