package com.task.mathsequences.service.impl;

import com.task.mathsequences.dto.response.FactorialResponseDto;
import com.task.mathsequences.entity.Factorial;
import com.task.mathsequences.entity.User;
import com.task.mathsequences.repository.FactorialRepository;
import com.task.mathsequences.repository.UserRepository;
import com.task.mathsequences.service.FactorialService;
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
 * Реализация сервиса для работы с факториальными последовательностями.
 * <p>
 * Предоставляет методы для генерации факториалов, сохранения истории запросов,
 * получения истории и её очистки для конкретного пользователя.
 *
 * @author Levshunov
 * @version 1.0
 * @since 2025-07-12
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FactorialServiceImpl implements FactorialService {

    private final FactorialRepository factorialRepository;
    private final UserRepository userRepository;

    /**
     * Генерирует список факториальных чисел от 1 до указанного числа.
     *
     * @param number Число, до которого нужно сгенерировать факториалы.
     * @return Список факториалов в порядке возрастания.
     */
    @Override
    public List<Long> generateFactorialNumbers(int number) {
        List<Long> factorials = new ArrayList<>();
        long factorial = 1;

        for (int i = 1; i <= number; i++) {
            factorial *= i;
            factorials.add(factorial);
        }

        log.debug("Generated Factorial sequence with {} numbers", number);
        return factorials;
    }

    /**
     * Генерирует факториальную последовательность, сохраняет запрос в базе данных и возвращает результат.
     *
     * @param username Имя пользователя, для которого выполняется запрос.
     * @param number   Число, до которого нужно сгенерировать факториалы.
     * @return DTO с результатами запроса.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
    @Override
    @Transactional
    public FactorialResponseDto generateAndSave(String username, int number) {
        log.info("Starting Factorial generation for user: {}, number: {}", username, number);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Long> numbers = generateFactorialNumbers(number);
        LocalDateTime now = LocalDateTime.now();

        Factorial request = Factorial.builder()
                .number(number)
                .factorialNumbers(numbers.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", ")))
                .requestTime(now)
                .user(user)
                .enabled(true)
                .build();

        Factorial savedRequest = factorialRepository.save(request);
        log.info("Successfully saved Factorial request with ID: {}", savedRequest.getId());

        return new FactorialResponseDto(
                number,
                now,
                numbers
        );
    }

    /**
     * Получает историю запросов факториалов для указанного пользователя.
     *
     * @param username Имя пользователя.
     * @return Список DTO с историей запросов.
     */
    @Override
    @Transactional
    public List<FactorialResponseDto> getHistory(String username) {
        log.info("Fetching Factorial history for user: {}", username);

        List<FactorialResponseDto> history = factorialRepository
                .findByUserUsernameAndEnabledTrueOrderByRequestTimeDesc(username)
                .stream()
                .map(req -> {
                    try {
                        String nums = req.getFactorialNumbers()
                                .replace("[", "")
                                .replace("]", "");
                        return new FactorialResponseDto(
                                req.getNumber(),
                                req.getRequestTime(),
                                Arrays.stream(nums.split(", "))
                                        .map(String::trim)
                                        .filter(s -> !s.isEmpty())
                                        .map(Long::parseLong)
                                        .toList()
                        );
                    } catch (Exception e) {
                        log.warn("Error parsing Factorial numbers for request ID: {}. Error: {}",
                                req.getId(), e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        log.info("Found {} Factorial requests for user: {}", history.size(), username);
        return history;
    }

    /**
     * Очищает историю запросов факториалов для указанного пользователя,
     * помечая все записи как неактивные (enabled = false).
     *
     * @param username Имя пользователя.
     */
    @Override
    @Transactional
    public void clearHistory(String username) {
        log.info("Clearing Factorial history for user: {}", username);

        List<Factorial> records = factorialRepository.findByUserUsernameAndEnabledTrueOrderByRequestTimeDesc(username);

        for (Factorial record : records) {
            record.setEnabled(false);
        }

        factorialRepository.saveAll(records);
    }

}