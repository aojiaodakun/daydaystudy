package com.hzk.tool.watermark.img;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageWatermarkTest {

    public static void main(String[] args) {
        // 输入图片路径
        String inputPath = "D:\\project\\daydaystudy\\basic\\src\\main\\resources\\test.jpg";    // 替换为你的图片路径
        // 输出图片路径
        String outputImagePath = "output_watermarked1.jpg";
        // 水印文字
        String watermarkText = "Confidential";

        try {
            // 读取原图
            BufferedImage originalImage = ImageIO.read(new File(inputPath));

            // 创建画布
            Graphics2D g2d = (Graphics2D) originalImage.getGraphics();

            // 设置抗锯齿（更平滑的文字）
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // 设置字体大小为图片宽度的 1/20，可自动适配不同分辨率
            int fontSize = Math.max(originalImage.getWidth() / 20, 20);
            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));

            // 设置半透明白色
            g2d.setColor(new Color(255, 255, 255, 128));

            // 计算文字位置（右下角）
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int textWidth = fontMetrics.stringWidth(watermarkText);
            int textHeight = fontMetrics.getHeight();

            int x = originalImage.getWidth() - textWidth - 20;
            int y = originalImage.getHeight() - textHeight + fontMetrics.getAscent() - 20;

            // 绘制水印文字
            g2d.drawString(watermarkText, x, y);

            // 释放资源
            g2d.dispose();
            // 输出新图片
            ImageIO.write(originalImage, "jpg", new File(outputImagePath));
            System.out.println("✅ 图片加水印完成：" + outputImagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

