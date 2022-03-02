package com.healthycoderapp;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BMICalculatorTest {

    @Nested
    class isDietRecomendedTests {
        // Test using parameterized Tests
        @ParameterizedTest
        @ValueSource(doubles = {89.0, 95.0, 110.0})
        void should_ReturnTrue_When_DietRecommended(Double coderWeight) {

            //given
            double weight = coderWeight;
            double height = 1.72;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);
        }

        // Test using CsvSource
        @ParameterizedTest(name = "weight={0}, height={1}")
        @CsvSource(value = {"89.0, 1.72", "95.0, 1.75", "110.0, 1.78"})
        void should_ReturnTrue_When_DietRecommended2(Double coderWeight, Double coderHeight) {

            //given
            double weight = coderWeight;
            double height = coderHeight;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);
        }

        // Test using CsvFileSource
        @ParameterizedTest(name = "weight={0}, height={1}")
        @CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
        void should_ReturnTrue_When_DietRecommended3(Double coderWeight, Double coderHeight) {

            //given
            double weight = coderWeight;
            double height = coderHeight;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);
        }


        @Test
        void should_ReturnFalse_When_DietRecommended() {

            //given
            double weight = 59.0;
            double height = 1.92;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertFalse(recommended);
        }

        // Test Exceptions
        @Test
        void should_ThrowArithmeticException_When_HeightZero() {

            //given
            double weight = 59.0;
            double height = 0;

            //when
            Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

            //then
            assertThrows(ArithmeticException.class, executable);
        }
    }

    @Nested
    class FindCoderWithWorstBMITests {

        private String environment = "prod";

        // Test Multiple Assertions
        @Test
        void should_ReturnCoderWithWorstBMI_when_CoderListNotEmpty() {
            //given
            List<Coder> coders = new ArrayList<>();

            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.50, 90.0));
            coders.add(new Coder(1.96, 70.0));

            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertAll(
                    () -> assertEquals(1.5, coderWorstBMI.getHeight()),
                    () -> assertEquals(90, coderWorstBMI.getWeight())
            );
        }

        // Test Performance
        @Test
        void should_returnCoderWithWorstBMI1Ms_WHen_CoderListHas10000Elements() {

            // given
            // using assunmptions
            assumeTrue(this.environment.equals("prod"));
            List<Coder> coders = new ArrayList<>();

            for (int i = 0; i < 10000; i++) {
                coders.add(new Coder(1.0 + i, 10.0 + i));
            }

            // when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

            // then
            assertTimeout(Duration.ofMillis(5), executable);
        }


        // Test Null Values
        @Test
        void should_ReturnNull_when_CoderListEmpty() {
            //given
            List<Coder> coders = new ArrayList<>();

            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertNull(coderWorstBMI);
        }


    }

    @Nested
    class GetBMIScoresTests {
        // Test Array Equality
        @Test
        void should_ReturnCorrectBMIScoreArray_when_CoderListNotEmpty() {
            //given
            List<Coder> coders = new ArrayList<>();

            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.82, 64.7));
            double[] expected = {18.52, 29.59, 19.53};

            //when
            double[] bmiScores = BMICalculator.getBMIScores(coders);

            //then
            assertArrayEquals(expected, bmiScores);
        }
    }
}