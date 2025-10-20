public class ExpensesExcelToDatabase {

    public static void main(String[] args) {
        String excelFilePath = "/Users/matheuscantarutti/Desktop/Personal/Finance/calc_financeira/dados/expenses_java_version.xlsx";

        // Definir o tipo de banco por argumento ou variável
        String databaseType = "mongodb"; // ou "postgres", etc.

        // Criar repositório com base no tipo
        ExpensesDataRepository repository;

        switch (databaseType.toLowerCase()) {
            case "mongodb" -> repository = new MongoExpensesRepository(
                    "mongodb://localhost:27017",
                    "expensive_adivisor",
                    "expenses"
            );
            case "postgres" -> repository = new PostgresExpensesRepository(
                "jdbc:postgresql://localhost:5432/expensive_adivisor",
                "usuario",
                "senha"
            );
            default -> throw new IllegalArgumentException("Tipo de banco desconhecido: " + databaseType);
        }

        // Lê Excel
        List<Map<String, Object>> excelData = readExcelData(excelFilePath);
        repository.save(excelData);
    }

    private static List<Map<String, Object>> readExcelData(String filePath) {
        List<Map<String, Object>> data = new ArrayList<>();

        try (
                FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)
        ) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (!rowIterator.hasNext()) {
                System.out.println("⚠️ Planilha vazia!");
                return data;
            }

            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, Object> rowData = new HashMap<>();

                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Object value = switch (cell.getCellType()) {
                        case STRING -> cell.getStringCellValue();
                        case NUMERIC -> cell.getNumericCellValue();
                        case BOOLEAN -> cell.getBooleanCellValue();
                        case BLANK -> null;
                        default -> cell.toString();
                    };
                    rowData.put(headers.get(i), value);
                }

                data.add(rowData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
