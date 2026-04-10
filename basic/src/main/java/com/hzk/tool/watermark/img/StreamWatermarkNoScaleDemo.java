package com.hzk.tool.watermark.img;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

public class StreamWatermarkNoScaleDemo {

    public static void main(String[] args) {
        String inputPath = "D:\\project\\daydaystudy\\basic\\src\\main\\resources\\test.jpg";    // 替换为你的图片路径
        String outputPath = "output_watermarked_full.jpg";

        try (ImageInputStream iis = ImageIO.createImageInputStream(new File(inputPath))) {

            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext()) {
                throw new RuntimeException("❌ 未找到可用的 ImageReader");
            }

            ImageReader reader = readers.next();
            reader.setInput(iis, true, true);

            int width = reader.getWidth(0);
            int height = reader.getHeight(0);
            System.out.printf("原图尺寸: %dx%d%n", width, height);

            int blockSize = 2000;
            BufferedImage fullImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D gFull = fullImage.createGraphics();

            for (int y = 0; y < height; y += blockSize) {
                for (int x = 0; x < width; x += blockSize) {

                    int blockW = Math.min(blockSize, width - x);
                    int blockH = Math.min(blockSize, height - y);
                    Rectangle region = new Rectangle(x, y, blockW, blockH);

                    ImageReadParam param = reader.getDefaultReadParam();
                    param.setSourceRegion(region);
                    BufferedImage part = reader.read(0, param);

                    gFull.drawImage(part, x, y, null);
                    System.out.printf("读取并绘制区域: [%d,%d] %dx%d%n", x, y, blockW, blockH);
                }
            }

            // ==============================
            //        绘制可见水印
            // ==============================
            Graphics2D g2d = fullImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            String watermark = "CONFIDENTIAL";
            int fontSize = Math.max(width / 15, 80);
            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
            FontMetrics fm = g2d.getFontMetrics();

            int textWidth = fm.stringWidth(watermark);
            int textHeight = fm.getHeight();

            // 居中绘制（可改为右下角）
            int x = (width - textWidth) / 2;
            int y = (height + textHeight) / 2;

            // 先画阴影（黑色半透明）
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.drawString(watermark, x + 4, y + 4);

            // 再画白色主体（半透明）
            g2d.setColor(new Color(255, 255, 255, 150));
            g2d.drawString(watermark, x, y);

            g2d.dispose();
            gFull.dispose();
            reader.dispose();

            ImageIO.write(fullImage, "jpg", new File(outputPath));
            System.out.println("✅ 分块流式读取 + 明显可见水印完成: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
