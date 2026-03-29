package ru.mentee.power.crm.spring.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FieldInjectionProblemTest {

    @Test
    void fieldInjectionCausesNullPointerWithoutSpring() {
        DemoController controller = new DemoController(null);

        assertThat(controller)
                .extracting("constructorService","fieldRepository", "setterService")
                .containsOnlyNulls();
    }
}
