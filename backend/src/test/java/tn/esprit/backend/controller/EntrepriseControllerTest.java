package tn.esprit.backend.controller;

import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.service.IEntrepriseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntrepriseControllerTest {

    @Mock
    private IEntrepriseService entrepriseService;

    @InjectMocks
    private EntrepriseController entrepriseController;

    @Test
    void shouldAddEntreprise() {
        Entreprise entreprise = Entreprise.builder()
                .nom("Test Corp")
                .adresse("123 Main St")
                .build();

        when(entrepriseService.addEntreprise(any(Entreprise.class))).thenReturn(entreprise);

        Entreprise result = entrepriseController.addEntreprise(entreprise);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Test Corp");
        verify(entrepriseService, times(1)).addEntreprise(any(Entreprise.class));
    }

    @Test
    void shouldUpdateEntreprise() {
        Entreprise entreprise = Entreprise.builder()
                .id(1L)
                .nom("Updated Corp")
                .adresse("456 Oak Ave")
                .build();

        when(entrepriseService.updateEntreprise(any(Entreprise.class))).thenReturn(entreprise);

        Entreprise result = entrepriseController.updateEntreprise(entreprise);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Updated Corp");
        verify(entrepriseService, times(1)).updateEntreprise(any(Entreprise.class));
    }

    @Test
    void shouldDeleteEntreprise() {
        Long id = 1L;

        doNothing().when(entrepriseService).deleteEntreprise(id);

        entrepriseController.deleteEntreprise(id);

        verify(entrepriseService, times(1)).deleteEntreprise(id);
    }

    @Test
    void shouldGetEntrepriseById() {
        Entreprise entreprise = Entreprise.builder()
                .id(1L)
                .nom("Test Corp")
                .adresse("123 Main St")
                .build();

        when(entrepriseService.getEntrepriseById(1L)).thenReturn(entreprise);

        Entreprise result = entrepriseController.getEntrepriseById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Test Corp");
        verify(entrepriseService, times(1)).getEntrepriseById(1L);
    }

    @Test
    void shouldReturnNullWhenEntrepriseNotFound() {
        when(entrepriseService.getEntrepriseById(999L)).thenReturn(null);

        Entreprise result = entrepriseController.getEntrepriseById(999L);

        assertThat(result).isNull();
        verify(entrepriseService, times(1)).getEntrepriseById(999L);
    }

    @Test
    void shouldGetAllEntreprises() {
        Entreprise e1 = Entreprise.builder().id(1L).nom("Corp A").build();
        Entreprise e2 = Entreprise.builder().id(2L).nom("Corp B").build();

        when(entrepriseService.getAllEntreprises()).thenReturn(Arrays.asList(e1, e2));

        List<Entreprise> result = entrepriseController.getAllEntreprises();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Entreprise::getNom).containsExactly("Corp A", "Corp B");
        verify(entrepriseService, times(1)).getAllEntreprises();
    }
}
