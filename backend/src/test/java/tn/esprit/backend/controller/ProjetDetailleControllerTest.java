package tn.esprit.backend.controller;

import tn.esprit.backend.entity.ProjetDetaille;
import tn.esprit.backend.service.IProjetDetailleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetDetailleControllerTest {

    @Mock
    private IProjetDetailleService projetDetailleService;

    @InjectMocks
    private ProjetDetailleController projetDetailleController;

    @Test
    void shouldAddProjetDetaille() {
        ProjetDetaille projetDetaille = ProjetDetaille.builder()
                .description("Detailed specs")
                .technologie("Java 17")
                .coutProvisoire(5000.0)
                .dateDebut(LocalDate.now())
                .build();

        when(projetDetailleService.addProjetDetaille(any(ProjetDetaille.class))).thenReturn(projetDetaille);

        ProjetDetaille result = projetDetailleController.addProjetDetaille(projetDetaille);

        assertThat(result).isNotNull();
        assertThat(result.getTechnologie()).isEqualTo("Java 17");
        verify(projetDetailleService, times(1)).addProjetDetaille(any(ProjetDetaille.class));
    }

    @Test
    void shouldUpdateProjetDetaille() {
        ProjetDetaille projetDetaille = ProjetDetaille.builder()
                .id(1L)
                .description("Updated specs")
                .technologie("Spring Boot")
                .coutProvisoire(8000.0)
                .dateDebut(LocalDate.now())
                .build();

        when(projetDetailleService.updateProjetDetaille(any(ProjetDetaille.class))).thenReturn(projetDetaille);

        ProjetDetaille result = projetDetailleController.updateProjetDetaille(projetDetaille);

        assertThat(result).isNotNull();
        assertThat(result.getTechnologie()).isEqualTo("Spring Boot");
        verify(projetDetailleService, times(1)).updateProjetDetaille(any(ProjetDetaille.class));
    }

    @Test
    void shouldDeleteProjetDetaille() {
        Long id = 1L;

        doNothing().when(projetDetailleService).deleteProjetDetaille(id);

        projetDetailleController.deleteProjetDetaille(id);

        verify(projetDetailleService, times(1)).deleteProjetDetaille(id);
    }

    @Test
    void shouldGetProjetDetailleById() {
        ProjetDetaille projetDetaille = ProjetDetaille.builder()
                .id(1L)
                .description("Test specs")
                .technologie("React")
                .build();

        when(projetDetailleService.getProjetDetailleById(1L)).thenReturn(projetDetaille);

        ProjetDetaille result = projetDetailleController.getProjetDetailleById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getTechnologie()).isEqualTo("React");
        verify(projetDetailleService, times(1)).getProjetDetailleById(1L);
    }

    @Test
    void shouldReturnNullWhenProjetDetailleNotFound() {
        when(projetDetailleService.getProjetDetailleById(999L)).thenReturn(null);

        ProjetDetaille result = projetDetailleController.getProjetDetailleById(999L);

        assertThat(result).isNull();
        verify(projetDetailleService, times(1)).getProjetDetailleById(999L);
    }

    @Test
    void shouldGetAllProjetsDetailles() {
        ProjetDetaille pd1 = ProjetDetaille.builder().id(1L).description("Specs A").build();
        ProjetDetaille pd2 = ProjetDetaille.builder().id(2L).description("Specs B").build();

        when(projetDetailleService.getAllProjetsDetailles()).thenReturn(Arrays.asList(pd1, pd2));

        List<ProjetDetaille> result = projetDetailleController.getAllProjetsDetailles();

        assertThat(result).hasSize(2);
        verify(projetDetailleService, times(1)).getAllProjetsDetailles();
    }

    @Test
    void shouldGetProjetDetaillesByProjet() {
        ProjetDetaille pd1 = ProjetDetaille.builder().id(1L).description("Specs A").build();
        ProjetDetaille pd2 = ProjetDetaille.builder().id(2L).description("Specs B").build();

        when(projetDetailleService.getProjetDetaillesByProjet(1L)).thenReturn(Arrays.asList(pd1, pd2));

        List<ProjetDetaille> result = projetDetailleController.getProjetDetaillesByProjet(1L);

        assertThat(result).hasSize(2);
        verify(projetDetailleService, times(1)).getProjetDetaillesByProjet(1L);
    }

    @Test
    void shouldAssignProjetDetailleToProjet() {
        ProjetDetaille projetDetaille = ProjetDetaille.builder().id(1L).description("Specs").build();

        when(projetDetailleService.assignProjetDetailleToProjet(1L, 1L)).thenReturn(projetDetaille);

        ProjetDetaille result = projetDetailleController.assignProjetDetailleToProjet(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Specs");
        verify(projetDetailleService, times(1)).assignProjetDetailleToProjet(1L, 1L);
    }
}
