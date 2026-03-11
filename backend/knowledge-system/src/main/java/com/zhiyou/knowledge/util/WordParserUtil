package com.zhiyou.knowledge.util;

import org.apache.poi.xwpf.usermodel.*;
import java.io.InputStream;
import java.util.List;

public class WordParserUtil {

    /**
     * 解析 .docx 文档，保留表格结构（转成 Markdown 格式）
     * @param inputStream 文件输入流
     * @return 带 Markdown 表格的完整文本
     * @throws Exception 解析异常
     */
    public static String parseDocxWithTables(InputStream inputStream) throws Exception {
        XWPFDocument doc = new XWPFDocument(inputStream);
        StringBuilder sb = new StringBuilder();

        for (IBodyElement element : doc.getBodyElements()) {
            if (element instanceof XWPFParagraph) {
                // 处理普通段落
                XWPFParagraph para = (XWPFParagraph) element;
                String text = para.getText();
                if (text != null && !text.trim().isEmpty()) {
                    sb.append(text).append("\n\n");
                }
            } else if (element instanceof XWPFTable) {
                // 处理表格 → 转 Markdown 表格
                XWPFTable table = (XWPFTable) element;
                sb.append("\n");
                List<XWPFTableRow> rows = table.getRows();
                if (rows.isEmpty()) continue;

                // 1. 表头行
                XWPFTableRow headerRow = rows.get(0);
                sb.append("|");
                for (XWPFTableCell cell : headerRow.getTableCells()) {
                    sb.append(cell.getText().trim()).append("|");
                }
                sb.append("\n");

                // 2. 分隔线行
                sb.append("|");
                for (int i = 0; i < headerRow.getTableCells().size(); i++) {
                    sb.append("---|");
                }
                sb.append("\n");

                // 3. 数据行
                for (int i = 1; i < rows.size(); i++) {
                    XWPFTableRow dataRow = rows.get(i);
                    sb.append("|");
                    for (XWPFTableCell cell : dataRow.getTableCells()) {
                        sb.append(cell.getText().trim()).append("|");
                    }
                    sb.append("\n");
                }
                sb.append("\n");
            }
        }
        doc.close();
        return sb.toString();
    }
}
