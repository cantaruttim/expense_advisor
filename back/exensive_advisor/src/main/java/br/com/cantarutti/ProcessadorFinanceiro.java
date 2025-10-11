//package com.exemplo.financeiro;
//
//import io.github.cdimascio.dotenv.Dotenv;
//import com.opencsv.CSVReader;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.*;
//import java.nio.file.*;
//import java.time.*;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class ProcessadorFinanceiro {
//
//    private static final String CAMINHO = "./data";
//    private static final String GASTOS = CAMINHO + "/gastos.xlsx";
//    private static final String TAXA_SELIC = CAMINHO + "/taxa_selic_apurada.csv";
//
//    private static final Map<String, Double> DESCONTOS = Map.of(
//            "10-2025", 870.0,
//            "12-2025", 1250.0,
//            "02-2026", 350.0,
//            "05-2026", 1695.0
//    );
//
//    public static void main(String[] args) throws Exception {
//        // Carregar .env
//        Dotenv dotenv = Dotenv.load();
//        String valor = dotenv.get("VALOR");
//
//        // Ler taxa selic
//        List<TaxaSelic> taxas = lerTaxaSelic(TAXA_SELIC);
//        Map<Integer, Double> mediaPorAno = calcularMediaTaxaPorAno(taxas);
//
//        // Ler Excel de gastos
//        List<Gasto> gastos = lerArquivoExcel(GASTOS);
//
//        // Agrupar gastos por vigência
//        Map<String, Double> gastosTotais = calcularGastosTotais(gastos);
//
//        // Comparar gastos mês a mês
//        List<GastoComparado> comparativo = compararGastos(gastosTotais);
//
//        // Aplicar descontos acumulados
//        aplicarDescontos(comparativo, DESCONTOS);
//
//        // Exibir resultados
//        comparativo.forEach(System.out::println);
//    }
//
//    // =========================
//    // Ler CSV da taxa selic
//    // =========================
//    public static List<TaxaSelic> lerTaxaSelic(String caminho) {
//        List<TaxaSelic> lista = new ArrayList<>();
//        try (CSVReader reader = new CSVReader(new FileReader(caminho))) {
//            String[] linha;
//            reader.readNext(); // pula cabeçalho
//            while ((linha = reader.readNext()) != null) {
//                String dataStr = linha[0];
//                String taxaStr = linha[1].replace(",", ".");
//                LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//                double taxa = Double.parseDouble(taxaStr);
//                lista.add(new TaxaSelic(data, taxa));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return lista;
//    }
//
//    public static Map<Integer, Double> calcularMediaTaxaPorAno(List<TaxaSelic> lista) {
//        return lista.stream()
//                .collect(Collectors.groupingBy(
//                        t -> t.data().getYear(),
//                        Collectors.averagingDouble(TaxaSelic::taxa)
//                ));
//    }
//
//    // =========================
//    // Ler Excel
//    // =========================
//    public static List<Gasto> lerArquivoExcel(String caminho) {
//        List<Gasto> gastos = new ArrayList<>();
//        try (FileInputStream fis = new FileInputStream(caminho);
//             Workbook workbook = new XSSFWorkbook(fis)) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rows = sheet.iterator();
//
//            rows.next(); // pula cabeçalho
//            while (rows.hasNext()) {
//                Row row = rows.next();
//                String cartao = row.getCell(0).getStringCellValue();
//                String dono = row.getCell(1).getStringCellValue();
//                LocalDate vigencia = row.getCell(2).getLocalDateTimeCellValue().toLocalDate();
//                double valor = row.getCell(3).getNumericCellValue();
//
//                gastos.add(new Gasto(cartao, dono, vigencia, valor));
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return gastos;
//    }
//
//    // =========================
//    // Calcular gastos por mês
//    // =========================
//    public static Map<String, Double> calcularGastosTotais(List<Gasto> gastos) {
//        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
//        return gastos.stream()
//                .collect(Collectors.groupingBy(
//                        g -> g.vigencia().format(fmt),
//                        Collectors.summingDouble(Gasto::valor)
//                ));
//    }
//
//    // =========================
//    // Comparativo mês a mês
//    // =========================
//    public static List<GastoComparado> compararGastos(Map<String, Double> gastos) {
//        List<GastoComparado> lista = new ArrayList<>();
//        List<String> meses = new ArrayList<>(gastos.keySet());
//        Collections.sort(meses);
//
//        Double anterior = null;
//        for (String mes : meses) {
//            double valor = gastos.get(mes);
//            double diff = 0, perc = 0;
//            String status = "—";
//
//            if (anterior != null) {
//                diff = valor - anterior;
//                perc = (valor / anterior - 1) * 100;
//                if (diff > 0) status = "aumento";
//                else if (diff < 0) status = "queda";
//                else status = "estável";
//            }
//
//            System.out.printf(
//                    "No mês %s, gasto foi de R$ %.2f (%s de R$ %.2f, %.2f%% em relação ao mês anterior)%n",
//                    mes, valor, status, diff, perc
//            );
//
//            lista.add(new GastoComparado(mes, valor, diff, perc));
//            anterior = valor;
//        }
//
//        return lista;
//    }
//
//    // =========================
//    // Aplicar descontos acumulados
//    // =========================
//    public static void aplicarDescontos(List<GastoComparado> lista, Map<String, Double> descontos) {
//        for (GastoComparado g : lista) {
//            double descontoTotal = descontos.entrySet().stream()
//                    .filter(e -> e.getKey().compareTo(g.mes()) <= 0)
//                    .mapToDouble(Map.Entry::getValue)
//                    .sum();
//            g.setDescontosAcumulados(descontoTotal);
//            g.setValorComDesconto(g.valor() - descontoTotal);
//        }
//    }
//}
//
//// =========================
//// Classes auxiliares (records)
//// =========================
//record TaxaSelic(LocalDate data, double taxa) {}
//record Gasto(String cartao, String dono, LocalDate vigencia, double valor) {}
//class GastoComparado {
//    private String mes;
//    private double valor;
//    private double diferenca;
//    private double percentual;
//    private double descontosAcumulados;
//    private double valorComDesconto;
//
//    public GastoComparado(String mes, double valor, double diferenca, double percentual) {
//        this.mes = mes;
//        this.valor = valor;
//        this.diferenca = diferenca;
//        this.percentual = percentual;
//    }
//
//    public String mes() { return mes; }
//    public double valor() { return valor; }
//    public void setDescontosAcumulados(double v) { this.descontosAcumulados = v; }
//    public void setValorComDesconto(double v) { this.valorComDesconto = v; }
//
//    @Override
//    public String toString() {
//        return String.format("%s | Valor: R$ %.2f | Desc: R$ %.2f | Líquido: R$ %.2f",
//                mes, valor, descontosAcumulados, valorComDesconto);
//    }
//}
