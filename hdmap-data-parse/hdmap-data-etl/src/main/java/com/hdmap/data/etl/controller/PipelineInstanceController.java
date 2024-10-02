//package com.hdmap.data.etl.controller;
//
//import com.hdmap.data.etl.entity.PipelineInstance;
//import com.hdmap.data.etl.service.PipelineInstanceService;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//
///**
// * 数据映射任务(PipelineInstance)表控制层
// *
// * @author makejava
// * @since 2024-07-05 11:04:23
// */
//@RestController
//@RequestMapping("pipelineInstance")
//public class PipelineInstanceController {
//    /**
//     * 服务对象
//     */
//    @Resource
//    private PipelineInstanceService pipelineInstanceService;
//
//    /**
//     * 分页查询
//     *
//     * @param pipelineInstance 筛选条件
//     * @param pageRequest      分页对象
//     * @return 查询结果
//     */
//    @GetMapping
//    public ResponseEntity<Page<PipelineInstance>> queryByPage(PipelineInstance pipelineInstance, PageRequest pageRequest) {
//        return ResponseEntity.ok(this.pipelineInstanceService.queryByPage(pipelineInstance, pageRequest));
//    }
//
//    /**
//     * 通过主键查询单条数据
//     *
//     * @param id 主键
//     * @return 单条数据
//     */
//    @GetMapping("{id}")
//    public ResponseEntity<PipelineInstance> queryById(@PathVariable("id")  id) {
//        return ResponseEntity.ok(this.pipelineInstanceService.queryById(id));
//    }
//
//    /**
//     * 新增数据
//     *
//     * @param pipelineInstance 实体
//     * @return 新增结果
//     */
//    @PostMapping
//    public ResponseEntity<PipelineInstance> add(PipelineInstance pipelineInstance) {
//        return ResponseEntity.ok(this.pipelineInstanceService.insert(pipelineInstance));
//    }
//
//    /**
//     * 编辑数据
//     *
//     * @param pipelineInstance 实体
//     * @return 编辑结果
//     */
//    @PutMapping
//    public ResponseEntity<PipelineInstance> edit(PipelineInstance pipelineInstance) {
//        return ResponseEntity.ok(this.pipelineInstanceService.update(pipelineInstance));
//    }
//
//    /**
//     * 删除数据
//     *
//     * @param id 主键
//     * @return 删除是否成功
//     */
//    @DeleteMapping
//    public ResponseEntity<Boolean> deleteById( id) {
//        return ResponseEntity.ok(this.pipelineInstanceService.deleteById(id));
//    }
//
//}
//
