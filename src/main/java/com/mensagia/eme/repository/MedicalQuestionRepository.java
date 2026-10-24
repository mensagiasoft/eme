package com.mensagia.eme.repository;

import com.mensagia.eme.entity.MedicalQuestion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MedicalQuestionRepository implements PanacheRepository<MedicalQuestion> {
}
