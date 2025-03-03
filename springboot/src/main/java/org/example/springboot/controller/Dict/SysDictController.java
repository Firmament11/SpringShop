package org.example.springboot.controller.Dict;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.springboot.common.Result;
import org.example.springboot.entity.Dict.SysDict;
import org.example.springboot.mapper.Dict.DictTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.springboot.common.Result.error;
import static org.example.springboot.common.Result.success;


@RestController
@RequestMapping("/sysdict")
public class SysDictController {

    @Autowired
    private DictTypeMapper sysDictMapper;

    /**
     * 新增/修改字典类型
     * @param sysDict
     * @return
     */
    @PostMapping("/save")
    @Operation(summary = "新增/修改字典类型")
    public Result<?> save(@RequestBody @Valid SysDict sysDict) {
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type_code",sysDict.getDictTypeCode());
        List<Object> exitDictType = sysDictMapper.selectObjs(queryWrapper);
        if(exitDictType!=null){
            return error("-1","该类型已存在");
        }
        int result = sysDictMapper.insert(sysDict);
        return result>0 ? success() : error("-1", "保存失败");
    }

    /**
     * 根据ID删除字典类型
     * @param id
     * @return
     */
    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "根据ID删除字典类型")
    public Result<?> deleteById(@PathVariable Integer id) {
        boolean result = sysDictMapper.deleteById(id) > 0;
        return result ? success() : error("-1", "删除失败");
    }

    /**
     * 批量删除字典类型
     * @param idList
     * @return
     */
    @PostMapping("/deleteBatch")
    @Operation(summary = "批量删除字典类型")
    public Result<?> deleteBatch(@RequestBody List<Integer> idList) {
        boolean result = sysDictMapper.deleteBatchIds(idList) > 0;
        return result ? success() : error("-1", "删除失败");
    }

    /**
     * 查询所有字典类型
     * @return
     */
    @GetMapping("/findAll")
    @Operation(summary = "查询所有字典类型")
    public Result<?> findAll() {
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        List<SysDict> sysDictList = sysDictMapper.selectList(queryWrapper);
        return success(sysDictList);
    }

    /**
     * 分页查询字典类型
     * @param pageNum
     * @param pageSize
     * @param dictTypeName
     * @return
     */
    @GetMapping("/findPage")
    @Operation(summary = "分页查询字典类型")
    public Result<?> findPage(@RequestParam Integer pageNum,
                              @RequestParam Integer pageSize,
                              @RequestParam(name = "dictTypeName", defaultValue = "") String dictTypeName) {
        Page<SysDict> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        if (!dictTypeName.isEmpty()) {
            queryWrapper.like("dict_type_name", dictTypeName);
        }
        Page<SysDict> sysDictPage = sysDictMapper.selectPage(page, queryWrapper);
        return success(sysDictPage);
    }
}