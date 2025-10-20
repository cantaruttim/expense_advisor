package br.com.cantarutti.mapper;

import br.com.cantarutti.data.dto.ExpensesDTO;
import br.com.cantarutti.model.ExpensesModel;

public class ObjectExpensesMapper {

    // expenses model to expenses dto
    public static ExpensesDTO ExpensesToDTO(ExpensesModel model) {

        ExpensesDTO dto = new ExpensesDTO();
        dto.setId(model.getId());
        dto.setOwner(model.getOwner());
        dto.setCard(model.getCard());
        dto.setAnomes(model.getAnomes());
        dto.setValue(model.getValue());
        return dto;
    }
}
