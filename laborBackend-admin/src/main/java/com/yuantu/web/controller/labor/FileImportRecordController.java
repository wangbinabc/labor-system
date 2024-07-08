package com.yuantu.web.controller.labor;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.labor.domain.FileImportRecord;
import com.yuantu.labor.service.IFileImportRecordService;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.common.core.page.TableDataInfo;

/**
 * 导入记录Controller
 *
 * @author ruoyi
 * @date 2023-11-10
 */
@RestController
@RequestMapping("/system/record")
public class FileImportRecordController extends BaseController {

    @Autowired
    private IFileImportRecordService fileImportRecordService;

    /**
     * 查询导入记录列表
     */
    //@PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(FileImportRecord fileImportRecord) {
        startPage();
        List<FileImportRecord> list = fileImportRecordService.selectFileImportRecordList(fileImportRecord);
        return getDataTable(list);
    }

    /**
     * 导出导入记录列表
     */
    //@PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "导入记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FileImportRecord fileImportRecord) {
        List<FileImportRecord> list = fileImportRecordService.selectFileImportRecordList(fileImportRecord);
        ExcelUtil<FileImportRecord> util = new ExcelUtil<FileImportRecord>(FileImportRecord.class);
        util.exportExcel(response, list, "导入记录数据");
    }

    /**
     * 获取导入记录详细信息
     */
  //  @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(fileImportRecordService.selectFileImportRecordById(id));
    }

    /**
     * 新增导入记录
     */
    //@PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "导入记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FileImportRecord fileImportRecord) {
        return toAjax(fileImportRecordService.insertFileImportRecord(fileImportRecord));
    }

    /**
     * 修改导入记录
     */
   // @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "导入记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FileImportRecord fileImportRecord) {
        return toAjax(fileImportRecordService.updateFileImportRecord(fileImportRecord));
    }

    /**
     * 删除导入记录
     */
   // @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "导入记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(fileImportRecordService.deleteFileImportRecordByIds(ids));
    }



}
