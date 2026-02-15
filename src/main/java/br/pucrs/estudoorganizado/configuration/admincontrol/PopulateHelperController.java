package br.pucrs.estudoorganizado.configuration.admincontrol;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/populate-helper")
public class PopulateHelperController {

    private final PopulateHelperService service;

    @PostMapping
    public ResponseEntity<Void> populateDate() {
        service.populateData();
        return ResponseEntity.ok().build();
    }
}
