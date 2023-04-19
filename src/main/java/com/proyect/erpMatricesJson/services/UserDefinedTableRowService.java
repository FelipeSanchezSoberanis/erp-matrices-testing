package com.proyect.erpMatricesJson.services;

import com.proyect.erpMatricesJson.entities.UserDefinedTableRow;
import com.proyect.erpMatricesJson.repositories.UserDefinedTableRowRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDefinedTableRowService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Autowired private UserDefinedTableRowRepository userDefinedTableRowRepo;

    public UserDefinedTableRow save(UserDefinedTableRow rowToSave) {
        rowToSave = prepareForDbSave(rowToSave);
        UserDefinedTableRow savedRow = userDefinedTableRowRepo.save(rowToSave);
        savedRow = constructAfterDbFetch(savedRow);
        return savedRow;
    }

    public Optional<UserDefinedTableRow> getById(Long id) {
        Optional<UserDefinedTableRow> rowOpt = userDefinedTableRowRepo.findById(id);
        if (rowOpt.isEmpty()) return rowOpt;
        UserDefinedTableRow row = rowOpt.get();
        row = constructAfterDbFetch(row);
        return Optional.of(row);
    }

    private UserDefinedTableRow prepareForDbSave(UserDefinedTableRow row) {
        Map<String, Object> data = row.getData();
        Set<String> keys = data.keySet();

        for (String key : keys) {
            Object object = data.get(key);
            if (object instanceof ZonedDateTime) {
                ZonedDateTime zonedDateTime = (ZonedDateTime) object;
                data.put(key, zonedDateTime.format(DATE_TIME_FORMATTER));
            }
        }

        row.setData(data);
        return row;
    }

    private UserDefinedTableRow constructAfterDbFetch(UserDefinedTableRow row) {
        Map<String, Object> data = row.getData();
        Set<String> keys = data.keySet();

        for (String key : keys) {
            Object object = data.get(key);
            try {
                ZonedDateTime zonedDateTime =
                        ZonedDateTime.parse(object.toString(), DATE_TIME_FORMATTER);
                data.put(key, zonedDateTime);
            } catch (DateTimeParseException ex) {

            }
        }

        row.setData(data);
        return row;
    }
}
