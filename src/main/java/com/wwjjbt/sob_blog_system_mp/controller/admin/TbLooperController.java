package com.wwjjbt.sob_blog_system_mp.controller.admin;


import com.wwjjbt.sob_blog_system_mp.pojo.TbLooper;
import com.wwjjbt.sob_blog_system_mp.response.ResponseResult;
import com.wwjjbt.sob_blog_system_mp.service.TbLooperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangji
 * @since 2020-06-21
 */
@RestController
@RequestMapping("/admin/looper")
public class TbLooperController {

    @Autowired
    private TbLooperService looperService;

    /*
     * 添加looper
     * */
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addLoop(@RequestBody TbLooper looper){

        return looperService.addLooper(looper);
    }

    /*
     * 删除loop
     * */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{loopId}")
    public ResponseResult delLoop(@PathVariable("loopId") String loopId){

        return looperService.deleteLoopById(loopId);
    }

    /*
     * 修改loop
     * */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{loopId}")
    public ResponseResult updateLoop(@PathVariable("loopId") String loopId,
                                     @RequestBody TbLooper looper){

        return looperService.updateLoop(loopId,looper);
    }


    /*
     * 查询loop单挑
     * */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{loopId}")
    public ResponseResult getLoop(@PathVariable("loopId") String loopId){

        return looperService.getLoopById(loopId);
    }

    /*
     * 查询loop列表
     * */
//    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listLoops(){


        return looperService.getLoopList();
    }

}

