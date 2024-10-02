package com.hdmap.task.model.dto;

import lombok.Data;

/**
 * @author admin
 * @version 1.0
 * @date 2024/5/20 18:00
 * @description: 用户简要信息
 */
@Data
public class UserShortInfo {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户角色
     */
    private String role;
}
