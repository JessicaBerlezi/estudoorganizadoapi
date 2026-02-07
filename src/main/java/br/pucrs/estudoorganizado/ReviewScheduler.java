package br.pucrs.estudoorganizado;

import br.pucrs.estudoorganizado.service.ReviewControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewScheduler {

    private final ReviewControlService reviewControlService;

    /**
     * Executa diariamente a atualização dos status das revisões.
     * Mantém o sistema sincronizado com o calendário.
     */
    @Scheduled(cron = "0 0 2 * * ?") // todo dia às 02:00
    public void updateDailyReviewStatuses() {
        reviewControlService.updateReviewStatuses();
    }
}
