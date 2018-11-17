package com.scarlatti

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test

/**
 * ______    __                         __           ____             __     __  __  _
 * ___/ _ | / /__ ___ ___ ___ ____  ___/ /______    / __/______ _____/ /__ _/ /_/ /_(_)
 * __/ __ |/ / -_|_-<(_-</ _ `/ _ \/ _  / __/ _ \  _\ \/ __/ _ `/ __/ / _ `/ __/ __/ /
 * /_/ |_/_/\__/___/___/\_,_/_//_/\_,_/_/  \___/ /___/\__/\_,_/_/ /_/\_,_/\__/\__/_/
 * Saturday, 11/17/2018
 */
class JacksonConverterTest {

    @Test
    void convertMapToPojoWithJackson() {
        Map<String, Object> map = [
                name: "phil",
                age: 1,
                shoe: [
                        size: 2
                ]
        ]

        Penguin penguin = new ObjectMapper().convertValue(map, Penguin.class)

        assert penguin.name == "phil"
        assert penguin.age == 1
        assert penguin.shoe != null
        assert penguin.shoe.size == 2
    }

    private static class Penguin {
        private String name;
        private int age;

        private Shoe shoe;

        String getName() {
            return name
        }

        void setName(String name) {
            this.name = name
        }

        int getAge() {
            return age
        }

        void setAge(int age) {
            this.age = age
        }

        Shoe getShoe() {
            return shoe
        }

        void setShoe(Shoe shoe) {
            this.shoe = shoe
        }
    }

    private static class Shoe {
        int size;

        int getSize() {
            return size
        }

        void setSize(int size) {
            this.size = size
        }
    }
}
