package net.artux.mupse.service.reason;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import net.artux.mupse.entity.reason.ReasonEntity;
import net.artux.mupse.model.page.QueryPage;
import net.artux.mupse.model.page.ResponsePage;
import net.artux.mupse.model.reason.ReasonDto;
import net.artux.mupse.model.reason.ReasonMapper;
import net.artux.mupse.repository.contact.ContactRepository;
import net.artux.mupse.repository.reason.ReasonRepository;
import net.artux.mupse.service.user.UserService;
import net.artux.mupse.service.util.PageService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReasonServiceImpl implements ReasonService {

    private final ReasonRepository repository;
    private final ContactRepository contactRepository;
    private final UserService userService;
    private final PageService pageService;
    private final ReasonMapper mapper;

    @Override
    public ResponsePage<ReasonDto> getReasons(QueryPage queryPage) {
        Page<ReasonEntity> page = repository.findAllByOwner(userService.getUserEntity(), pageService.getPageable(queryPage));
        return pageService.mapDataPageToResponsePage(page, mapper::dto);
    }

    @Override
    public ByteArrayInputStream exportAll() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Причины отписок");

        CreationHelper createHelper = workbook.getCreationHelper();

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("dd.MM.yyyy HH:mm"));

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellStyle(headerCellStyle);

        cell = row.createCell(1);
        cell.setCellStyle(headerCellStyle);

        List<ReasonEntity> reasons = repository.findAllByOwner(userService.getUserEntity());

        for (int i = 0; i < reasons.size(); i++) {
            Row header = sheet.createRow(i);
            ReasonEntity reason = reasons.get(i);
            Cell headerCell = header.createCell(0);
            headerCell.setCellStyle(headerCellStyle);
            headerCell.setCellValue(reason.getContact().getEmail());

            headerCell = header.createCell(1);
            headerCell.setCellStyle(headerCellStyle);
            headerCell.setCellValue(reason.getContact().getName());

            headerCell = header.createCell(2);
            headerCell.setCellStyle(headerCellStyle);
            headerCell.setCellValue(reason.getTime());

            headerCell = header.createCell(3);
            headerCell.setCellStyle(headerCellStyle);
            headerCell.setCellValue(reason.getContent());
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public void clear(boolean clearContacts) {
        repository.deleteAllByOwner(userService.getUserEntity());
        if (clearContacts)
            contactRepository.deleteAllByOwnerAndDisabledTrue(userService.getUserEntity());
    }

    @Override
    public void createReason(UUID uuid) {
        ReasonEntity reason = repository.findByContactToken(uuid).orElseGet(ReasonEntity::new);
        reason.setContact(contactRepository.findByToken(uuid).orElseThrow());
        reason.setTime(LocalDateTime.now());
        repository.save(reason);
    }

    @Override
    public void setReasonContent(UUID uuid, String content) {
        ReasonEntity reason = repository.findByContactToken(uuid).orElseThrow();
        reason.setContent(content);
        repository.save(reason);
    }
}
