package tn.esprit.backend.service.impl;

import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.entity.Equipe;
import tn.esprit.backend.entity.Projet;
import tn.esprit.backend.repository.EntrepriseRepository;
import tn.esprit.backend.repository.EquipeRepository;
import tn.esprit.backend.repository.ProjetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipeServiceImplTest {

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private EntrepriseRepository entrepriseRepository;

    @Mock
    private ProjetRepository projetRepository;

    @InjectMocks
    private EquipeServiceImpl equipeService;

    @Test
    void shouldAddEquipe() {
        Equipe equipe = Equipe.builder()
                .nom("Dev Team")
                .specialite("Java")
                .build();

        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.addEquipe(equipe);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Dev Team");
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void shouldUpdateEquipe() {
        Equipe equipe = Equipe.builder()
                .id(1L)
                .nom("QA Team")
                .specialite("Testing")
                .build();

        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.updateEquipe(equipe);

        assertThat(result).isNotNull();
        assertThat(result.getSpecialite()).isEqualTo("Testing");
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void shouldDeleteEquipe() {
        Long id = 1L;

        doNothing().when(equipeRepository).deleteById(id);

        equipeService.deleteEquipe(id);

        verify(equipeRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldGetEquipeByIdWhenExists() {
        Equipe equipe = Equipe.builder()
                .id(1L)
                .nom("Dev Team")
                .build();

        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));

        Equipe result = equipeService.getEquipeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Dev Team");
        verify(equipeRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnNullWhenEquipeDoesNotExist() {
        when(equipeRepository.findById(999L)).thenReturn(Optional.empty());

        Equipe result = equipeService.getEquipeById(999L);

        assertThat(result).isNull();
        verify(equipeRepository, times(1)).findById(999L);
    }

    @Test
    void shouldGetAllEquipes() {
        Equipe e1 = Equipe.builder().id(1L).nom("Team A").build();
        Equipe e2 = Equipe.builder().id(2L).nom("Team B").build();

        when(equipeRepository.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<Equipe> result = equipeService.getAllEquipes();

        assertThat(result).hasSize(2);
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    void shouldGetEquipesByEntreprise() {
        Equipe e1 = Equipe.builder().id(1L).nom("Team A").build();
        Equipe e2 = Equipe.builder().id(2L).nom("Team B").build();

        when(equipeRepository.findByEntrepriseId(1L)).thenReturn(Arrays.asList(e1, e2));

        List<Equipe> result = equipeService.getEquipesByEntreprise(1L);

        assertThat(result).hasSize(2);
        verify(equipeRepository, times(1)).findByEntrepriseId(1L);
    }

    @Test
    void shouldAssignEquipeToEntreprise() {
        Equipe equipe = Equipe.builder().id(1L).nom("Dev Team").build();
        Entreprise entreprise = Entreprise.builder().id(1L).nom("Test Corp").build();

        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(entrepriseRepository.findById(1L)).thenReturn(Optional.of(entreprise));
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.assignEquipeToEntreprise(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getEntreprise()).isEqualTo(entreprise);
        verify(equipeRepository, times(1)).findById(1L);
        verify(entrepriseRepository, times(1)).findById(1L);
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void shouldReturnNullWhenAssigningEquipeToEntrepriseAndEquipeNotFound() {
        when(equipeRepository.findById(999L)).thenReturn(Optional.empty());

        Equipe result = equipeService.assignEquipeToEntreprise(999L, 1L);

        assertThat(result).isNull();
        verify(equipeRepository, times(1)).findById(999L);
        verify(equipeRepository, never()).save(any());
    }

    @Test
    void shouldAssignEquipeToProjet() {
        Equipe equipe = Equipe.builder().id(1L).nom("Dev Team").build();
        equipe.setProjets(new ArrayList<>());
        Projet projet = Projet.builder().id(1L).sujet("New App").build();

        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(projetRepository.findById(1L)).thenReturn(Optional.of(projet));
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.assignEquipeToProjet(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getProjets()).contains(projet);
        verify(equipeRepository, times(1)).findById(1L);
        verify(projetRepository, times(1)).findById(1L);
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void shouldReturnNullWhenAssigningEquipeToProjetAndEquipeNotFound() {
        when(equipeRepository.findById(999L)).thenReturn(Optional.empty());

        Equipe result = equipeService.assignEquipeToProjet(999L, 1L);

        assertThat(result).isNull();
        verify(equipeRepository, times(1)).findById(999L);
        verify(equipeRepository, never()).save(any());
    }
}
