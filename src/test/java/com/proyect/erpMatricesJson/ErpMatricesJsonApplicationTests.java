package com.proyect.erpMatricesJson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyect.erpMatricesJson.entities.UserDefinedTableRow;
import com.proyect.erpMatricesJson.services.UserDefinedTableRowService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ErpMatricesJsonApplicationTests {

    @Autowired private UserDefinedTableRowService userDefinedTableRowService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @Rollback(false)
    void testRowSave() {
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put("name", "Felipe");
        dataToSave.put("age", 23);
        dataToSave.put("time", ZonedDateTime.now());
        dataToSave.put("boolean", true);
        dataToSave.put("Nombre con mamádas ¬|·½½¬·||·|", "Hola");

        UserDefinedTableRow rowToSave = new UserDefinedTableRow();
        rowToSave.setData(dataToSave);

        Long savedId = userDefinedTableRowService.save(rowToSave).getId();

        UserDefinedTableRow savedRow = userDefinedTableRowService.getById(savedId).get();
        Map<String, Object> savedData = savedRow.getData();

        try {
            System.out.println("Data to save: " + objectMapper.writeValueAsString(dataToSave));
            System.out.println("Saved data: " + objectMapper.writeValueAsString(savedData));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals(dataToSave, savedData);
    }
}
