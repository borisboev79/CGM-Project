package cgm.util;

import cgm.model.dto.GuestViewDto;
import cgm.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author javacodepoint.com
 */
@Service
@RequiredArgsConstructor
public class ExcelConverterService {

    private static final String SHEET_NAME = "GroupID_";
    private static final String HEADER_CABIN_NO = "Cabin No.";
    private static final String HEADER_CABIN_CODE = "Cabin Code";
    private static final String HEADER_CABIN_TYPE = "Cabin Type";
    private static final String HEADER_FULL_NAME = "Full Name";
    private static final String HEADER_BIRTH_DATE = "DOB";
    private static final String HEADER_AGE = "Age";
    private static final String HEADER_EMAIL = "Email";
    private static final String HEADER_PHONE = "Phone";
    private static final String HEADER_EGN = "EGN";
    private static final String HEADER_PASSPORT = "Passport";

    private final GuestService guestService;

    /**
     * Method to convert json file to excel file
     *
     * @return Workbook excel workbook
     */
    public Workbook generateExcelWorkbook(Long id) {

        List<GuestViewDto> guestList = guestService.getAllGuests(id);


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SHEET_NAME + id);
        createHeaders(sheet);

        for (GuestViewDto guest : guestList) {
            createRow(sheet, guest);
        }
        return workbook;
    }

    private void createHeaders(Sheet sheet) {
        Row lastRow = sheet.createRow(0);
        lastRow.createCell(0).setCellValue(HEADER_CABIN_NO);
        lastRow.createCell(1).setCellValue(HEADER_CABIN_CODE);
        lastRow.createCell(2).setCellValue(HEADER_CABIN_TYPE);
        lastRow.createCell(3).setCellValue(HEADER_FULL_NAME);
        lastRow.createCell(4).setCellValue(HEADER_BIRTH_DATE);
        lastRow.createCell(5).setCellValue(HEADER_AGE);
        lastRow.createCell(6).setCellValue(HEADER_EMAIL);
        lastRow.createCell(7).setCellValue(HEADER_PHONE);
        lastRow.createCell(8).setCellValue(HEADER_EGN);
        lastRow.createCell(9).setCellValue(HEADER_PASSPORT);
    }

    private void createRow(Sheet sheet, GuestViewDto guest) {
        Row lastRow = sheet.createRow(sheet.getPhysicalNumberOfRows());
        lastRow.createCell(0).setCellValue(guest.getCabinNumber());
        lastRow.createCell(1).setCellValue(guest.getCabinCode());
        lastRow.createCell(2).setCellValue(guest.getCabinType());
        lastRow.createCell(3).setCellValue(guest.getFullName());
        lastRow.createCell(4).setCellValue(guest.getBirthDate().toString());
        lastRow.createCell(5).setCellValue(guest.getAge());
        lastRow.createCell(6).setCellValue(guest.getEmail());
        lastRow.createCell(7).setCellValue(guest.getPhone());
        lastRow.createCell(8).setCellValue(guest.getEGN());
        lastRow.createCell(9).setCellValue(guest.getPassportNumber());
    }

}