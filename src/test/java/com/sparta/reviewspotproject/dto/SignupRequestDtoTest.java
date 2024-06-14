package com.sparta.reviewspotproject.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("잘못된 사용자 ID")
    void testInvalidUserId() {
        // Given
        SignupRequestDto dto = new SignupRequestDto();
        dto.setUserId("asdf");
        dto.setPassword("Asdfg12345!");
        dto.setUserName("박강현");
        dto.setEmail("tset@gmail.com");

        // When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        ConstraintViolation<SignupRequestDto> violation = violations.iterator().next();
        assertEquals("사용자 이름은 대소문자 포함 영문자와 숫자로 이루어진 10자에서 20자 사이여야 합니다.", violation.getMessage());
    }

    @Test
    @DisplayName("유효한 SignupRequestDto")
    void testValidSignupRequestDto() {
        // Given
        SignupRequestDto dto = new SignupRequestDto();
        dto.setUserId("asdfg12345");
        dto.setPassword("Asdfg12345!");
        dto.setUserName("박강현");
        dto.setEmail("tset@gmail.com");

        // When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(dto);

        // Then
        assertTrue(violations.isEmpty());
    }

}