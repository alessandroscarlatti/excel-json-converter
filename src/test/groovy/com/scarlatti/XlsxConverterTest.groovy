package com.scarlatti

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test

import java.nio.file.Paths

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 11/17/2018
 */
class XlsxConverterTest {

    @Test
    void convertTest1Xlsx() {
        XlsxWorkbook workbook = new XlsxConverter().convert(Paths.get("src/test/resources/test1.xlsx"))

        assert workbook != null

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(workbook)
        println json

        assert workbook == [
                "Empty Sheet": [],
                "Books"   : [
                        [
                                "Length": "3.0",
                                "Title" : "cat in the hat",
                                "Cost"  : "4.56"
                        ], [
                                "Length": "45.0",
                                "Title" : "green eggs and ham",
                                "Cost"  : "56.773"
                        ]
                ],
                "Penguins": [
                        [
                                "Name": "Phil",
                                "Age" : "2.0",
                        ],
                        [
                                "Name": "Annie",
                                "Age" : "3.0",
                        ],
                        [
                                "Name": "Charlotte",
                                "Age" : "4.0",
                        ],
                        [
                                "Name": "Quater",
                                "Age" : "55.0",
                        ]
                ]
        ]
    }

    @Test
    void convertTest2Xlsx() {
        XlsxWorkbook workbook = new XlsxConverter().convert(Paths.get("src/test/resources/test2.xlsx"))

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(workbook)
        println json

        assert workbook == [
                "Penguins": [
                        [
                                "Name": "Phil",
                                "Age" : "2.0",
                        ],
                        [
                                "Name": "Charlotte",
                                "Age" : "",
                        ],
                        [
                                "Name": "",
                                "Age" : "55.0",
                        ]
                ]
        ]
    }

    @Test
    void convertTest1XlsxWithJackson() {
        XlsxWorkbook workbook = new XlsxConverter().convert(Paths.get("src/test/resources/test1.xlsx"))

        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(workbook)
        println json

        Test1 test1 = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).convertValue(workbook, Test1)

        assert test1.penguins != null
        assert test1.penguins[0] != null
        assert test1.penguins[0].name == "Phil"
    }

    private static class Test1 {
        @JsonProperty("Penguins")
        List<Penguin> penguins

        @JsonProperty("Stuff")
        List<Book> stuff
    }

    private static class Penguin {
        @JsonProperty("Name")
        String name

        @JsonProperty("Age")
        String age
    }

    private static class Book {
        @JsonProperty("Title")
        String title

        @JsonProperty("Length")
        String length

        @JsonProperty("Cost")
        String cost
    }
}
