package com.hzk.tool.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Path;

public class QrCodeTest {

    public QrCodeTest(){

    }
    public QrCodeTest(String name) {

    }

    private QrCodeTest(int i){

    }



    public static void main(String[] args) throws Exception{

        Class<QrCodeTest> clazz = QrCodeTest.class;


        String url = "https://www.baidu.com";
        // 1、生成二维码图片
        //genPng(url);

        // 2、生成二维码数组
        byte[] encodes = encode(url, 20, 20);
        String s = new String(Base64.encodeBase64(encodes), "UTF-8");
        System.out.println(s);


    }

    public static void genPng(String urlString) throws Exception{
        //2.通过zxing生成二维码 (保存到本地图片， 支持以data url 的形式体现)
        //创建QRCodeWriter对象
        QRCodeWriter writer = new QRCodeWriter();
        //生成的图片基本配置
        /*
        参数
        1.二维码信息
        2.图片类型
        3.图片宽度
        4.图片长度
        */
        BitMatrix bt = writer.encode(urlString, BarcodeFormat.QR_CODE, 200, 200);
        //保存到本地
        Path path = new File("D:\\工作\\HR中台\\需求\\外部用户登录苍穹\\qrcode\\test.png").toPath();
        MatrixToImageWriter.writeToPath(bt,"png",path);
    }


    public static byte[] encode(String content, int width, int height) {
        if (content != null && !"".equals(content)) {
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                Throwable var4 = null;

                byte[] var7;
                try {
                    content = URLDecoder.decode(content, "UTF-8");
                    QRCodeWriter writer = new QRCodeWriter();
                    BitMatrix m = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
                    MatrixToImageWriter.writeToStream(m, "png", os);
                    var7 = os.toByteArray();
                } catch (Throwable var17) {
                    var4 = var17;
                    throw var17;
                } finally {
                    if (os != null) {
                        if (var4 != null) {
                            try {
                                os.close();
                            } catch (Throwable var16) {
                                var4.addSuppressed(var16);
                            }
                        } else {
                            os.close();
                        }
                    }

                }

                return var7;
            } catch (Exception var19) {
                var19.printStackTrace();
            }
        }
        return null;
    }

}
