package com.example;

import org.apache.metamodel.*;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.apache.metamodel.schema.Column;
import org.apache.metamodel.schema.ColumnType;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MetaModelExample {
    public static void main(String args[]) {
        MetaModelExample example = new MetaModelExample();

        String csvFile = ClassLoader.getSystemClassLoader().getResource("example.csv").getPath();
        String xlsxFile = ClassLoader.getSystemClassLoader().getResource("example.xlsx").getPath();
        String jsonFile = ClassLoader.getSystemClassLoader().getResource("example.json").getPath();

        example.processCsv(csvFile);
        example.processSpreadsheet(xlsxFile);
        example.processJson(jsonFile);

        // Uncomment if a MongoDB instance is available
        // example.connectToMongoDb();
    }

    public void processCsv(String filename) {
        UpdateableDataContext dataContext = DataContextFactory.createCsvDataContext(new File(filename));
        List<String> tableNames = dataContext.getDefaultSchema().getTableNames();

        System.out.println(Arrays.toString(tableNames.toArray()));

        final String table = tableNames.get(0); // [1] is "default_table" alias for single-table data stores

        DataSet dataSet = dataContext.query()
                .from(table)
                .select("project")
                .where("language").eq("Java")
                .and("completed").eq(false)
                .execute();

        for (Row r: dataSet.toRows())
            System.out.println(Arrays.toString(r.getValues()));

        System.out.println("Now updating");

        dataContext.executeUpdate(new UpdateScript() {
            public void run(UpdateCallback callback) {
                callback.update(table).value("completed", "true").where("language").eq("Java").execute();
                callback.insertInto(table)
                        .value("project", "Project4")
                        .value("language", "Java")
                        .value("completed", "false")
                        .execute();
            }
        });

        System.out.println("Rerunning the previous query");

        dataSet = dataContext.query()
                .from(table)
                .select("project")
                .where("language").eq("Java")
                .and("completed").eq(false)
                .execute();

        for (Row r: dataSet.toRows())
            System.out.println(Arrays.toString(r.getValues()));
    }

    public void processSpreadsheet(String filename) {
        DataContext dataContext = DataContextFactory.createExcelDataContext(new File(filename));

        List<String> sheetNames = dataContext.getDefaultSchema().getTableNames();
        System.out.println(Arrays.toString(sheetNames.toArray()));

        for (String sheetName: sheetNames) {
            List<Column> sheetColumns = dataContext.getDefaultSchema().getTableByName(sheetName).getColumns();

            for (Column col: sheetColumns)
                System.out.println(col.getName());

            DataSet content = dataContext.query().from(sheetName).selectAll().execute();

            for (Row r: content.toRows())
                System.out.println(Arrays.toString(r.getValues()));
        }

        DataSet joined = dataContext.query()
                .from(sheetNames.get(0))
                .innerJoin(sheetNames.get(1)).on("id", "id")
                .select("Names.surname", "Products.amount")
                .execute();

        for (Row r: joined.toRows())
            System.out.println(Arrays.toString(r.getValues()));
    }

    public void processJson(String filename) {
        DataContext dataContext = DataContextFactory.createJsonDataContext(new File(filename));

        List<String> tableNames = dataContext.getDefaultSchema().getTableNames();
        System.out.println(Arrays.toString(tableNames.toArray()));

        DataSet dataSet = dataContext.query().from(tableNames.get(0))
                .select("id")
                .where("value")
                .gte(10)
                .execute();

        for (Row r: dataSet.toRows())
            System.out.println(Arrays.toString(r.getValues()));
    }

    public void connectToMongoDb() {
        UpdateableDataContext dataContext = DataContextFactory.createMongoDbDataContext(
                "localhost", 27017, "test", null, null);

        List<String> tableNames = dataContext.getDefaultSchema().getTableNames();

        if (tableNames.isEmpty()) {
            dataContext.executeUpdate(new UpdateScript() {
                public void run(UpdateCallback callback) {
                    String table = "mytable";

                    callback.createTable(callback.getDataContext().getDefaultSchema(), table)
                            .withColumn("color").ofType(ColumnType.VARCHAR)
                            .withColumn("size").ofType(ColumnType.CHAR)
                            .execute();

                    callback.insertInto(table).value("color", "red").value("size", "L").execute();
                    callback.insertInto(table).value("color", "yellow").value("size", "S").execute();
                }
            });
        }

        tableNames = dataContext.getDefaultSchema().getTableNames();
        System.out.println(Arrays.toString(tableNames.toArray()));

        DataSet dataSet = dataContext.query().from(tableNames.get(0))
                .selectAll()
                .where("size")
                .eq("S")
                .execute();

        for (Row r: dataSet.toRows())
            System.out.println(Arrays.toString(r.getValues()));
    }
}
