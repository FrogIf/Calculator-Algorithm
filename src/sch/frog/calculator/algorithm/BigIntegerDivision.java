package sch.frog.calculator.algorithm;

/**
 * 大整数除法
 */
public class BigIntegerDivision {
    
    /**
     * 除法
     * @param dividendStr 被除数
     * @param divisorStr 除数
     * @return 返回数组, 共有两个元素, 第一个元素是商, 第二个元素是余数
     */
    public static String[] division(String dividendStr, String divisorStr){
        if(!isIntegerNumber(dividendStr) || !isIntegerNumber(divisorStr)){
            throw new IllegalArgumentException("is not number, " + dividendStr + "/" + divisorStr);
        }

        int[] dividend = convertToIntArr(dividendStr);
        int[] divisor = convertToIntArr(divisorStr);

        final int m = dividend.length - divisor.length;
        int d = 1;
        // 规范化
        final int n = divisor.length;   // 即使规范化之后, n的值也不会受到影响
        int hd = divisor[divisor.length - 1];   // 除数的最高位
        if(hd < 5){ 
            d = 10 / (hd + 1);
            dividend = simpleMult(dividend, d);
            if(dividend.length < m + n + 1){
                int[] newDividend = new int[m + n + 1];
                for(int i = 0; i < dividend.length; i++){
                    newDividend[i] = dividend[i];
                }
                dividend = newDividend;
            }
            divisor = simpleMult(divisor, d);
        }

        // 初始化
        int[] quotient = new int[m + 1];
        for(int j = m; j >= 0; j--){
            // 计算qhat
            int qhat = (dividend[j + n] * 10 + dividend[j + n - 1]) / divisor[n - 1];    // 估商的结果
            int rhat = (dividend[j + n] * 10 + dividend[j + n - 1]) % divisor[n - 1];    // 余数
            while(qhat == 10 || qhat * divisor[n - 2] > 10 * rhat + dividend[j + n - 2]){
                // 说明估商结果偏大
                qhat = qhat - 1;
                rhat = rhat + divisor[n - 1];
            }

            // 乘和减
            int borrow = 0;
            for(int i = 0; i < n; i++){
                int s = dividend[j + i] - borrow - divisor[i] * qhat;
                borrow = 0;
                while(s < 0){
                    s += 10;
                    borrow++;
                }
                dividend[j + i] = s;
            }
            if(dividend[j + n] > 0 && borrow > 0){
                int s = dividend[j + n] - borrow;
                borrow = 0;
                while(s < 0){
                    s += 10;
                    borrow++;
                }
                dividend[j + n] = s;
            }

            if(borrow > 0){ // 测试余数
                // 往回加
                qhat--;
                int carry = 0;
                for(int i = 0; i < n; i++){
                    int sum = dividend[j + i] + carry + divisor[i];
                    carry = 0;
                    while(sum >= 10){
                        carry++;
                        sum -= 10;
                    }
                    dividend[j + i] = sum;
                }
                assert carry == 0;
            }
            quotient[j] = qhat;
        }

        // 去规格化, 求余数
        int[] r = simpleDivision(dividend, d);

        return new String[]{ numberArrayToString(quotient), numberArrayToString(r)};
    }

    /**
     * 除数只有一位的除法
     */
    private static int[] simpleDivision(int[] dividend, int divisor){
        int i = dividend.length - 1;
        for(; i >= 0; i--){
            if(dividend[i] > 0){
                break;
            }
        }
        if(i <= 0){
            return new int[]{0};
        }
        int[] result = new int[i + 1];
        int rem = 0;
        for(; i >= 0; i--){
            int dd = dividend[i] + rem * 10;
            result[i] = dd / divisor;
            rem = dd % divisor;
        }
        return result;
    }

    /**
     * 一个大数字乘以一个只有一位的数字
     */
    private static int[] simpleMult(int[] number, int n2){
        int carry = 0;  // 进位
        int[] result = new int[number.length];
        for(int i = 0; i < number.length; i++){
            int prod = n2 * number[i] + carry;
            carry = prod / 10;
            result[i] = prod % 10;
        }
        if(carry > 0){
            int bit = carry > 10 ? 2 : 1;
            int[] newResult = new int[result.length + bit];
            int i = 0;
            for(; i < result.length; i++){
                newResult[i] = result[i];
            }
            while(carry > 10){
                newResult[i] = carry / 10;
                carry = carry % 10;
                i++;
            }
            newResult[i] = carry;
            result = newResult;
        }
        return result;
    }

    /**
     * 用字符串代表的数字转为用数组代表的数字
     * 转换后, 数组的高索引处存储数字的高位, 并且每一位仅代表数字中的一位
     */
    private static int[] convertToIntArr(String numberStr){
        char[] charArray = numberStr.toCharArray();
        int[] arr = new int[charArray.length];
        for(int i = 0; i < arr.length; i++){
            arr[arr.length - 1 - i] = charArray[i] - '0';
        }
        return arr;
    }

    /**
     * 判断是否为整数
     */
    private static boolean isIntegerNumber(String numberStr){
        if(numberStr == null){ return false; }
        char[] charArray = numberStr.toCharArray();
        for(int i = 0; i < charArray.length; i++){
            char ch = charArray[i];
            if(ch < '0' || ch > '9'){
                return false;
            }
        }
        return true;
    }

    /**
     * 数字内部表示形式(数组)转字符串
     */
    private static String numberArrayToString(int[] number){
        StringBuilder sb = new StringBuilder();
        for(int i = number.length - 1; i >= 0; i--){
            sb.append(number[i]);
        }
        return sb.toString();
    }

    public static void main(String[] args){
        String[] res = division("92834", "123");
        System.out.println(res[0] + "......" + res[1]);
    }

}
