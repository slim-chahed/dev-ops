package tn.esprit.backend.service.impl;

import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.entity.ProjetDetaille;
import tn.esprit.backend.repository.ProjetDetailleRepository;
import tn.esprit.backend.repository.ProjetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjetDetailleServiceImplTest {

    @Mock
    private ProjetDetailleRepository projetDetailleRepository;

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private ProjetDetailleServiceImpl projetDetailleService;

    @Test
    void shouldAddProjetDetaille() {
        ProjetDetaille projetDetaille = ProjetDetaille.builder()
                .description("Detailed specs")
                .technologie("Java 17")
                .coutProvisoire(5000.0)
                .dateDebut(LocalDate.now())
                .build();

        when(projetDetailleRepository.save(projetDetaille)).thenReturn(projetDetaille);

        ProjetDetaille result = projetDetailleService.addProjetDetaille(projetDetaille);

        assertThat(result).isNotNull();
        assertThat(result.getTechnologie()).isEqualTo("Java 17");
        verify(projetDetailleRepository, times(1)).save(projetDetaille);
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

        when(projetDetailleRepository.save(projetDetaille)).thenReturn(projetDetaille);

        ProjetDetaille result = projetDetailleService.updateProjetDetaille(projetDetaille);

        assertThat(result).isNotNull();
        assertThat(result.getTechnologie()).isEqualTo("Spring Boot");
        verify(projetDetailleRepository, times(1)).save(projetDetaille);
    }

    @Test
    void shouldDeleteProjetDetaille() {
        Long id = 1L;

        doNothing().when(projetDetailleRepository).deleteById(id);

        projetDetailleService.deleteProjetDetaille(id);

        verify(projetDetailleRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldGetProjetDetailleByIdWhenExists() {
        ProjetDetaille projetDetaille = ProjetDetaille.builder()
                .id(1L)
                .description("Test specs")
                .technologie("React")
                .build();

        when(projetDetailleRepository.findById(1L)).thenReturn(Optional.of(projetDetaille));

        ProjetDetaille result = projetDetailleService.getProjetDetailleById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getTechnologie()).isEqualTo("React");
        verify(projetDetailleRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnNullWhenProjetDetailleDoesNotExist() {
        when(projetDetailleRepository.findById(999L)).thenReturn(Optional.empty());

        ProjetDetaille result = projetDetailleService.getProjetDetailleById(999L);

        assertThat(result).isNull();
        verify(projetDetailleRepository, times(1)).findById(999L);
    }

    @Test
    void shouldGetAllProjetsDetailles() {
        ProjetDetaille pd1 = ProjetDetaille.builder().id(1L).description("Specs A").build();
        ProjetDetaille pd2 = ProjetDetaille.builder().id(2L).description("Specs B").build();

        when(projetDetailleRepository.findAll()).thenReturn(Arrays.asList(pd1, pd2));

        List<ProjetDetaille> result = projetDetailleService.getAllProjetsDetailles();

        assertThat(result).hasSize(2);
        verify(projetDetailleRepository, times(1)).findAll();
    }

    @Test
    void shouldGetProjetDetaillesByProjet() {
        Projet projet = Projet.builder().id(1L).sujet("Project A").build();
        ProjetDetaille pd1 = ProjetDetaille.builder().id(1L).projet(projet).build();
        ProjetDetaille pd2 = ProjetDetaille.builder().id(2L).projet(projet).build();

        when(projetDetailleRepository.findByProjetId(1L)).thenReturn(Arrays.asList(pd1, pd2));

        List<ProjetDetaille> result = projetDetailleService.getProjetDetaillesByProjet(1L);

        assertThat(result).hasSize(2);
        verify(projetDetailleRepository, times(1)).findByProjetId(1L);
    }

    @Test
    void shouldAssignProjetDetailleToProjet() {
        ProjetDetaille projetDetaille = ProjetDetaille.builder().id(1L).description("Specs").build();
        Projet projet = Projet.builder().id(1L).sujet("Project A").build();

        when(projetDetailleRepository.findById(1L)).thenReturn(Optional.of(projetDetaille));
        when(projetRepository.findById(1L)).thenReturn(Optional.of(projet));
        when(projetDetailleRepository.save(projetDetaille)).thenReturn(projetDetaille);

        ProjetDetaille result = projetDetailleService.assignProjetDetailleToProjet(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getProjet()).isEqualTo(projet);
        verify(projetDetailleRepository, times(1)).findById(1L);
        verify(projetRepository, times(1)).findById(1L);
        verify(projetDetailleRepository, times(1)).save(projetDetaille);
    }

    @Test
    void shouldReturnNullWhenAssigningProjetDetailleToProjetAndProjetDetailleNotFound() {
        when(projetDetailleRepository.findById(999L)).thenReturn(Optional.empty());

        ProjetDetaille result = projetDetailleService.assignProjetDetailleToProjet(999L, 1L);

        assertThat(result).isNull();
        verify(projetDetailleRepository, times(1)).findById(999L);
        verify(projetDetailleRepository, never()).save(any());
    }
}
