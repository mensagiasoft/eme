package com.mensagia.eme.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(
    name = "medical_questions",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_medical_question_exam",
        columnNames = {"question_number", "exam_year"}
    )
)
public class MedicalQuestion extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "question_number", nullable = false)
    public Integer questionNumber;

    @Column(name = "exam_year", nullable = false, length = 10)
    public String examYear;

    @Column(name = "question_text", nullable = false, columnDefinition = "text")
    public String questionText;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "alternatives", nullable = false, columnDefinition = "jsonb")
    public Map<String, String> alternatives = new LinkedHashMap<>();

    @Column(name = "correct_answer", nullable = false, length = 1)
    public String correctAnswer;

    @Column(name = "explanation", nullable = false, columnDefinition = "text")
    public String explanation;

    @Column(name = "status", nullable = false, length = 20)
    public String status = "ACTIVE";

    @Column(name = "created_at", nullable = false)
    public OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    public OffsetDateTime updatedAt;

    protected MedicalQuestion() {
        // Required by JPA.
    }

    public void applyDefaultsForInsert() {
        OffsetDateTime now = OffsetDateTime.now();
        if (status == null || status.isBlank()) {
            status = "ACTIVE";
        }
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    public void markUpdated() {
        updatedAt = OffsetDateTime.now();
    }
}
