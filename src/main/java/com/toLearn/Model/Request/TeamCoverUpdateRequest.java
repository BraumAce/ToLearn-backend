package com.toLearn.Model.Request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 队伍封面更新请求
 * @author BraumAce
 */
@Data
@ApiModel(value = "队伍封面更新请求")
public class TeamCoverUpdateRequest {

    @ApiModelProperty(value = "队伍id")
    private Long id;

    @ApiModelProperty(value = "封面文件")
    private MultipartFile file;
}
