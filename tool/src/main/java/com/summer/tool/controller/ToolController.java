package com.summer.tool.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/03/22 20:47
 */
@RestController
@RequestMapping("tool")
@RequiredArgsConstructor
@Tag(name = "ToolController", description = "工具管理")
public class ToolController {

}
