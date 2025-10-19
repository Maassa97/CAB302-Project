package com.cab302.javafxreadingdemo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Boundary tests for GradeUtils.gpaFromPercent */
class GradeUtilsTest {

    @Test
    void gpaBoundaries_lowerEdgesInclusive() {
        assertEquals(1, GradeUtils.gpaFromPercent(0));
        assertEquals(2, GradeUtils.gpaFromPercent(20));
        assertEquals(3, GradeUtils.gpaFromPercent(45));
        assertEquals(4, GradeUtils.gpaFromPercent(50));
        assertEquals(5, GradeUtils.gpaFromPercent(65));
        assertEquals(6, GradeUtils.gpaFromPercent(75));
        assertEquals(7, GradeUtils.gpaFromPercent(85));
    }

    @Test
    void gpaBoundaries_justBelowThresholds() {
        assertEquals(1, GradeUtils.gpaFromPercent(19.999));
        assertEquals(2, GradeUtils.gpaFromPercent(44.999));
        assertEquals(3, GradeUtils.gpaFromPercent(49.999));
        assertEquals(4, GradeUtils.gpaFromPercent(64.999));
        assertEquals(5, GradeUtils.gpaFromPercent(74.999));
        assertEquals(6, GradeUtils.gpaFromPercent(84.999));
    }

    @Test
    void midBandSamples_correctGpa() {
        assertEquals(1, GradeUtils.gpaFromPercent(10.0));
        assertEquals(2, GradeUtils.gpaFromPercent(30.0));
        assertEquals(3, GradeUtils.gpaFromPercent(47.5));
        assertEquals(4, GradeUtils.gpaFromPercent(57.5));
        assertEquals(5, GradeUtils.gpaFromPercent(70.0));
        assertEquals(6, GradeUtils.gpaFromPercent(80.0));
        assertEquals(7, GradeUtils.gpaFromPercent(95.0));
    }

    @Test
    void outOfRangeInputs_areSane() {
        assertEquals(1, GradeUtils.gpaFromPercent(-5.0));
        assertEquals(7, GradeUtils.gpaFromPercent(100.0));
        assertEquals(7, GradeUtils.gpaFromPercent(1000.0));
    }

    @Test
    void justAboveThresholds_nextBand() {
        assertEquals(2, GradeUtils.gpaFromPercent(20.001));
        assertEquals(3, GradeUtils.gpaFromPercent(45.001));
        assertEquals(4, GradeUtils.gpaFromPercent(50.001));
        assertEquals(5, GradeUtils.gpaFromPercent(65.001));
        assertEquals(6, GradeUtils.gpaFromPercent(75.001));
        assertEquals(7, GradeUtils.gpaFromPercent(85.001));
    }
}
