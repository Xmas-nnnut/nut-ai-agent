package com.xqj.nutaiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.xqj.nutaiagent.constant.FileConstant;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * PDF 生成工具
 */
public class PDFGenerationTool {

    // Markdown格式检测正则表达式
    private static final Pattern MARKDOWN_PATTERN = Pattern.compile(
        "(?m)^#+\\s+.*|\\*\\*.*\\*\\*|\\*.*\\*|`.*`|\\[.*\\]\\(.*\\)|^\\s*[-*+]\\s+|^\\s*\\d+\\.\\s+"
    );

    @Tool(description = "Generate a PDF file with given content, supports both plain text and Markdown format")
    public String generatePDF(
            @ToolParam(description = "Name of the file to save the generated PDF") String fileName,
            @ToolParam(description = "Content to be included in the PDF, supports Markdown format") String content) {
        String fileDir = FileConstant.FILE_SAVE_DIR + "/pdf";
        String filePath = fileDir + "/" + fileName;
        
        try {
            // 创建目录
            FileUtil.mkdir(fileDir);
            
            if (isMarkdownContent(content)) {
                // 如果是Markdown格式，转换为HTML再生成PDF
                return generatePDFFromMarkdown(content, filePath);
            } else {
                // 如果是纯文本，使用原有逻辑
                return generatePDFFromText(content, filePath);
            }
        } catch (Exception e) {
            return "Error generating PDF: " + e.getMessage();
        }
    }

    /**
     * 检测内容是否为Markdown格式
     */
    private boolean isMarkdownContent(String content) {
        // 检查是否包含常见的Markdown语法
        return MARKDOWN_PATTERN.matcher(content).find();
    }

    /**
     * 从Markdown内容生成PDF
     */
    private String generatePDFFromMarkdown(String markdownContent, String filePath) throws IOException {
        // 配置CommonMark扩展
        List<Extension> extensions = Arrays.asList(
                TablesExtension.create(),
                HeadingAnchorExtension.create()
        );
        
        // 创建解析器和渲染器
        Parser parser = Parser.builder()
                .extensions(extensions)
                .build();
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(extensions)
                .build();
        
        // 解析Markdown为HTML
        Node document = parser.parse(markdownContent);
        String htmlContent = renderer.render(document);
        
        // 创建完整的HTML文档，包含CSS样式
        String fullHtml = createStyledHtml(htmlContent);
        
        // 配置HTML转PDF
        ConverterProperties properties = new ConverterProperties();
        DefaultFontProvider fontProvider = new DefaultFontProvider();
        
        // 添加自定义字体
        String fontPath = Paths.get("src/main/resources/static/fonts/MiSans-Normal.ttf")
                .toAbsolutePath().toString();
        try {
            fontProvider.addFont(fontPath);
        } catch (Exception e) {
            // 如果自定义字体加载失败，继续使用默认字体
            System.out.println("Warning: Could not load custom font, using default font");
        }
        
        properties.setFontProvider(fontProvider);
        
        // 将HTML转换为PDF
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            HtmlConverter.convertToPdf(fullHtml, outputStream, properties);
        }
        
        return "PDF generated successfully from Markdown to: " + filePath;
    }

    /**
     * 创建带样式的HTML文档
     */
    private String createStyledHtml(String htmlContent) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: 'MiSans-Normal', 'SimSun', serif; margin: 40px; line-height: 1.6; }" +
                "h1, h2, h3, h4, h5, h6 { color: #333; margin-top: 24px; margin-bottom: 16px; }" +
                "h1 { font-size: 28px; border-bottom: 2px solid #eee; padding-bottom: 8px; }" +
                "h2 { font-size: 24px; border-bottom: 1px solid #eee; padding-bottom: 6px; }" +
                "h3 { font-size: 20px; }" +
                "h4 { font-size: 18px; }" +
                "p { margin-bottom: 16px; }" +
                "code { background-color: #f4f4f4; padding: 2px 4px; border-radius: 3px; font-family: monospace; }" +
                "pre { background-color: #f4f4f4; padding: 12px; border-radius: 6px; overflow-x: auto; }" +
                "blockquote { border-left: 4px solid #ddd; margin: 0; padding-left: 16px; color: #666; }" +
                "ul, ol { margin-bottom: 16px; padding-left: 30px; }" +
                "li { margin-bottom: 4px; }" +
                "table { border-collapse: collapse; width: 100%; margin-bottom: 16px; }" +
                "th, td { border: 1px solid #ddd; padding: 8px 12px; text-align: left; }" +
                "th { background-color: #f2f2f2; font-weight: bold; }" +
                "a { color: #0066cc; text-decoration: none; }" +
                "a:hover { text-decoration: underline; }" +
                "hr { border: none; border-top: 1px solid #eee; margin: 24px 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                htmlContent +
                "</body>" +
                "</html>";
    }

    /**
     * 从纯文本生成PDF（原有逻辑）
     */
    private String generatePDFFromText(String content, String filePath) throws IOException {
        // 创建 PdfWriter 和 PdfDocument 对象
        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {
            
            // 自定义字体（需要人工下载字体文件到特定目录）
            String fontPath = Paths.get("src/main/resources/static/fonts/MiSans-Normal.ttf")
                    .toAbsolutePath().toString();
            try {
                PdfFont font = PdfFontFactory.createFont(fontPath,
                        PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                document.setFont(font);
            } catch (Exception e) {
                // 如果自定义字体加载失败，使用默认字体
                System.out.println("Warning: Could not load custom font, using default font");
            }
            
            // 创建段落
            Paragraph paragraph = new Paragraph(content);
            // 添加段落并关闭文档
            document.add(paragraph);
        }
        return "PDF generated successfully from text to: " + filePath;
    }
}
