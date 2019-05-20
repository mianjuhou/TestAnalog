package com.potevio.analog.util;

import org.springframework.boot.system.ApplicationHome;

import java.io.*;

/**
 * 创建，获取和清空存放Excel配置信心的文件
 */
public class ExcelFileUtil {
    private static String fileName = "/ExcelConfig.xlsx";
    private static String filePath;
    public static File excelFile;

    private ExcelFileUtil() {
        filePath = new ApplicationHome(this.getClass()).getSource().getParentFile().getPath() + fileName;
        excelFile = new File(filePath);
    }

    public static ExcelFileUtil INSTANCE = new ExcelFileUtil();


    public File getExcelFile() {
        if (!excelFile.exists()) {
            try {
                excelFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return excelFile;
    }

    public FileOutputStream getExcelFileOutputStream() {
        try {
            return new FileOutputStream(getExcelFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FileInputStream getExcelFileInputStream() {
        try {
            return new FileInputStream(getExcelFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isEmpty() {
        return getExcelFile().length() == 0;
    }
}
