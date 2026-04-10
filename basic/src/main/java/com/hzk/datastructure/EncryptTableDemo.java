package com.hzk.datastructure;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.UnsupportedEncodingException;

/**
 * 密码表功能
 * 1、生成16位或32位的密钥
 * 2、将密钥的字符随机存储
 */
public class EncryptTableDemo {

    // 大表的行数
    public static int bigRow = 50;
    // 大表的列数
    public static int bigColumn = 200;
    // 编码
    public final static String UTF_8 = "utf-8";
    public final static int LEN_16 = 16;
    public final static int LEN_32 = 32;
    // 密钥长度(国际算法16，国内算法32)
    public final static int SECRET_LEN = LEN_32;

    public static void main(String[] args) throws Exception{
        // 获取长度16的随机密钥字符串（0-9，a-z,A-Z）
        String secretKey = RandomStringUtils.randomAlphanumeric(SECRET_LEN);
        System.out.println("密钥：" + secretKey);

        EncryptTableDemo demo = new EncryptTableDemo();
        String bigArrayString = demo.getEncryptSparseArray(secretKey);

        // 解析
        String resolveString = demo.getEncryptStringBySpareArray(bigArrayString);
        System.out.println("-------------------------------------------");
        System.out.println("还原后的字符串：" + resolveString + "，匹配布尔值：" + secretKey.equals(resolveString));
    }


    /**
     * 获取密码表
     * @param secretKey 密钥字符串
     */
    private String getEncryptSparseArray(String secretKey) throws Exception{
        /*
         1、生成二维数据大表（空表）
         */
        int[][] bigIntArray = new int[bigRow][bigColumn];
        /*
         2、将密钥转换存入大表，返回索引数组
         */
        int[][] indexArray = storeSecretToBigArray(bigIntArray,secretKey);
        System.out.println("---------------------------索引数组----------------------------------------");
        printIntArray(indexArray);
        System.out.println("---------------------------真实数据（无干扰）----------------------------------------");
        printIntArray(bigIntArray);
        // 3、往大表加入干扰字节
        interfereBigArray(bigIntArray);
        System.out.println("---------------------------真实数据（含干扰）----------------------------------------");
        printIntArray(bigIntArray);
        // 4、将索引数组存入大表的第一行
        storeIndexToBigArray(bigIntArray,indexArray);
        System.out.println("---------------------------全部数据（含干扰，含索引）----------------------------------------");
        printIntArray(bigIntArray);
        // 5、将大表转换成长字符串
        String bigArrayString = getBigIndexArrayString(bigIntArray);
        System.out.println("大表长字符串：" + bigArrayString);
        return bigArrayString;
    }

    /**
     * 将大表字符串转换成字节数组大表
     * @param bigString
     * @return
     */
    private int[][] bigIntArrayByBigString(String bigString){
        int[][] bigArray = new int[bigRow][bigColumn];
        int size = 0;
        for (int i = 0; i < bigArray.length; i++) {
            for (int j = 0; j < bigArray[0].length; j++) {
                char c = bigString.charAt(size);
                bigArray[i][j] = c;
                size++;
            }
        }
        return bigArray;
    }

    /**
     * 从大表中获取密钥字符串
     * @return
     */
    private String getEncryptStringBySpareArray(String bigArrayString){
        int[][] bigArray = bigIntArrayByBigString(bigArrayString);
        String result = "";
        // 1、获取大表第一行的索引数组
        int indexLen = SECRET_LEN * 2;
        int[] bytes = new int[indexLen];
        for (int i = 0; i < indexLen; i++) {
            bytes[i] = bigArray[0][i];
        }
        StringBuilder sb = new StringBuilder();
        // 2、根据索引数组，从大表中获取字符串
        for (int i = 0; i < indexLen; i=i+2) {
            int row = bytes[i];
            int column = bytes[i+1];
            char c = (char)bigArray[row][column];
            sb.append(c);
        }
        result = sb.toString();
        return result;
    }

    /**
     * 将大表转换成长字符串
     * @param bigIndexArray 大表数组
     * @return
     */
    private String getBigIndexArrayString(int[][] bigIndexArray){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bigIndexArray.length; i++) {
            for (int j = 0; j < bigIndexArray[0].length; j++) {
                char c = (char)bigIndexArray[i][j];
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将密钥转换存入大表，返回稀疏数组
     * @param bigIntArray 大表
     * @param secret 密钥
     * @return 索引数据
     */
    private int[][] storeSecretToBigArray(int[][] bigIntArray,String secret) throws UnsupportedEncodingException {
        byte[] secretBytes = secret.getBytes(UTF_8);
        // 大数组行，列
        int bigRow = bigIntArray.length;
        int bigColumn = bigIntArray[0].length;
        int secretSize = secretBytes.length;
        // 索引数组
        int[][] indexArray = new int[secretSize][2];
        for (int i = 0; i < secretSize; i++) {
            byte b = secretBytes[i];
            int row;
            int column;
            while(true){
                // 第一行行放置索引数据
                row = (int)(Math.random()*(bigRow-2)) + 1;
                column = (int)(Math.random()*bigColumn);
                if(bigIntArray[row][column] == 0){
                    break;
                }
            }
            bigIntArray[row][column] = b;
            indexArray[i][0] = row;
            indexArray[i][1] = column;
        }
        return indexArray;
    }


    /**
     * 将稀疏数组存入大表的第一行
     * @param bigIntArray
     * @param indexArray
     */
    private void storeIndexToBigArray(int[][] bigIntArray,int[][] indexArray){
        int row0size = 0;
        for (int i = 0; i < indexArray.length; i++) {
            for (int j = 0; j < indexArray[0].length; j++) {
                bigIntArray[0][row0size] = indexArray[i][j];
                row0size++;
            }
        }
    }

    /**
     * 打印数组
     * @param array
     */
    private void printIntArray(int[][] array){
        for (int row[] : array) {
            for (int value : row) {
                System.out.printf("%d\t", value);
            }
            System.out.println();
        }
    }

    /**
     * 对大数据进行数据干扰
     * @param bigArray
     */
    private void interfereBigArray(int[][] bigArray){
        for (int i = 0; i < bigArray.length; i++) {
            for (int j = 0; j < bigArray[0].length; j++) {
                if(bigArray[i][j] == 0){
                    bigArray[i][j] = getInterfere();
                }
            }
        }
    }

    /**
     * 获取干扰字节
     * 0-9  48-57
     * A-Z  65-90
     * a-z  97-122
     * @return
     */
    private int getInterfere(){
        int i = (int)(Math.random() * 3);
        switch (i){
            case 0:
                return getNumberInterfere();
            case 1:
                return getUpperInterfere();
            default:
                return getLowwerInterfere();
        }
    }

    /**
     * 获取数字干扰
     * 0-9  48-57
     * @return
     */
    private int getNumberInterfere(){
        return (int)(Math.random() * 10) + 48;
    }

    /**
     * 获取大写干扰
     * A-Z  65-90
     * @return
     */
    private int getUpperInterfere(){
        return (int)(Math.random() * 26) + 65;
    }
    /**
     * 获取小写干扰
     * a-z  97-122
     * @return
     */
    private int getLowwerInterfere(){
        return (int)(Math.random() * 26) + 97;
    }

}
