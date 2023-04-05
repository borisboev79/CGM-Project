package cgm.controller;

import cgm.util.ExcelConverterService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class FileExportController {

    private final ExcelConverterService excelConverterService;

    @GetMapping("/{id}/export/")
    public ResponseEntity<Resource> generateWorkbook(@PathVariable Long id) throws IOException {
        return generateExcelResource(this.excelConverterService.generateExcelWorkbook(id));
    }

    private ResponseEntity<Resource> generateExcelResource(final Workbook workbook) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        InputStreamResource file = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=guest-list.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
