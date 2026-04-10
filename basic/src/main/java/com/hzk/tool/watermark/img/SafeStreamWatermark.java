package com.hzk.tool.watermark.img;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.util.Iterator;

public class SafeStreamWatermark {

    public static void main(String[] args) throws Exception {
        String inputPath = "D:\\project\\daydaystudy\\basic\\src\\main\\resources\\test.jpg";    // 替换为你的图片路径
        String outputPath = "output_watermarked.png"; // 输出文件建议为 PNG 无损格式

        File inputFile = new File(inputPath);
        try (ImageInputStream iis = ImageIO.createImageInputStream(inputFile)) {

            // 选择合适的 ImageReader（JPEG、PNG 都行）
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext()) {
                throw new RuntimeException("未找到可用的 ImageReader");
            }
            ImageReader reader = readers.next();
            reader.setInput(iis, true, true);

            int width = reader.getWidth(0);
            int height = reader.getHeight(0);
            System.out.printf("原图尺寸: %dx%d%n", width, height);

            // 创建输出画布
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outputImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            // 分块读取（防止OOM）
            int blockSize = 1024;
            for (int y = 0; y < height; y += blockSize) {
                for (int x = 0; x < width; x += blockSize) {

                    int w = Math.min(blockSize, width - x);
                    int h = Math.min(blockSize, height - y);

                    ImageReadParam param = reader.getDefaultReadParam();
                    param.setSourceRegion(new Rectangle(x, y, w, h));

                    // 读取子区域像素
                    Raster raster = reader.readRaster(0, param);

                    // 某些JPEG reader会返回比请求小的区域（对齐到8像素）
                    int actualW = raster.getWidth();
                    int actualH = raster.getHeight();

                    BufferedImage part = new BufferedImage(actualW, actualH, BufferedImage.TYPE_INT_RGB);
                    part.setData(raster);

                    // 绘制到整图上
                    g2d.drawImage(part, x, y, null);
                    System.out.printf("读取并绘制分块: [%d,%d] -> %dx%d%n", x, y, actualW, actualH);
                }
            }

            // ==========================
            // 居中绘制高可见度水印
            // ==========================
            String watermark = "CONFIDENTIAL";
            int fontSize = Math.max(width / 15, 80);
            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
            FontMetrics fm = g2d.getFontMetrics();

            int textWidth = fm.stringWidth(watermark);
            int textHeight = fm.getHeight();
            int tx = (width - textWidth) / 2;
            int ty = (height + textHeight) / 2;

            // 阴影
            g2d.setColor(new Color(0, 0, 0, 160));
            g2d.drawString(watermark, tx + 4, ty + 4);
            // 白色主体
            g2d.setColor(new Color(255, 255, 255, 140));
            g2d.drawString(watermark, tx, ty);

            g2d.dispose();

            // ==========================
            // 输出到文件（无损）
            // ==========================
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
            if (!writers.hasNext()) {
                throw new RuntimeException("未找到 PNG writer");
            }
            ImageWriter writer = writers.next();

            try (ImageOutputStream ios = ImageIO.createImageOutputStream(new File(outputPath))) {
                writer.setOutput(ios);
                writer.write(outputImage);
                writer.dispose();
            }

            reader.dispose();
            System.out.println("✅ 无缩放、无截断水印生成完成：" + outputPath);
        }
    }
}
