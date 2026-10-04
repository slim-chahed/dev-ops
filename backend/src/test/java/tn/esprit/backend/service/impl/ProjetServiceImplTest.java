package tn.esprit.backend.service.impl;

import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.repository.ProjetRepository;
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
class ProjetServiceImplTest {

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private ProjetServiceImpl projetService;

    @Test
    void shouldAddProjet() {
        Projet projet = Projet.builder()
                .sujet("New Application")
                .build();

        when(projetRepository.save(projet)).thenReturn(projet);

        Projet result = projetService.addProjet(projet);

        assertThat(result).isNotNull();
        assertThat(result.getSujet()).isEqualTo("New Application");
        verify(projetRepository, times(1)).save(projet);
    }

    @Test
    void shouldUpdateProjet() {
        Projet projet = Projet.builder()
                .id(1L)
                .sujet("Updated Application")
                .build();

        when(projetRepository.save(projet)).thenReturn(projet);

        Projet result = projetService.updateProjet(projet);

        assertThat(result).isNotNull();
        assertThat(result.getSujet()).isEqualTo("Updated Application");
        verify(projetRepository, times(1)).save(projet);
    }

    @Test
    void shouldDeleteProjet() {
        Long id = 1L;

        doNothing().when(projetRepository).deleteById(id);

        projetService.deleteProjet(id);

        verify(projetRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldGetProjetByIdWhenExists() {
        Projet projet = Projet.builder()
                .id(1L)
                .sujet("Test Project")
                .build();

        when(projetRepository.findById(1L)).thenReturn(Optional.of(projet));

        Projet result = projetService.getProjetById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getSujet()).isEqualTo("Test Project");
        verify(projetRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnNullWhenProjetDoesNotExist() {
        when(projetRepository.findById(999L)).thenReturn(Optional.empty());

        Projet result = projetService.getProjetById(999L);

        assertThat(result).isNull();
        verify(projetRepository, times(1)).findById(999L);
    }

    @Test
    void shouldGetAllProjets() {
        Projet p1 = Projet.builder().id(1L).sujet("Project A").build();
        Projet p2 = Projet.builder().id(2L).sujet("Project B").build();

        when(projetRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Projet> result = projetService.getAllProjets();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Projet::getSujet).containsExactly("Project A", "Project B");
        verify(projetRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoProjets() {
        when(projetRepository.findAll()).thenReturn(Arrays.asList());

        List<Projet> result = projetService.getAllProjets();

        assertThat(result).isEmpty();
        verify(projetRepository, times(1)).findAll();
    }
}
