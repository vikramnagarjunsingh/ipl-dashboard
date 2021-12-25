package com.example.ipldashboard.controller;

import com.example.ipldashboard.model.Team;
import com.example.ipldashboard.repository.TeamRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamController {

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    private final TeamRepository teamRepository;

    @GetMapping("/team/{teamName}")
    private Team getTeam(@PathVariable String teamName){
        return this.teamRepository.getTeamWithLatestMatches(teamName);
    }

}
