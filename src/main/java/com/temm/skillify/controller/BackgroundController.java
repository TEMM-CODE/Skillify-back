package com.temm.skillify.controller;


import com.temm.skillify.model.enums.BackgroundColorEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/backgrounds")
@RequiredArgsConstructor
public class BackgroundController {

    @GetMapping
    public ResponseEntity<List<String>> getAllBackgroundHexValues() {
        List<String> hexValues = Arrays.stream(BackgroundColorEnum.values())
                .map(BackgroundColorEnum::getHex)
                .collect(Collectors.toList());
        return ResponseEntity.ok(hexValues);
    }
}
