package com.hms.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hms.dto.PatientRecordDTO;
import com.hms.dto.PatientRecordResponseDTO;
import com.hms.service.PatientRecordService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/patientRecords")
public class PatientRecordController {
	
	private static final String FILE_NAME = "generated_excel.xlsx";

	@Autowired
	private PatientRecordService patientRecordService;

	@GetMapping("/patient-records")
	public Page<PatientRecordDTO> getAllPatientRecords(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return patientRecordService.getAllPatientRecords(pageable);
	}

	@PostMapping
    public ResponseEntity<?> createPatientRecord(@Valid @RequestBody PatientRecordDTO patientRecordDTO, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }
        PatientRecordDTO createdPatientRecord = patientRecordService.createPatientRecord(patientRecordDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatientRecord);
    }

	@GetMapping
	public ResponseEntity<List<PatientRecordDTO>> getAllPatientRecords() {
		List<PatientRecordDTO> patientRecords = patientRecordService.getAllPatientRecords();
		return new ResponseEntity<>(patientRecords, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updatePatientRecord(@PathVariable Integer id,
			@RequestBody PatientRecordDTO patientRecordDTO) {
		patientRecordService.updatePatientRecord(id, patientRecordDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePatientRecord(@PathVariable Integer id) {
		patientRecordService.deletePatientRecord(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/upload")
	public ResponseEntity<PatientRecordResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
		List<String> errors = new ArrayList<>();
		String message = "";

		if (!file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			errors.add("Uploaded file is not an Excel file.");
			return ResponseEntity.badRequest().body(new PatientRecordResponseDTO(false, message, errors));
		}

		String filename = "patients.xlsx";
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get("src/main/resources/static/assets/" + filename);
			Files.write(path, bytes);
		} catch (IOException e) {
			errors.add("Failed to save uploaded file.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new PatientRecordResponseDTO(false, message, errors));
		}

		try (FileInputStream inputStream = new FileInputStream("src/main/resources/static/assets/" + filename);
				Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = sheet.iterator();

			// Skip header row
			if (iterator.hasNext()) {
				iterator.next(); // Skip header row
			}

			DataFormatter dataFormatter = new DataFormatter();
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				List<String> data = new ArrayList<>();
				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					String cellValue = dataFormatter.formatCellValue(currentCell);
					data.add(cellValue);
				}

				// Validate data
				if (data.size() != 5) {
					errors.add("Invalid Excel format for row: " + currentRow.getRowNum());
					continue;
				}
				String id = data.get(0);
				String dateStr = data.get(1).trim();
				String diagnosis = data.get(2);
				String patientName = data.get(3);
				String symptoms = data.get(4);



				//  validation for date format
				LocalDate date;
				try {
					date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy/MM/dd")); // Parse with correct
																								// format
				} catch (DateTimeParseException e) {
					errors.add("Invalid date format for row: " + currentRow.getRowNum() + ". Date value: " + dateStr);
					continue;
				}

				// Create a PatientRecordDTO object
				PatientRecordDTO patientRecord = new PatientRecordDTO();
				patientRecord.setId(Integer.parseInt(id));
				patientRecord.setDate(date);
				patientRecord.setDiagnosis(diagnosis);
				patientRecord.setPatientName(patientName);
				patientRecord.setSymptoms(symptoms);

				// Call the service method to save the patient record using the DTO
				patientRecordService.createPatientRecord(patientRecord);
			}

			if (!errors.isEmpty()) {
				message = "File uploaded with errors.";
				return ResponseEntity.badRequest().body(new PatientRecordResponseDTO(false, message, errors));
			}

			message = "File uploaded successfully.";
			return ResponseEntity.ok(new PatientRecordResponseDTO(true, message, errors));
		} catch (IOException e) {
			errors.add("Failed to process uploaded file.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new PatientRecordResponseDTO(false, message, errors));
		}
	}

	@GetMapping("/download")
	public ResponseEntity<InputStreamResource> downloadFile() {
		List<PatientRecordDTO> patientRecords = patientRecordService.getAllPatientRecords();// Get patient records from
																						// service

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Patients");
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("ID");
			headerRow.createCell(1).setCellValue("Completion Date");
			headerRow.createCell(2).setCellValue("Diagnosis");
			headerRow.createCell(3).setCellValue("Patient Name");
			headerRow.createCell(4).setCellValue("Symptoms");

			int rowNum = 1;
			for (PatientRecordDTO patientRecord : patientRecords) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(patientRecord.getId());
				row.createCell(1).setCellValue(patientRecord.getDate().toString());
				row.createCell(2).setCellValue(patientRecord.getDiagnosis());
				row.createCell(3).setCellValue(patientRecord.getPatientName());
				row.createCell(4).setCellValue(patientRecord.getSymptoms());
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			workbook.close(); 

			InputStreamResource resource = new InputStreamResource(
					new ByteArrayInputStream(outputStream.toByteArray()));
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=patients.xlsx");

			return ResponseEntity.ok().headers(headers)
					.contentType(MediaType
							.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
					.body(resource);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	

	@GetMapping("/downloadExcel")
	public ResponseEntity<InputStreamResource> downloadExcel() throws IOException {
		File file = new File(FILE_NAME);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + FILE_NAME);

		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(resource);
	}
	
	@GetMapping("/downloadPdf")
    public ResponseEntity<InputStreamResource> downloadStatsPdf(HttpServletResponse response) throws IOException {
        // Generate the PDF document
        ByteArrayOutputStream outputStream = generatePdf();

        // Set the headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "download_report.pdf");

        // Return the PDF as a response entity
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray())));
    }

    private ByteArrayOutputStream generatePdf() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();
            document.add(new Paragraph("Download Patient Report"));
            document.add(new Paragraph("Generated on: " + new Date()));
            document.add(new Paragraph("Total Downloads: 10"));
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return outputStream;
    }
}
