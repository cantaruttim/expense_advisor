package br.com.cantarutti.data.dto;

import org.springframework.data.annotation.Id;


public class ExpensesDTO {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String owner;
    private String card ;
    private String anomes;
    private Double value;



}
