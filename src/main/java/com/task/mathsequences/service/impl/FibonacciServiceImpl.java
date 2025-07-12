package com.task.mathsequences.service.impl;

import com.task.mathsequences.dto.response.FibonacciResponseDto;
import com.task.mathsequences.entity.Fibonacci;
import com.task.mathsequences.entity.User;
import com.task.mathsequences.repository.FibonacciRepository;
import com.task.mathsequences.repository.UserRepository;
import com.task.mathsequences.service.FibonacciService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с последовательностью Фибоначчи.
 * <p>
 * Предоставляет методы для генерации чисел Фибоначчи, сохранения истории запросов,
 * получения истории и её очистки для конкретного пользователя.
 *
 * @author Levshunov
 * @version 1.0
 * @since 2025-07-12
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FibonacciServiceImpl implements FibonacciService {

    private final FibonacciRepository fibonacciRepository;
    private final UserRepository userRepository;

    /**
     * Генерирует последовательность чисел Фибоначчи заданной длины.
     *
     * @param number Количество чисел, которое нужно сгенерировать.
     * @return Список чисел Фибоначчи в порядке возрастания.
     */
    @Override
    public List<Long> generateFibonacciNumbers(int number) {
        List<Long> fibonaccies = new ArrayList<>();
        if (number >= 1) fibonaccies.add(0L);
        if (number >= 2) fibonaccies.add(1L);

        for (int i = 2; i < number; i++) {
            fibonaccies.add(fibonaccies.get(i - 1) + fibonaccies.get(i - 2));
        }

        log.debug("Generated Fibonacci sequence with {} numbers", number);
        return fibonaccies;
    }

    /**
     * Генерирует последовательность чисел Фибоначчи, сохраняет запрос в базе данных и возвращает результат.
     *
     * @param username Имя пользователя, для которого выполняется запрос.
     * @param number   Количество чисел, которое нужно сгенерировать.
     * @return DTO с результатами запроса.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
    @Override
    @Transactional
    public FibonacciResponseDto generateAndSave(String username, int number) {
        log.info("Starting Fibonacci generation for user: {}, number: {}", username, number);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Long> numbers = generateFibonacciNumbers(number);
        LocalDateTime now = LocalDateTime.now();

        Fibonacci request = Fibonacci.builder()
                .number(number)
                .fibonacciNumbers(numbers.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", ")))
                .requestTime(now)
                .user(user)
                .enabled(true)
                .build();

        Fibonacci savedRequest = fibonacciRepository.save(request);
        log.info("Successfully saved Fibonacci request with ID: {}", savedRequest.getId());

        return new FibonacciResponseDto(
                number,
                now,
                numbers
        );
    }

    /**
     * Получает историю запросов на генерацию чисел Фибоначчи для указанного пользователя.
     *
     * @param username Имя пользователя.
     * @return Список DTO с историей запросов.
     */
    @Override
    @Transactional
    public List<FibonacciResponseDto> getHistory(String username) {
        log.info("Fetching Fibonacci history for user: {}", username);

        List<FibonacciResponseDto> history = fibonacciRepository
                .findByUserUsernameAndEnabledTrueOrderByRequestTimeDesc(username)
                .stream()
                .map(req -> {
                    try {
                        String nums = req.getFibonacciNumbers()
                                .replace("[", "")
                                .replace("]", "");
                        return new FibonacciResponseDto(
                                req.getNumber(),
                                req.getRequestTime(),
                                Arrays.stream(nums.split(", "))
                                        .map(String::trim)
                                        .filter(s -> !s.isEmpty())
                                        .map(Long::parseLong)
                                        .toList()
                        );
                    } catch (Exception e) {
                        log.warn("Error parsing Fibonacci numbers for request ID: {}. Error: {}",
                                req.getId(), e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        log.info("Found {} Fibonacci requests for user: {}", history.size(), username);
        return history;
    }

    /**
     * Очищает историю запросов на генерацию чисел Фибоначчи для указанного пользователя,
     * помечая все записи как неактивные (enabled = false).
     *
     * @param username Имя пользователя.
     */
    @Override
    @Transactional
    public void clearHistory(String username) {
        log.info("Clearing Fibonacci history for user: {}", username);

        List<Fibonacci> records = fibonacciRepository.findByUserUsernameAndEnabledTrueOrderByRequestTimeDesc(username);

        for (Fibonacci record : records) {
            record.setEnabled(false);
        }

        fibonacciRepository.saveAll(records);
    }

}