package tn.esprit.backend.service.impl;

import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.repository.EntrepriseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntrepriseServiceImplTest {

    @Mock
    private EntrepriseRepository entrepriseRepository;

    @InjectMocks
    private EntrepriseServiceImpl entrepriseService;

    @Test
    void shouldAddEntreprise() {
        Entreprise entreprise = Entreprise.builder()
                .nom("Test Corp")
                .adresse("123 Main St")
                .build();

        when(entrepriseRepository.save(entreprise)).thenReturn(entreprise);

        Entreprise result = entrepriseService.addEntreprise(entreprise);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Test Corp");
        verify(entrepriseRepository, times(1)).save(entreprise);
    }

    @Test
    void shouldUpdateEntreprise() {
        Entreprise entreprise = Entreprise.builder()
                .id(1L)
                .nom("Updated Corp")
                .adresse("456 Oak Ave")
                .build();

        when(entrepriseRepository.save(entreprise)).thenReturn(entreprise);

        Entreprise result = entrepriseService.updateEntreprise(entreprise);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Updated Corp");
        verify(entrepriseRepository, times(1)).save(entreprise);
    }

    @Test
    void shouldDeleteEntreprise() {
        Long id = 1L;

        doNothing().when(entrepriseRepository).deleteById(id);

        entrepriseService.deleteEntreprise(id);

        verify(entrepriseRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldGetEntrepriseByIdWhenExists() {
        Entreprise entreprise = Entreprise.builder()
                .id(1L)
                .nom("Test Corp")
                .build();

        when(entrepriseRepository.findById(1L)).thenReturn(Optional.of(entreprise));

        Entreprise result = entrepriseService.getEntrepriseById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Test Corp");
        verify(entrepriseRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnNullWhenEntrepriseDoesNotExist() {
        when(entrepriseRepository.findById(999L)).thenReturn(Optional.empty());

        Entreprise result = entrepriseService.getEntrepriseById(999L);

        assertThat(result).isNull();
        verify(entrepriseRepository, times(1)).findById(999L);
    }

    @Test
    void shouldGetAllEntreprises() {
        Entreprise e1 = Entreprise.builder().id(1L).nom("Corp A").build();
        Entreprise e2 = Entreprise.builder().id(2L).nom("Corp B").build();

        when(entrepriseRepository.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<Entreprise> result = entrepriseService.getAllEntreprises();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Entreprise::getNom).containsExactly("Corp A", "Corp B");
        verify(entrepriseRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoEntreprises() {
        when(entrepriseRepository.findAll()).thenReturn(Arrays.asList());

        List<Entreprise> result = entrepriseService.getAllEntreprises();

        assertThat(result).isEmpty();
        verify(entrepriseRepository, times(1)).findAll();
    }
}
