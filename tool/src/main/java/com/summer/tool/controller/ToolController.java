package com.summer.tool.controller;

import com.summer.common.model.response.AndiResponse;
import com.summer.tool.service.PdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/03/22 20:47
 */
@RestController
@RequestMapping("tool")
@Tag(name = "ToolController", description = "工具管理")
public class ToolController {

    @Resource
    private PdfService pdfService;

    @Operation(summary = "writePdfTest", description = "pdf文件生成测试")
    @PostMapping("writePdfTest")
    public AndiResponse<?> writePdfTest() {
        return pdfService.writePdfTest();
    }


    @Operation(summary = "writePdfAsWord", description = "pdf文件转换成word文件")
    @PostMapping("writePdfAsWord")
    public AndiResponse<?> writePdfAsWord(@RequestParam MultipartFile file) {
        return pdfService.writePdfAsWord(file);
    }
}
