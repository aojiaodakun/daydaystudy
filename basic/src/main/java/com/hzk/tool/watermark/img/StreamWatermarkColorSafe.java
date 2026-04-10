package com.hzk.tool.watermark.img;
import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import ar.com.hjg.pngj.*;

public class StreamWatermarkColorSafe {
    public static void main(String[] args) throws Exception {
        String inputPath = "D:\\project\\daydaystudy\\basic\\src\\main\\resources\\test.jpg";    // 替换为你的图片路径
        String outputPath = "output_watermarked.png"; // 输出图片路径
        String watermarkText = "CONFIDENTIAL";

        int blockHeight = 1024; // 每次处理多少行
        int margin = 80;       // 距离边缘
        int fontSize = 120;    // 字体大小

        File inFile = new File(inputPath);
        if (!inFile.exists()) throw new FileNotFoundException(inFile.getAbsolutePath());

        try (ImageInputStream iis = ImageIO.createImageInputStream(inFile)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext()) throw new RuntimeException("没有找到合适的ImageReader");
            ImageReader reader = readers.next();
            reader.setInput(iis, true, true);

            int width = reader.getWidth(0);
            int height = reader.getHeight(0);
            System.out.printf("原图尺寸: %dx%d%n", width, height);

            fontSize = Math.max(width / 50, fontSize);

            // 测量水印文字尺寸
            BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gTmp = tmp.createGraphics();
            gTmp.setFont(new Font("Arial", Font.BOLD, fontSize));
            FontMetrics fm = gTmp.getFontMetrics();
            int textW = fm.stringWidth(watermarkText);
            int textH = fm.getHeight();
            gTmp.dispose();

            int wmX = width - textW - margin;
            int wmY = height - margin;

            // 创建 PNG 写出对象（RGB）
            ImageInfo imi = new ImageInfo(width, height, 8, false);
            PngWriter pngw = new PngWriter(new File(outputPath), imi);

            // 按块读取
            for (int y = 0; y < height; y += blockHeight) {
                int h = Math.min(blockHeight, height - y);
                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceRegion(new Rectangle(0, y, width, h));
                BufferedImage tile = reader.read(0, param);

                // 如果该块在水印区域，直接画在 tile 上
                if (y + h >= wmY - textH && y <= wmY) {
                    Graphics2D gTile = tile.createGraphics();
                    gTile.setFont(new Font("Arial", Font.BOLD, fontSize));
                    gTile.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                    int localY = wmY - y;
                    gTile.setColor(new Color(0, 0, 0, 160));
                    gTile.drawString(watermarkText, wmX + 3, localY + 3);
                    gTile.setColor(new Color(255, 255, 255, 180));
                    gTile.drawString(watermarkText, wmX, localY);
                    gTile.dispose();
                }

                // 写每行
                for (int row = 0; row < h; row++) {
                    int globalY = y + row;
                    int[] rgb = new int[width];
                    tile.getRGB(0, row, width, 1, rgb, 0, width);

                    ImageLineInt line = new ImageLineInt(imi);
                    int[] scan = line.getScanline();

                    for (int x = 0; x < width; x++) {
                        int argb = rgb[x];
                        int r = (argb >> 16) & 0xFF;
                        int g = (argb >> 8) & 0xFF;
                        int b = (argb) & 0xFF;
                        scan[x * 3] = r;
                        scan[x * 3 + 1] = g;
                        scan[x * 3 + 2] = b;
                    }

                    pngw.writeRow(line, globalY);
                }

                System.out.printf("已处理行: %d-%d%n", y, y + h);
            }

            pngw.end();
            reader.dispose();
            System.out.println("✅ 输出完成: " + outputPath);
        }
    }
}
