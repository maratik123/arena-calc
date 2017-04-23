package org.maratik.arena.calc.controller;

import io.swagger.annotations.ApiOperation;
import org.maratik.arena.calc.model.dto.BattleToken;
import org.maratik.arena.calc.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by maratik.
 */
@Controller
@RequestMapping("/service/battle")
public class BattleController {

    private final BattleService battleService;

    @Autowired
    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @GetMapping(path = "/create")
    @ApiOperation("Create battle")
    @ResponseBody
    public BattleToken create() {
        return new BattleToken(battleService.createToken());
    }
}
