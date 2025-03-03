package org.example.springboot.controller.Dict;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import org.example.springboot.common.Result;
import org.example.springboot.entity.Dict.DictItem;
import org.example.springboot.mapper.Dict.DictItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.springboot.common.Result.error;
import static org.example.springboot.common.Result.success;


@RestController
@RequestMapping("/dictitem")
public class DictItemController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DictItemController.class);
    @Autowired
    private DictItemMapper dictItemMapper;

    /**
     * 新增/修改字典项
     * @param dictItem
     * @return
     */
    @PostMapping("/save")
    @Operation(summary = "新增/修改字典项")
    public Result<?> save(@RequestBody DictItem dictItem) {
        QueryWrapper<DictItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("item_key",dictItem.getItemKey());

        List<Object> exitDictItem = dictItemMapper.selectObjs(queryWrapper);
        if(!exitDictItem.isEmpty()){
            return error("-1","该项目已存在");
        }
        boolean result = dictItemMapper.insert(dictItem)>0;

        return result? success() : error("-1", "保存失败");
    }


    @Operation(summary = "更新字典项")
    @PutMapping("/{id}")
    public Result<?>  update(@PathVariable int id, @RequestBody DictItem dictItem) {
        dictItem.setId(id);
        LOGGER.info("UPDATE dictItem:"+dictItem);
        int res = dictItemMapper.updateById(dictItem);
        if(res>0){
            return success(dictItem);
        }else{
            return error("-1","修改失败");
        }
    }

    @GetMapping("/findByType")
    @Operation(summary = "根据字典类型编码查询字典项列表")
    public Result<?> getByTypeCode(@RequestParam(defaultValue = "") String code) {
        LOGGER.info("itemList CODE:"+code);
        DictItem dictItem = new DictItem();
        dictItem.setDictTypeCode(code);
        QueryWrapper<DictItem> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("dict_type_code", dictItem.getDictTypeCode());
        List<DictItem> itemList = dictItemMapper.selectList(queryWrapper);
        LOGGER.info("LOADING itemList:"+itemList);
        return success(itemList);
    }
    /**
     * 根据ID删除字典项
     * @param id
     * @return
     */
    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "根据ID删除字典项")
    public Result<?> deleteById(@PathVariable Integer id) {
        boolean result = dictItemMapper.deleteById(id) > 0;
        return result ? success() : error("-1", "删除失败");
    }

    /**
     * 批量删除字典项
     * @param idList
     * @return
     */
    @DeleteMapping("/deleteBatch")
    @Operation(summary = "批量删除字典项")
    public Result<?> deleteBatch(@RequestParam List<Integer> idList) {
        boolean result = dictItemMapper.deleteByIds(idList) > 0;
        return result ? success() : error("-1", "删除失败");
    }

    /**
     * 查询所有字典项
     * @return
     */
    @GetMapping("/findAll")
    @Operation(summary = "查询所有字典项")
    public Result<?> findAll() {
        QueryWrapper<DictItem> queryWrapper = new QueryWrapper<>();
        List<DictItem> dictItemList = dictItemMapper.selectList(queryWrapper);
        return success(dictItemList);
    }

    /**
     * 分页查询字典项
     * @param currentPage
     * @param size
     * @param itemKey
     * @return
     */
    @GetMapping("/findPage")
    @Operation(summary = "分页查询字典项")
    public Result<?> findPage(@RequestParam Integer currentPage,
                              @RequestParam Integer size,
                              @RequestParam String  code,
                              @RequestParam(name = "itemKey", defaultValue = "") String itemKey) {
        Page<DictItem> page = new Page<>(currentPage, size);
        DictItem dictItem = new DictItem();
        dictItem.setDictTypeCode(code);
        dictItem.setItemKey(itemKey);
        QueryWrapper<DictItem> queryWrapper = new QueryWrapper<>();
        if(!code.isEmpty()){
            queryWrapper.eq("dict_type_code", dictItem.getDictTypeCode());
        }
        if (!itemKey.isEmpty()) {
            queryWrapper.like("item_key", dictItem.getItemKey());
        }
        Page<DictItem> dictItemPage = dictItemMapper.selectPage(page, queryWrapper);
        return success(dictItemPage);
    }
}