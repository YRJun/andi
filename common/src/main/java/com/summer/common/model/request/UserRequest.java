package com.summer.common.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Renjun Yu
 * @date 2024/01/21 14:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRequest extends AndiBaseRequest{
    private String username;
}
