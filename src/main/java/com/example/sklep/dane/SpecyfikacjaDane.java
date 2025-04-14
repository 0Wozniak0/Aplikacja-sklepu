package com.example.sklep.dane;

import com.example.sklep.model.Specyfikacja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecyfikacjaDane extends JpaRepository<Specyfikacja, Long> {
    List<Specyfikacja> findByPrzedmiotId(Long id);
    // Możesz dodać inne metody, jeśli potrzebujesz
}
