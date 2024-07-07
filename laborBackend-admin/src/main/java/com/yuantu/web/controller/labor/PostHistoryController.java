package com.yuantu.web.controller.labor;

import com.yuantu.common.annotation.Excel;
import com.yuantu.common.annotation.Log;
import com.yuantu.common.core.controller.BaseController;
import com.yuantu.common.core.domain.AjaxResult;
import com.yuantu.common.core.page.TableDataInfo;
import com.yuantu.common.enums.BusinessType;
import com.yuantu.common.utils.DateUtils;
import com.yuantu.common.utils.poi.ExcelUtil;
import com.yuantu.labor.domain.PostHistory;
import com.yuantu.labor.service.IPostHistoryService;
import com.yuantu.labor.vo.ExportDivideVO;
import com.yuantu.labor.vo.ExportVO;
import com.yuantu.labor.vo.PostQueryParamsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 职位变更Controller
 * 
 * @author ruoyi
 * @date 2023-09-08
 */
@Api("岗位调动历史管理")
@RestController
@RequestMapping("/labor/posthistory")
public class PostHistoryController extends BaseController
{
    @Autowired
    private IPostHistoryService postHistoryService;


    /**
     * 查询职位变更列表
     */
   // @PreAuthorize("@ss.hasPermi('labor:posthistory:list')")
    @GetMapping("/list")
    @ApiOperation("查询职位变更列表")
    public TableDataInfo list(PostHistory postHistory)
    {
        startPage();
        List<PostHistory> list = postHistoryService.selectPostHistoryList(postHistory);
        return getDataTable(list);
    }

    /**
     * 筛选查询职位变更列表
     */
   // @PreAuthorize("@ss.hasPermi('labor:posthistory:list')")
//    @GetMapping("/screen")
//    @ApiOperation("筛选查询职位变更列表")
//    public TableDataInfo  selectPostHistoryListByScreen(PostHistoryScreenVO postHistoryScreenVO){
//        startPage();
//        List<PostHistory> list = postHistoryService.selectPostHistoryListByScreen(postHistoryScreenVO);
//        return  getDataTable(list);
//    }


    /**
     * 条件查询职位变更列表
     */
    //@PreAuthorize("@ss.hasPermi('labor:posthistory:list')")
    @GetMapping("/listByParams")
    @ApiOperation("条件查询职位变更列表")
    public TableDataInfo listByParams(PostQueryParamsVo paramsVo)
    {
        startPage();
        List<PostHistory> list = postHistoryService.selectPostHistoryListByParamsVo(paramsVo);
        return getDataTable(list);
    }


    /**
     * 统计职位变更列表,按年降序排序，如第一条记录为今年，第二条为去年
     */
 //  @PreAuthorize("@ss.hasPermi('labor:posthistory:count')")
    @GetMapping("/count")
   @ApiOperation("统计职位变更列表")
    public AjaxResult countByYears(Long years)
    {
        years = years==null?Long.valueOf(5):years;
        Map map= postHistoryService.countPostHistoryByYears(years);

        return success(map);
    }

    /**
     * 导出职位变更列表
     */
  //  @PreAuthorize("@ss.hasPermi('labor:posthistory:export')")
    @Log(title = "职位变更", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出职位变更列表")
    public void export(HttpServletResponse response, @RequestBody ExportVO export) {
        List<PostHistory>  list =  postHistoryService.findExportInfos(export);
        ExcelUtil<PostHistory> util = new ExcelUtil<>(PostHistory.class);
        Field[] declaredFields = PostHistory.class.getDeclaredFields();
        List<String> fieldNamesWithExcel = new ArrayList<>();
        for (Field field : declaredFields) {
            ReflectionUtils.makeAccessible(field);
            if (field.isAnnotationPresent(Excel.class)) {
                fieldNamesWithExcel.add(field.getName());
            }
        }
        List<String> excludeFieldNames = new ArrayList<>();
        List<String> fieldNames = export.getFieldNames();
        if (!CollectionUtils.isEmpty(fieldNames)) {
            for (String s : fieldNamesWithExcel) {
                if (!fieldNames.contains(s)) {
                    excludeFieldNames.add(s);
                }
            }
            if (!CollectionUtils.isEmpty(excludeFieldNames)) {
                String[] excludeArr = new String[excludeFieldNames.size()];
                excludeFieldNames.toArray(excludeArr);
                util.hideColumn(excludeArr);
            }
        }
        util.exportExcel(response, list, "职位变更数据");;
    }

    /**
     * 表格拆分导出职位变更数据信息
     */
    @ApiOperation("表格拆分导出职位变更数据信息")
 //     @PreAuthorize("@ss.hasPermi('labor:posthistory:export')")
    @Log(title = "职位变更数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export/divide")
    public void exportDivide(HttpServletResponse response, @RequestBody ExportDivideVO export) {
        postHistoryService.exportDivide(response, export);
    }

    /**
     * 获取职位变更详细信息
     */
    @ApiOperation("获取职位变更详细信息")
  //  @PreAuthorize("@ss.hasPermi('labor:posthistory:query')")
    @GetMapping(value = "/{phId}")
    public AjaxResult getInfo(@PathVariable("phId") Long phId)
    {
        return success(postHistoryService.selectPostHistoryByPhId(phId));
    }

    /**
     * 新增职位变更
     */
//    @PreAuthorize("@ss.hasPermi('labor:posthistory:add')")
    @Log(title = "职位变更", businessType = BusinessType.INSERT)
    @ApiOperation("新增职位变更")
    @PostMapping
    public AjaxResult add(@RequestBody PostHistory postHistory)
    {
        postHistory.setCreateBy(getUsername());
        postHistory.setCreateTime(DateUtils.getNowDate());
        postHistory.setInsertType(Long.valueOf(0));
        return toAjax(postHistoryService.insertPostHistory(postHistory));
    }

 //  @PreAuthorize("@ss.hasPermi('labor:posthistory:import')")
    @ApiOperation("导入岗位变动信息")
    @PostMapping("/import")
    public AjaxResult importInfos(@RequestBody MultipartFile file) {
        String username = getUsername();
        Long userId = getUserId();
        return success(postHistoryService.uploadPostInfosFile(file,userId,username));
    }


    /**
     * 修改职位变更
     */
    @PreAuthorize("@ss.hasPermi('labor:posthistory:edit')")
    @Log(title = "职位变更", businessType = BusinessType.UPDATE)
    @ApiOperation("修改职位变更")
    @PutMapping
    public AjaxResult edit(@RequestBody PostHistory postHistory)
    {
        postHistory.setUpdateBy(getUsername());
        postHistory.setUpdateTime(DateUtils.getNowDate());
        return toAjax(postHistoryService.updatePostHistory(postHistory));
    }


 //   @PreAuthorize("@ss.hasPermi('labor:posthistory:import')")
    @ApiOperation("下载岗位变动导入模板")
    @GetMapping("/downloadExecl")
    public void downloadExcel(HttpServletResponse response)
    {
//       ExcelUtil<PostHistorySimpleVO> util = new ExcelUtil<>(PostHistorySimpleVO.class);
//        util.importTemplateExcel(response, "岗位变动导入模板");
          postHistoryService.downloadExcelTemplate(response);
    }


    /**
     * 删除职位变更
     */
    @ApiOperation("删除职位变更")
    @PreAuthorize("@ss.hasPermi('labor:posthistory:remove')")
    @Log(title = "职位变更", businessType = BusinessType.DELETE)
	@DeleteMapping("/{phIds}")
    public AjaxResult remove(@PathVariable Long[] phIds)
    {
        return toAjax(postHistoryService.deletePostHistoryByPhIds(phIds));
    }
}
