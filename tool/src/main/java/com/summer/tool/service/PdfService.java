package com.summer.tool.service;

import com.summer.common.model.response.AndiResponse;
import com.summer.common.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/03/22 21:13
 */
@Service
@Slf4j
public class PdfService {

    @Value("${file.tmp.path}")
    private String fileTmpPath;

    public AndiResponse<?> writePdfTest() {
        try (PDDocument doc = new PDDocument()) {
            PDPage myPage = new PDPage();
            doc.addPage(myPage);
            try (PDPageContentStream cont = new PDPageContentStream(doc, myPage)) {
                final PDType1Font pdType1Font = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
                cont.beginText();
                cont.setFont(pdType1Font, 12);
                cont.setLeading(14.5f);
                cont.newLineAtOffset(25, 700);
                //String line1 = "这是第一句";
                String line1 = "this is one";
                cont.showText(line1);
                cont.newLine();
                //String line2 = "这是第二句";
                String line2 = "this is two";
                cont.showText(line2);
                cont.newLine();
                //String line3 = "这是第三句";
                String line3 = "this is three";
                cont.showText(line3);
                cont.newLine();
                //String line4 = "这是第四句";
                String line4 = "this is four";
                cont.showText(line4);
                cont.newLine();
                cont.endText();
            }

            PDPage myPage1 = new PDPage();
            doc.addPage(myPage1);
            try (PDPageContentStream cont = new PDPageContentStream(doc, myPage1)) {
                final PDType1Font pdType1Font = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
                cont.beginText();
                cont.setFont(pdType1Font, 12);
                cont.setLeading(14.5f);
                cont.newLineAtOffset(25, 700);
                //String line1 = "这是第一句";
                String line1 = "this is five";
                cont.showText(line1);
                cont.newLine();
                //String line2 = "这是第二句";
                String line2 = "this is six";
                cont.showText(line2);
                cont.newLine();
                //String line3 = "这是第三句";
                String line3 = "this is seven";
                cont.showText(line3);
                cont.newLine();
                //String line4 = "这是第四句";
                String line4 = "this is eight";
                cont.showText(line4);
                cont.newLine();
                cont.endText();
            }

            doc.save("D:\\Test\\test.pdf");
            return AndiResponse.success();
        } catch (IOException e) {
            log.error("pdf生成失败", e);
            return AndiResponse.fail();
        }
    }

    public AndiResponse<?> writePdfAsWord(final MultipartFile file) {
        try {
            FileUtil.multipartToFile(file, fileTmpPath);
        } catch (IOException e) {
            return AndiResponse.fail();
        }
        try (PDDocument pdfDocument = Loader.loadPDF(new RandomAccessReadBufferedFile(fileTmpPath + file.getOriginalFilename()))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdfDocument);
            // 创建Word文档
            new XWPFDocument();
            // 添加文本到Word文档

            return AndiResponse.success(text);
        } catch (Exception e) {
            log.error("pdf转换word失败", e);
            return AndiResponse.fail();
        } finally {
            FileUtil.deleteFile(fileTmpPath + file.getOriginalFilename());
        }

    }
}
