package com.hzk.tool.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 使用poi实现execl导出
 */
public class ExcelExportTest {

    public static void main(String[] args) throws Exception {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建一个工作表
        Sheet sheet = workbook.createSheet("数据表");

        // 标题行，单位为1/256个字符宽度
        int unit = 256;
        Row row = sheet.createRow(0);
        sheet.setColumnWidth(0, unit * 70);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue("key");

        // 增加标注
        CreationHelper createHelper = workbook.getCreationHelper();

        Drawing draw = row.getSheet().createDrawingPatriarch();
        ClientAnchor anchor = createHelper.createClientAnchor();
        anchor.setCol1(cell0.getColumnIndex());
        anchor.setRow1(cell0.getRowIndex());
        anchor.setCol2(anchor.getCol1() + 2);
        anchor.setRow2(anchor.getRow1() + 2);
        Comment comment = draw.createCellComment(anchor);
        comment.setString(createHelper.createRichTextString("一级key名称"));
        cell0.setCellComment(comment);

        Cell cell1 = row.createCell(1);
        sheet.setColumnWidth(1, unit * 10);
        cell1.setCellValue("type");

        Cell cell2 = row.createCell(2);
        sheet.setColumnWidth(2, unit * 10);
        cell2.setCellValue("itemSize");

        Cell cell3 = row.createCell(3);
        sheet.setColumnWidth(3, unit * 10);
        cell3.setCellValue("byteSize");

        Cell cell4 = row.createCell(4);
        sheet.setColumnWidth(4, unit * 10);
        cell4.setCellValue("ttl(s)");

        // 数据行
        for (int i = 1; i < 3; i++) {
            Row tempRow = sheet.createRow(i);
            Cell tempCell0 = tempRow.createCell(0);
            tempCell0.setCellValue("key" + i);
            Cell tempCell1 = tempRow.createCell(1);
            tempCell1.setCellValue("hash");
            Cell tempCell2 = tempRow.createCell(2);
            tempCell2.setCellValue(i);
            Cell tempCell3 = tempRow.createCell(3);
            tempCell3.setCellValue(1000);
            Cell tempCell4 = tempRow.createCell(4);
            tempCell4.setCellValue(200);
        }

        // 写入到文件
        try (FileOutputStream outputStream = new FileOutputStream("example.xlsx")) {
            workbook.write(outputStream); // 将工作簿写入文件
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close(); // 关闭工作簿释放资源
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}

