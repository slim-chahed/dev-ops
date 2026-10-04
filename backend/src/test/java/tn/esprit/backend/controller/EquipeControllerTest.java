package tn.esprit.backend.controller;

import tn.esprit.backend.entity.Equipe;
import tn.esprit.backend.service.IEquipeService;
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
class EquipeControllerTest {

    @Mock
    private IEquipeService equipeService;

    @InjectMocks
    private EquipeController equipeController;

    @Test
    void shouldAddEquipe() {
        Equipe equipe = Equipe.builder()
                .nom("Dev Team")
                .specialite("Java")
                .build();

        when(equipeService.addEquipe(any(Equipe.class))).thenReturn(equipe);

        Equipe result = equipeController.addEquipe(equipe);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Dev Team");
        verify(equipeService, times(1)).addEquipe(any(Equipe.class));
    }

    @Test
    void shouldUpdateEquipe() {
        Equipe equipe = Equipe.builder()
                .id(1L)
                .nom("QA Team")
                .specialite("Testing")
                .build();

        when(equipeService.updateEquipe(any(Equipe.class))).thenReturn(equipe);

        Equipe result = equipeController.updateEquipe(equipe);

        assertThat(result).isNotNull();
        assertThat(result.getSpecialite()).isEqualTo("Testing");
        verify(equipeService, times(1)).updateEquipe(any(Equipe.class));
    }

    @Test
    void shouldDeleteEquipe() {
        Long id = 1L;

        doNothing().when(equipeService).deleteEquipe(id);

        equipeController.deleteEquipe(id);

        verify(equipeService, times(1)).deleteEquipe(id);
    }

    @Test
    void shouldGetEquipeById() {
        Equipe equipe = Equipe.builder()
                .id(1L)
                .nom("Dev Team")
                .specialite("Java")
                .build();

        when(equipeService.getEquipeById(1L)).thenReturn(equipe);

        Equipe result = equipeController.getEquipeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Dev Team");
        verify(equipeService, times(1)).getEquipeById(1L);
    }

    @Test
    void shouldGetAllEquipes() {
        Equipe e1 = Equipe.builder().id(1L).nom("Team A").build();
        Equipe e2 = Equipe.builder().id(2L).nom("Team B").build();

        when(equipeService.getAllEquipes()).thenReturn(Arrays.asList(e1, e2));

        List<Equipe> result = equipeController.getAllEquipes();

        assertThat(result).hasSize(2);
        verify(equipeService, times(1)).getAllEquipes();
    }

    @Test
    void shouldGetEquipesByEntreprise() {
        Equipe e1 = Equipe.builder().id(1L).nom("Team A").build();
        Equipe e2 = Equipe.builder().id(2L).nom("Team B").build();

        when(equipeService.getEquipesByEntreprise(1L)).thenReturn(Arrays.asList(e1, e2));

        List<Equipe> result = equipeController.getEquipesByEntreprise(1L);

        assertThat(result).hasSize(2);
        verify(equipeService, times(1)).getEquipesByEntreprise(1L);
    }

    @Test
    void shouldAssignEquipeToEntreprise() {
        Equipe equipe = Equipe.builder().id(1L).nom("Dev Team").build();

        when(equipeService.assignEquipeToEntreprise(1L, 1L)).thenReturn(equipe);

        Equipe result = equipeController.assignEquipeToEntreprise(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Dev Team");
        verify(equipeService, times(1)).assignEquipeToEntreprise(1L, 1L);
    }

    @Test
    void shouldAssignEquipeToProjet() {
        Equipe equipe = Equipe.builder().id(1L).nom("Dev Team").build();

        when(equipeService.assignEquipeToProjet(1L, 1L)).thenReturn(equipe);

        Equipe result = equipeController.assignEquipeToProjet(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Dev Team");
        verify(equipeService, times(1)).assignEquipeToProjet(1L, 1L);
    }
}
