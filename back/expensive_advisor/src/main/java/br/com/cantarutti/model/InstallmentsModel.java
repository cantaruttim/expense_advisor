package br.com.cantarutti.model;

import java.util.Objects;

public class InstallmentsModel {

    private String YEARMONTH;
    private String origin;
    private String card;
    private String installment;
    private String totalInstallment;
    private Double value;

    public String getYEARMONTH() {
        return YEARMONTH;
    }

    public void setYEARMONTH(String YEARMONTH) {
        this.YEARMONTH = YEARMONTH;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getTotalInstallment() {
        return totalInstallment;
    }

    public void setTotalInstallment(String totalInstallment) {
        this.totalInstallment = totalInstallment;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof InstallmentsModel that)) return false;
        return Objects.equals(getYEARMONTH(), that.getYEARMONTH()) && Objects.equals(getOrigin(), that.getOrigin()) && Objects.equals(getCard(), that.getCard()) && Objects.equals(getInstallment(), that.getInstallment()) && Objects.equals(getTotalInstallment(), that.getTotalInstallment()) && Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getYEARMONTH(), getOrigin(), getCard(), getInstallment(), getTotalInstallment(), getValue());
    }
}
