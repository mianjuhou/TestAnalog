package com.potevio.analog.dao;

import com.potevio.analog.pojo.ExcelData;
import com.potevio.analog.util.ExcelFileUtil;
import com.potevio.analog.util.IdWorker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Component
public class ExcelConfigDao {

    public static final String[] excelColumns = {"id", "numOfPktsPerTime", "intervalOfPerTime", "intervalOfPerPkt", "portNum", "testType", "dataLength", "ip", "callLength", "batchNum", "batchDelay"};

    @Autowired
    private IdWorker idWorker;

    public List<ExcelData> getExcelConfig() {
        if (ExcelFileUtil.INSTANCE.isEmpty()) {//文件为空不读取
            return null;
        }
        XSSFWorkbook workbook = null;
        try {//文件格式错误不读取
            workbook = new XSSFWorkbook(ExcelFileUtil.INSTANCE.getExcelFileInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Sheet sheet = workbook.getSheet("configlist");
        if (sheet == null) {//不存在configlist的sheet表不读取
            return null;
        }
        //数据不完整的行过滤掉
        if (sheet.getLastRowNum() == 0) {//sheet中无行数据
            if (sheet.getRow(0).getLastCellNum() == 0) {
                return null;
            }
        }
        List<ExcelData> dataList = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            ExcelData excelData = drawExcelRowData(sheet.getRow(i));
            if (excelData != null) {
                dataList.add(excelData);
            }
        }
        if (dataList.isEmpty()) {
            return null;
        }
        return dataList;
    }

    public List<ExcelData> addExcelConfig(ExcelData data) {
        data.setId(idWorker.nextId() + "");
        List<ExcelData> excelDataList = getExcelConfig();
        if (excelDataList == null) {
            excelDataList = new ArrayList<>();
        }
        excelDataList.add(data);
        addExcelConfigs(excelDataList);
        return excelDataList;
    }

    public List<ExcelData> deleteExcelConfig(String id) {
        List<ExcelData> excelConfig = getExcelConfig();
        Iterator<ExcelData> it = excelConfig.iterator();
        boolean hasRemoved = false;
        while (it.hasNext()) {
            ExcelData next = it.next();
            if (id.equals(next.getId())) {
                it.remove();
                hasRemoved = true;
            }
        }
        if (hasRemoved) {
            addExcelConfigs(excelConfig);
        }
        return excelConfig;
    }

    /**
     * 把所有的pojo数据导出到Excel文件中
     */
    public void addExcelConfigs(List<ExcelData> datas) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("configlist");
        for (int i = 0; i < datas.size(); i++) {
            createExcelRow(sheet.createRow(i), datas.get(i));
        }
        try {
            workbook.write(ExcelFileUtil.INSTANCE.getExcelFileOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把pojo对象中的数据添加到Excel行对象中
     */
    public void createExcelRow(Row row, ExcelData data) {
        for (int j = 0; j < excelColumns.length; j++) {
            String value = null;
            switch (j) {
                case 0:
                    value = data.getId();
                    break;
                case 1:
                    value = data.getNumOfPktsPerTime();
                    break;
                case 2:
                    value = data.getIntervalOfPerTime();
                    break;
                case 3:
                    value = data.getIntervalOfPerPkt();
                    break;
                case 4:
                    value = data.getPortNum();
                    break;
                case 5:
                    value = data.getTestType();
                    break;
                case 6:
                    value = data.getDataLength();
                    break;
                case 7:
                    value = data.getIp();
                    break;
                case 8:
                    value = data.getCallLength();
                    break;
                case 9:
                    value = data.getBatchNum();
                    break;
                case 10:
                    value = data.getBatchDelay();
                    break;
            }
            Cell cell = row.createCell(j);
            if (value != null) {
                cell.setCellValue(value);
            }
        }
    }

    /**
     * 从Excel文件的一行中提取数据组装成一个对象
     */
    public ExcelData drawExcelRowData(Row row) {
        short cellNum = row.getLastCellNum();
        if (cellNum > 0) {
            ExcelData excelData = new ExcelData();
            for (int j = 0; j < cellNum; j++) {
                String value = null;
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                if (row.getCell(j).getCellType() == CellType.NUMERIC) {
                    value = String.valueOf((long) row.getCell(j).getNumericCellValue());
                } else {
                    value = row.getCell(j).getStringCellValue();
                }
                switch (j) {
                    case 0:
                        excelData.setId(value);
                        break;
                    case 1:
                        excelData.setNumOfPktsPerTime(value);
                        break;
                    case 2:
                        excelData.setIntervalOfPerTime(value);
                        break;
                    case 3:
                        excelData.setIntervalOfPerPkt(value);
                        break;
                    case 4:
                        excelData.setPortNum(value);
                        break;
                    case 5:
                        excelData.setTestType(value);
                        break;
                    case 6:
                        excelData.setDataLength(value);
                        break;
                    case 7:
                        excelData.setIp(value);
                        break;
                    case 8:
                        excelData.setCallLength(value);
                        break;
                    case 9:
                        excelData.setBatchNum(value);
                        break;
                    case 10:
                        excelData.setBatchDelay(value);
                        break;
                }
            }
            return excelData;
        }
        return null;
    }

}
