package com.hzk.datastructure;

/**
 * 稀疏数组练习
 * 参考：五子棋
 */
public class SparseArrayDemo {
    public static void main(String[] args) {
        // 1、原始二维数组，值1为白子，值2为黑子
        int chessArr1[][] = new int[11][11];
        chessArr1[1][2] = 1;
        chessArr1[2][3] = 2;
        System.out.println("原始二维数组：");
        for (int row[] : chessArr1) {
            for (int value : row) {
                System.out.printf("%d\t", value);
            }
            System.out.println();
        }
        System.out.println("-------------------------------------------------------------------");

        // 2、二维数组转稀疏数组
        // 2.1、获取非0个数
        int sum = 0;// 非0个数
        for (int i = 0; i < chessArr1.length; i++) {
            for (int j = 0; j < chessArr1[0].length; j++) {
                if (chessArr1[i][j] != 0) {
                    sum++;
                }
            }
        }

        // 2.2、创建稀疏数组
        System.out.println("稀疏数组：");
        int sparseArr[][] = new int[sum + 1][3];
        // 设置第一行
        sparseArr[0][0] = chessArr1.length;// 行
        sparseArr[0][1] = chessArr1[0].length;// 列
        sparseArr[0][2] = sum;//值数

        // 2.3、遍历原始数组，获取非0值
        int count = 0;
        for (int i = 0; i < chessArr1.length; i++) {
            for (int j = 0; j < chessArr1[0].length; j++) {
                if (chessArr1[i][j] != 0) {
                    count++;
                    sparseArr[count][0] = i;
                    sparseArr[count][1] = j;
                    sparseArr[count][2] = chessArr1[i][j];
                }
            }
        }

        // 2.4、遍历展示稀疏数组
        for (int row[] : sparseArr) {
            for (int value : row) {
                System.out.printf("%d\t", value);
            }
            System.out.println();
        }
        System.out.println("-------------------------------------------------------------------");

        // 3、稀疏数组转二维数组
        System.out.println("稀疏数组转原始数据：");
        int chessArr2[][] = new int[sparseArr[0][0]][sparseArr[0][1]];
        for (int i = 1; i < sparseArr.length; i++) {
            int row0 = sparseArr[i][0];
            int row1 = sparseArr[i][1];
            int row2 = sparseArr[i][2];
            chessArr2[row0][row1] = row2;
        }
        // 3.1、遍历展示稀疏数组
        for (int row[] : chessArr2) {
            for (int value : row) {
                System.out.printf("%d\t", value);
            }
            System.out.println();
        }


    }
}
