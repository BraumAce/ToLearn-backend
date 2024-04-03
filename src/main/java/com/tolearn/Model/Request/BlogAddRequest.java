package com.tolearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;

/**
 * 添加博文请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "添加博文请求")
public class BlogAddRequest implements Serializable {
    // 串行版本uid
    @Serial
    private static final long serialVersionUID = 8975136896057535409L;

    @ApiModelProperty(value = "图片")
    private MultipartFile[] images;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "正文")
    private String content;
}
