package com.mensagia.eme.service;

import com.mensagia.eme.entity.MedicalQuestion;
import com.mensagia.eme.repository.MedicalQuestionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class MedicalQuestionService {

    private static final Set<String> VALID_ANSWERS = Set.of("A", "B", "C", "D", "E");
    private static final Set<String> VALID_STATUSES = Set.of("ACTIVE", "INACTIVE");

    private final MedicalQuestionRepository repository;

    public MedicalQuestionService(MedicalQuestionRepository repository) {
        this.repository = repository;
    }

    public List<MedicalQuestion> findAll() {
        return repository.listAll();
    }

    public Optional<MedicalQuestion> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Transactional
    public MedicalQuestion create(MedicalQuestion question) {
        validate(question);
        question.id = null;
        question.applyDefaultsForInsert();
        repository.persist(question);
        return question;
    }

    @Transactional
    public Optional<MedicalQuestion> update(Long id, MedicalQuestion input) {
        validate(input);

        Optional<MedicalQuestion> existingOptional = repository.findByIdOptional(id);
        if (existingOptional.isEmpty()) {
            return Optional.empty();
        }

        MedicalQuestion existing = existingOptional.get();
        existing.questionNumber = input.questionNumber;
        existing.examYear = input.examYear;
        existing.questionText = input.questionText;
        existing.alternatives = input.alternatives;
        existing.correctAnswer = input.correctAnswer;
        existing.explanation = input.explanation;

        if (input.status != null && !input.status.isBlank()) {
            existing.status = input.status;
        }
        existing.markUpdated();
        return Optional.of(existing);
    }

    @Transactional
    public boolean delete(Long id) {
        return repository.deleteById(id);
    }

    private void validate(MedicalQuestion question) {
        if (question == null) {
            throw new IllegalArgumentException("El cuerpo JSON es obligatorio.");
        }
        if (question.questionNumber == null || question.questionNumber <= 0) {
            throw new IllegalArgumentException("questionNumber debe ser mayor que cero.");
        }
        if (isBlank(question.examYear)) {
            throw new IllegalArgumentException("examYear es obligatorio.");
        }
        if (isBlank(question.questionText)) {
            throw new IllegalArgumentException("questionText es obligatorio.");
        }
        if (question.alternatives == null || question.alternatives.isEmpty()) {
            throw new IllegalArgumentException("alternatives debe contener al menos una alternativa.");
        }
        if (!hasValidAlternatives(question.alternatives)) {
            throw new IllegalArgumentException("Las claves de alternatives deben ser A, B, C, D o E y sus textos no pueden estar vacíos.");
        }
        if (question.correctAnswer == null ||
            !VALID_ANSWERS.contains(question.correctAnswer.trim().toUpperCase())) {
            throw new IllegalArgumentException("correctAnswer debe ser A, B, C, D o E.");
        }
        question.correctAnswer = question.correctAnswer.trim().toUpperCase();

        if (isBlank(question.explanation)) {
            throw new IllegalArgumentException("explanation es obligatorio.");
        }
        if (question.status != null && !question.status.isBlank()) {
            question.status = question.status.trim().toUpperCase();
            if (!VALID_STATUSES.contains(question.status)) {
                throw new IllegalArgumentException("status debe ser ACTIVO o INACTIVO.");
            }
        }
    }

    private boolean hasValidAlternatives(Map<String, String> alternatives) {
        return alternatives.entrySet().stream().allMatch(entry ->
            entry.getKey() != null &&
            VALID_ANSWERS.contains(entry.getKey().trim().toUpperCase()) &&
            entry.getValue() != null &&
            !entry.getValue().isBlank()
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
