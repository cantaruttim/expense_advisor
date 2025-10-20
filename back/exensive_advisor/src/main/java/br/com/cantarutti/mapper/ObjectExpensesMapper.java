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

    // expeses dto to expenses dto
    public static ExpensesModel ExpensesDTOToModel(ExpensesDTO dto) {
        ExpensesModel model = new ExpensesModel();

        model.setId(dto.getId());
        model.setOwner(dto.getOwner());
        model.setCard(dto.getCard());
        model.setAnomes(dto.getAnomes());
        model.setValue(dto.getValue());
        return model;
    }


}
