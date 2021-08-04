package sch.frog.calculator.algorithm;

public class DecimalToFraction {

    /**
     * 小数转分数
     * @param decimal 小数, 形如: xxx.xxx_xx  (其中_后面为循环部分)
     * @return 分数, 形如: xxx/xx
     */
    public static String toFraction(String decimal){
        int dotPos = decimal.indexOf(".");
        int cirPos = decimal.indexOf("_");
        if(cirPos >= 0 && (cirPos < dotPos || dotPos < 0)){
            throw new IllegalArgumentException("can't parse decimal : " + decimal);
        }
        if(dotPos < 0){ // 是整数
            return decimal;
        }

        if(cirPos <= 0){
            cirPos = decimal.length();
        }

        String aStr = decimal.substring(0, dotPos);
        String bStr = decimal.substring(dotPos + 1, cirPos);
        long a = Long.parseLong(aStr);
        long b = (dotPos + 1 == cirPos) ? 0 : Long.parseLong(bStr); // 可能是纯循环小数
        long c = 0;
        if(cirPos < decimal.length()){
            String cStr = decimal.substring(cirPos + 1);
            c = Long.parseLong(cStr);
        }

        return convertCirculating(a, b, c);
    }

    /**
     * 
     * @param a 整数部分
     * @param b 小数不循环部分
     * @param c 小数循环部分
     * @return
     */
    private static String convertCirculating(long a, long b, long c){
        long tb = b;
        long tc = c;

        long ten1 = 1;
        while(tb > 0){
            tb /= 10;
            ten1 *= 10;
        }

        long ten2 = tc == 0 ? 10 : 1;   // 这个三元运算用来兼容普通的非循环小数
        while(tc > 0){
            tc /= 10;
            ten2 *= 10;
        }

        long numerator = a * ten1 * ten2 + b * ten2 + c - (a * ten1 + b);
        long denominator = (ten2 - 1) * ten1;
        return numerator + "/" + denominator;
    }
    
}
