package tn.esprit.backend.controller;

import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.service.IProjetService;
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
class ProjetControllerTest {

    @Mock
    private IProjetService projetService;

    @InjectMocks
    private ProjetController projetController;

    @Test
    void shouldAddProjet() {
        Projet projet = Projet.builder()
                .sujet("New Application")
                .build();

        when(projetService.addProjet(any(Projet.class))).thenReturn(projet);

        Projet result = projetController.addProjet(projet);

        assertThat(result).isNotNull();
        assertThat(result.getSujet()).isEqualTo("New Application");
        verify(projetService, times(1)).addProjet(any(Projet.class));
    }

    @Test
    void shouldUpdateProjet() {
        Projet projet = Projet.builder()
                .id(1L)
                .sujet("Updated Application")
                .build();

        when(projetService.updateProjet(any(Projet.class))).thenReturn(projet);

        Projet result = projetController.updateProjet(projet);

        assertThat(result).isNotNull();
        assertThat(result.getSujet()).isEqualTo("Updated Application");
        verify(projetService, times(1)).updateProjet(any(Projet.class));
    }

    @Test
    void shouldDeleteProjet() {
        Long id = 1L;

        doNothing().when(projetService).deleteProjet(id);

        projetController.deleteProjet(id);

        verify(projetService, times(1)).deleteProjet(id);
    }

    @Test
    void shouldGetProjetById() {
        Projet projet = Projet.builder()
                .id(1L)
                .sujet("Test Project")
                .build();

        when(projetService.getProjetById(1L)).thenReturn(projet);

        Projet result = projetController.getProjetById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getSujet()).isEqualTo("Test Project");
        verify(projetService, times(1)).getProjetById(1L);
    }

    @Test
    void shouldReturnNullWhenProjetNotFound() {
        when(projetService.getProjetById(999L)).thenReturn(null);

        Projet result = projetController.getProjetById(999L);

        assertThat(result).isNull();
        verify(projetService, times(1)).getProjetById(999L);
    }

    @Test
    void shouldGetAllProjets() {
        Projet p1 = Projet.builder().id(1L).sujet("Project A").build();
        Projet p2 = Projet.builder().id(2L).sujet("Project B").build();

        when(projetService.getAllProjets()).thenReturn(Arrays.asList(p1, p2));

        List<Projet> result = projetController.getAllProjets();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Projet::getSujet).containsExactly("Project A", "Project B");
        verify(projetService, times(1)).getAllProjets();
    }
}
