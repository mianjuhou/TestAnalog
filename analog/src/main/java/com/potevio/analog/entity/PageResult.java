package com.potevio.analog.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "返回分页结果封装类")
public class PageResult<T> {
    @ApiModelProperty(value = "数据总条数", notes = "数据总条数", required = true)
    private long total;
    @ApiModelProperty(value = "一页数据的列表", notes = "一页数据的列表", required = false)
    private List<T> rows;

    public PageResult() {
    }

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
