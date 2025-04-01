package com.example.ylstest01;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StaticSpad {
    public  String getAllparmSting(double[] spadArry){
        DecimalFormat format=new DecimalFormat("#.0");
        String meanSting ="Mean："+ format.format(mean(spadArry));
        String medianSting ="Median："+ format.format(median(spadArry));
        String modeString="Mode:"+format.format(mode(spadArry));
        String varianceSting="Variance:"+format.format(variance(spadArry,mean(spadArry)));
        String standardDeviationString="Standard deviation:"+format.format(standardDeviation(variance(spadArry,mean(spadArry))));
        String maxString="Maximum value："+format.format(max(spadArry));
        String minString="Minimum value："+format.format(min(spadArry));
        String quantilesString="First quartile："+format.format(quantiles(spadArry)[0])+"\n"+"Third quartile："+format.format(quantiles(spadArry)[1]);
        return ""+meanSting+"\n"+medianSting+"\n"+modeString+"\n"+varianceSting+"\n"+standardDeviationString+"\n"+maxString+"\n"+minString+"\n"+quantilesString;
    }
    // 计算均值
    public static double mean(double[] data) {
        return data.length > 0 ? Arrays.stream(data).sum() / data.length : 0;
    }

    // 计算中位数
    public static double median(double[] data) {
        Arrays.sort(data);
        int middle = data.length / 2;
        if (data.length % 2 == 0) {
            return (data[middle - 1] + data[middle]) / 2.0;
        } else {
            return data[middle];
        }
    }

    // 计算众数
    public static double mode(double[] data) {
        Map<Double, Integer> frequencyMap = new HashMap<>();
        for (double num : data) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        double mode = data[0];
        int maxFrequency = 0;

        for (Map.Entry<Double, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                mode = entry.getKey();
                maxFrequency = entry.getValue();
            }
        }
        return mode;
    }

    // 计算方差
    public static double variance(double[] data, double mean) {
        double variance = 0.0;
        for (double num : data) {
            variance += Math.pow(num - mean, 2);
        }
        return variance / data.length;
    }

    // 计算标准差
    public static double standardDeviation(double variance) {
        return Math.sqrt(variance);
    }

    // 寻找最小值
    public static double min(double[] data) {
        return Arrays.stream(data).min().orElse(Double.NaN);
    }

    // 寻找最大值
    public static double max(double[] data) {
        return Arrays.stream(data).max().orElse(Double.NaN);
    }

    // 计算四分位数
    public static double[] quantiles(double[] data) {
        Arrays.sort(data);

        double q1 = data.length > 1 ? data[data.length / 4] : data[0];
        double q3 = data.length > 2 ? data[3 * data.length / 4] : data[1];

        return new double[] {q1, q3};
    }
}
