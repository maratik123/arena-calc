package org.maratik.arena.calc.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by maratik.
 */
@Service
public class BattleService {
    public String createToken() {
        return UUID.randomUUID().toString();
    }
}
