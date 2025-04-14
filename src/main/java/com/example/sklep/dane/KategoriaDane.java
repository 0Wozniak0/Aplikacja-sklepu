package com.example.sklep.dane;

import com.example.sklep.model.Kategoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KategoriaDane extends JpaRepository<Kategoria, Long> {
}
