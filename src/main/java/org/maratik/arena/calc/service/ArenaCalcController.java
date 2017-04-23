package org.maratik.arena.calc.service;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * Created by maratik.
 */
@Controller
@RequestMapping("/service")
public class ArenaCalcController {
    @GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String ping() {
        return "Ok";
    }

    @GetMapping(path = "/battle/create")
    @ApiOperation("Create battle")
    public String createBattle() {
        return UUID.randomUUID().toString();
    }
}
