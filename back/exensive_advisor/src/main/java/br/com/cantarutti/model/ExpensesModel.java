package br.com.cantarutti.model;

import java.util.Objects;

public class ExpensesModel {

    private Long id;
    private String owner;
    private String card ;
    private String anomes;
    private Double value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getAnomes() {
        return anomes;
    }

    public void setAnomes(String anomes) {
        this.anomes = anomes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpensesModel that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(owner, that.owner) && Objects.equals(card, that.card) && Objects.equals(anomes, that.anomes) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, card, anomes, value);
    }

}
