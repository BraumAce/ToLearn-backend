package com.toLearn.Controller;

import com.toLearn.Common.BaseResponse;
import com.toLearn.Common.ErrorCode;
import com.toLearn.Common.ResultUtils;
import com.toLearn.Exception.BusinessException;
import com.toLearn.Model.Domain.User;
import com.toLearn.Properties.ToLearnProperties;
import com.toLearn.Service.UserService;
import com.toLearn.Utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

import static com.toLearn.Constants.SystemConstants.DEFAULT_BUFFER_SIZE;
import static com.toLearn.Constants.SystemConstants.FILE_END;


/**
 * 文件控制器
 * @author BraumAce
 */
@RestController
@RequestMapping("/common")
@CrossOrigin("http://localhost:3000")
@Api(tags = "文件管理模块")
public class FileController {

    // 基本路径
    @Value("${tolearn.img}")
    private String basePath;

    // 用户服务
    @Resource
    private UserService userService;

    @Resource
    private ToLearnProperties toLearnProperties;

    @Value("${tolearn.qiniu.url:null}")
    private String qiniuUrl;

    @Value("${server.servlet.session.cookie.domain}")
    private String host;

    @Value("${server.port}")
    private String port;

    /**
     * 文件上传
     * @param file    文件
     * @param request 请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件"),
                        @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> upload(MultipartFile file, HttpServletRequest request) {
        if (file == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请上传文件");
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "请登录");
        }
        if (toLearnProperties.isUseLocalStorage()) {
            String fileName = FileUtils.uploadFile2Local(file);
            String fileUrl = "http://" + host + ":" + port + "/api/common/image/" + fileName;
            User user = new User();
            user.setId(loginUser.getId());
            user.setAvatarUrl(fileUrl);
            boolean success = userService.updateById(user);
            if (!success) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "头像上传失败");
            }
            return ResultUtils.success(fileUrl);
        } else {
            String filename = FileUtils.uploadFile2Cloud(file);
            String fileUrl = qiniuUrl + filename;
            User user = new User();
            user.setId(loginUser.getId());
            user.setAvatarUrl(fileUrl);
            boolean success = userService.updateById(user);
            if (!success) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "头像上传失败");
            }
            return ResultUtils.success(fileUrl);
        }
    }

    /**
     * 文件下载
     * @param name     名字
     * @param response 响应
     */
    @GetMapping("/image/{name}")
    @ApiOperation(value = "文件下载")
    @ApiImplicitParams({@ApiImplicitParam(name = "name", value = "文件名"),
                        @ApiImplicitParam(name = "request", value = "request请求")})
    public void download(@PathVariable String name, HttpServletResponse response) {
        try {
            //获取指定文件
            File img = new File(System.getProperty("user.dir") + basePath + name);
            //获取输入流
            FileInputStream inputStream = new FileInputStream(img);
            //获取输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //指定response类型
            response.setContentType("image/jpeg");
            int len = 0;
            //设置缓冲区大小
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            //将文件从输入流读到缓冲区，输出流读取缓冲区内容
            while ((len = inputStream.read(bytes)) != FILE_END) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
