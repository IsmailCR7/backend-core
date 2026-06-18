package ru.mentee.power.crm.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class ProductJpaRepositoryTest {

    @Autowired
    private ProductJpaRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Создаём тестовый продукт перед каждым тестом
        testProduct = new Product();
        testProduct.setName("Тестовый ноутбук");
        testProduct.setSku("LAPTOP-TEST-001");
        testProduct.setPrice(new BigDecimal("99999.99"));
        testProduct.setActive(true);
    }

    // ==================== ТЕСТ 1: Сохранение продукта ====================

    @Test
    void shouldSaveAndFindProductWhenValidData() {
        // Given
        Product product = new Product();
        product.setName("Консультация по архитектуре");
        product.setSku("CONSULT-ARCH-001");
        product.setPrice(new BigDecimal("50000.00"));
        product.setActive(true);

        // When
        Product saved = productRepository.save(product);

        // Then
        assertThat(saved.getId()).isNotNull();

        Optional<Product> found = productRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Консультация по архитектуре");
        assertThat(found.get().getSku()).isEqualTo("CONSULT-ARCH-001");
        assertThat(found.get().getPrice()).isEqualTo(new BigDecimal("50000.00"));
        assertThat(found.get().getActive()).isTrue();
    }

    // ==================== ТЕСТ 2: Поиск по SKU ====================

    @Test
    void shouldFindProductBySkuWhenProductExists() {
        // Given
        productRepository.save(testProduct);

        // When
        Optional<Product> found = productRepository.findBySku("LAPTOP-TEST-001");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Тестовый ноутбук");
        assertThat(found.get().getSku()).isEqualTo("LAPTOP-TEST-001");
    }

    @Test
    void shouldReturnEmptyOptionalWhenSkuNotFound() {
        // Given: нет продуктов с таким SKU

        // When
        Optional<Product> found = productRepository.findBySku("NONEXISTENT-SKU");

        // Then
        assertThat(found).isEmpty();
    }

    // ==================== ТЕСТ 3: Поиск активных продуктов ====================

    @Test
    void shouldFindActiveProductsWhenActiveTrue() {
        // Given
        Product active1 = new Product();
        active1.setName("Активный товар 1");
        active1.setSku("ACTIVE-001");
        active1.setPrice(new BigDecimal("100.00"));
        active1.setActive(true);
        productRepository.save(active1);

        Product active2 = new Product();
        active2.setName("Активный товар 2");
        active2.setSku("ACTIVE-002");
        active2.setPrice(new BigDecimal("200.00"));
        active2.setActive(true);
        productRepository.save(active2);

        Product inactive = new Product();
        inactive.setName("Неактивный товар");
        inactive.setSku("INACTIVE-001");
        inactive.setPrice(new BigDecimal("50.00"));
        inactive.setActive(false);
        productRepository.save(inactive);

        // When
        List<Product> activeProducts = productRepository.findByActiveTrue();

        // Then
        assertThat(activeProducts).hasSize(2);
        assertThat(activeProducts)
                .extracting(Product::getSku)
                .containsExactlyInAnyOrder("ACTIVE-001", "ACTIVE-002");
        assertThat(activeProducts)
                .noneMatch(product -> !product.getActive());
    }

    // ==================== ТЕСТ 4: Уникальность SKU ====================

    @Test
    void shouldThrowExceptionWhenSkuNotUnique() {
        // Given: продукт с SKU уже сохранён
        productRepository.save(testProduct);
        productRepository.flush(); // Принудительно отправляем в БД

        // Создаём дубликат с тем же SKU
        Product duplicate = new Product();
        duplicate.setName("Дубликат товара");
        duplicate.setSku("LAPTOP-TEST-001"); // Тот же SKU
        duplicate.setPrice(new BigDecimal("150.00"));
        duplicate.setActive(true);

        // When & Then
        assertThatThrownBy(() -> {
            productRepository.save(duplicate);
            productRepository.flush(); // Принудительная отправка, чтобы триггернуть constraint
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    // ==================== ТЕСТ 5: Обновление продукта ====================

    @Test
    void shouldUpdateProductWhenProductExists() {
        // Given
        Product saved = productRepository.save(testProduct);

        // When
        saved.setName("Обновлённое название");
        saved.setPrice(new BigDecimal("150000.00"));
        Product updated = productRepository.save(saved);

        // Then
        Optional<Product> found = productRepository.findById(updated.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Обновлённое название");
        assertThat(found.get().getPrice()).isEqualTo(new BigDecimal("150000.00"));
        assertThat(found.get().getSku()).isEqualTo("LAPTOP-TEST-001"); // SKU не изменился
    }

    // ==================== ТЕСТ 6: Удаление продукта ====================

    @Test
    void shouldDeleteProductWhenProductExists() {
        // Given
        Product saved = productRepository.save(testProduct);
        UUID id = saved.getId();

        // When
        productRepository.deleteById(id);

        // Then
        Optional<Product> found = productRepository.findById(id);
        assertThat(found).isEmpty();
    }

    // ==================== ТЕСТ 7: Поиск всех продуктов ====================

    @Test
    void shouldFindAllProductsWhenMultipleProductsExist() {
        // Given
        Product product1 = new Product();
        product1.setName("Товар 1");
        product1.setSku("ALL-001");
        product1.setPrice(new BigDecimal("100.00"));
        product1.setActive(true);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Товар 2");
        product2.setSku("ALL-002");
        product2.setPrice(new BigDecimal("200.00"));
        product2.setActive(false);
        productRepository.save(product2);

        // When
        List<Product> allProducts = productRepository.findAll();

        // Then
        assertThat(allProducts).hasSize(2);
        assertThat(allProducts)
                .extracting(Product::getSku)
                .containsExactlyInAnyOrder("ALL-001", "ALL-002");
    }

    // ==================== ТЕСТ 8: Активный по умолчанию ====================

    @Test
    void shouldSetActiveTrueWhenNotSpecified() {
        // Given: создаём продукт без указания active
        Product product = new Product();
        product.setName("Товар без указания активности");
        product.setSku("DEFAULT-ACTIVE-001");
        product.setPrice(new BigDecimal("1000.00"));
        // НЕ устанавливаем active

        // When
        Product saved = productRepository.save(product);

        // Then
        assertThat(saved.getActive()).isTrue(); // Должно быть true по умолчанию
    }

    // ==================== ТЕСТ 9: Продукт с null полями ====================

    @Test
    void shouldThrowExceptionWhenRequiredFieldIsNull() {
        // Given: продукт без имени (null)
        Product product = new Product();
        product.setName(null); // NOT NULL поле
        product.setSku("NULL-TEST-001");
        product.setPrice(new BigDecimal("100.00"));
        product.setActive(true);

        // When & Then
        assertThatThrownBy(() -> {
            productRepository.save(product);
            productRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
