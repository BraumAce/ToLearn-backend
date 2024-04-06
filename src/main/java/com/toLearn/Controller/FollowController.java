package com.toLearn.Controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toLearn.Common.BaseResponse;
import com.toLearn.Common.ErrorCode;
import com.toLearn.Common.ResultUtils;
import com.toLearn.Exception.BusinessException;
import com.toLearn.Model.Domain.User;
import com.toLearn.Model.VO.UserVO;
import com.toLearn.Service.FollowService;
import com.toLearn.Service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 关注控制器
 * @author BraumAce
 */
@RestController
@RequestMapping("/follow")
@Api(tags = "关注管理模块")
public class FollowController {
    // 关注服务
    @Resource
    private FollowService followService;

    // 用户服务
    @Resource
    private UserService userService;

    /**
     * 关注用户
     * @param id      id
     * @param request 请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @PostMapping("/{id}")
    @ApiOperation(value = "关注用户")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "关注用户id"),
                    @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> followUser(@PathVariable Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        followService.followUser(id, loginUser.getId());
        return ResultUtils.success("ok");
    }

    /**
     * 获取粉丝
     * @param request     请求
     * @param currentPage 当前页码
     * @return {@link BaseResponse}<{@link Page}<{@link UserVO}>>
     */
    @GetMapping("/fans")
    @ApiOperation(value = "获取粉丝")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<UserVO>> listFans(HttpServletRequest request, String currentPage) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Page<UserVO> userVoPage = followService.pageFans(loginUser.getId(), currentPage);
        return ResultUtils.success(userVoPage);
    }

    /**
     * 获取我关注的用户
     * @param request     请求
     * @param currentPage 当前页码
     * @return {@link BaseResponse}<{@link Page}<{@link UserVO}>>
     */
    @GetMapping("/my")
    @ApiOperation(value = "获取我关注的用户")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<UserVO>> listMyFollow(HttpServletRequest request, String currentPage) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Page<UserVO> userVoPage = followService.pageMyFollow(loginUser.getId(), currentPage);
        return ResultUtils.success(userVoPage);
    }
}
